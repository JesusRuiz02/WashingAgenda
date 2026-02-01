/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import { onSchedule } from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin";
import { onCall, HttpsError } from "firebase-functions/v2/https";






admin.initializeApp();
const db = admin.firestore();

export const createAuthUser = onCall(async (request: any) => {
  const { email, password } = request.data;

  if (!email || !password) {
    throw new HttpsError("invalid-argument", "Email y password requeridos");
  }

  const user = await admin.auth().createUser({
    email,
    password,
  });

  const verificationLink = await admin.auth().generateEmailVerificationLink(email);

  return {
    uid: user.uid,
    email: user.email,
    verificationSent: true
  };
});


export const updateUserEverySunday = onSchedule(
    {
        schedule: "59 23 * * 0",
        timeZone: "America/Mexico_City"
    },
    async () => {
        const buildingsSnapshot = await db.collection("Building").get();
        const buildingsDataMap = new Map<string, number>();

        buildingsSnapshot.forEach((doc) => {
            const data = doc.data();
            const buildingId = doc.id;
            const maxHoursPerWeek = data.maxHoursPerWeek;

            if (maxHoursPerWeek !== undefined && typeof maxHoursPerWeek === 'number') {
                buildingsDataMap.set(buildingId, maxHoursPerWeek);
            }
        });

        const usersSnapshot = await db.collection("Users").get();
        let updated = 0;
        let batch = db.batch();
        let batchCount = 0;

        for (const doc of usersSnapshot.docs) {
            const userData = doc.data();
            const userBuildingId = userData.building;

            if (userBuildingId && buildingsDataMap.has(userBuildingId)) {
                const newHours = buildingsDataMap.get(userBuildingId);

                batch.update(doc.ref, {
                    washHours: newHours,
                });

                updated++;
                batchCount++;
            } else {
                console.log(`Usuario ${doc.id} pertenece a un edificio (${userBuildingId}) sin datos de horas.`);
            }

            if (batchCount === 500) {
                await batch.commit();
                batch = db.batch();
                batchCount = 0;
            }
        }

        if (batchCount > 0) {
            await batch.commit();
        }
        console.log(`Actualizados ${updated} documentos de usuarios.`);
    }
);

export const updateEventEveryHour = onSchedule(
    {
        schedule: "00 * * * *",
        timeZone: "America/Mexico_City"
    },
    async () => {
        const buildingsSnapshot = await db.collection("Building").get();
        const buildingsCancellationMap = new Map<string, number>();

        buildingsSnapshot.forEach((doc) => {
            const data = doc.data();
            const windowHourTolerance = data.windowHourTolerance;

            if (windowHourTolerance !== undefined && typeof windowHourTolerance === 'number') {
                buildingsCancellationMap.set(doc.id, windowHourTolerance);
            }
        });

        const snapshot = await db.collection("Events").get();
        let updated = 0;
        let batch = db.batch();
        let batchCount = 0;

        for (const doc of snapshot.docs) {
            const data = doc.data();
            if (!data.endDate || !data.startDate) {
                continue;
            }
            if (data.status === "Canceled") {
                continue;
            }

            const endDate = data.endDate.toDate();
            const startDate = data.startDate.toDate();
            const now = new Date();

            const buildingId = data.building;
            const cancelWindowHours = buildingsCancellationMap.get(buildingId) || 0;

            const startDatePlusOneHour = new Date(startDate.getTime());
            startDatePlusOneHour.setHours(startDate.getHours() + cancelWindowHours);

            let newStatus = "";
            if (endDate < now) {
                newStatus = "Completed";
            } else if (startDate <= now) {
                newStatus = "Active";
            } else if (startDatePlusOneHour <= now) {
                newStatus = "Scheduled";
            }

            if (newStatus == "" || newStatus == data.status) {
                continue;
            }

            batch.update(doc.ref, {
                status: newStatus,
            });

            batchCount++;
            updated++;
            if (batchCount === 500) {
                await batch.commit();
                batch = db.batch();
                batchCount = 0;
            }
        }
        if (batchCount > 0) {
            await batch.commit();
        }
        console.log(`Actualizados ${updated} documentos`);
    }
);

export const deletedOldEvents = onSchedule(
    {
        schedule: "59 23 * * 0",
        timeZone: "America/Mexico_City",
    },
    async () => {
        const oneMonthAgo = new Date();
        oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);

        const oneMonthAgoTs = admin.firestore.Timestamp.fromDate(oneMonthAgo);

        const snapshot = await db
            .collection("Events")
            .where("endDate", "<", oneMonthAgoTs)
            .get();

        let deleted = 0;
        let batch = db.batch();
        let batchCount = 0;

        for (const doc of snapshot.docs) {
            batch.delete(doc.ref);
            deleted++;
            batchCount++;

            if (batchCount === 500) {
                await batch.commit();
                batch = db.batch();
                batchCount = 0;
            }
        }

        if (batchCount > 0) {
            await batch.commit();
        }

        console.log(`Eliminados ${deleted} eventos antiguos`);
    }
);
