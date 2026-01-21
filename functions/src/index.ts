/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import { onSchedule } from "firebase-functions/v2/scheduler";
import * as admin from "firebase-admin"

admin.initializeApp();
const db = admin.firestore();

export const updateUserEverySunday = onSchedule(
        {
            schedule: "59 23 * * 0",
            timeZone: "America/Mexico_City"
        },
    async () => {
        const snapshot = await db.collection("Users").get();
               let updated = 0;
               let batch = db.batch();
               let bathCount = 0;

               for(const doc of snapshot.docs){
                    batch.update(doc.ref,
                        {
                            hours: 10,
                        });

                    bathCount++;
                    updated++;
                    if(bathCount === 500){
                        await batch.commit();
                        batch = db.batch();
                        bathCount = 0;
                    }
                   }
                   if(bathCount > 0){
                    await batch.commit();
                   }
               console.log('Actualizados ${updated} documentos');
        }
    )




export const updateEventEverySunday = onSchedule(
        {
            schedule: "59 23 * * 0",
            timeZone: "America/Mexico_City"
        },
    async () => {
        const snapshot = await db.collection("Events").get();
               let updated = 0;
               let batch = db.batch();
               let bathCount = 0;

               for(const doc of snapshot.docs){
                   const data = doc.data();
                    if(!data.endDate)
                    {
                        continue;
                    }
                    const endDate = data.endDate.toDate();
                    const newStatus = endDate < new Date() ? "Completed" : "Active";

                    batch.update(doc.ref,
                        {
                            status: newStatus,
                        });

                    bathCount++;
                    updated++;
                    if(bathCount === 500){
                        await batch.commit();
                        batch = db.batch();
                        bathCount = 0;
                    }
                   }
                   if(bathCount > 0){
                    await batch.commit();
                   }
               console.log('Actualizados ${updated} documentos');
        }
    );

export const deletedOldEvents =  onSchedule(
    {
        schedule: "59 23 * * 0",
        timeZone: "America/Mexico_City",
      },
      async () => {
        const oneMonthAgo = new Date();
        oneMonthAgo.setMonth(oneMonthAgo.getMonth() - 1);

        const oneMonthAgoTs =
          admin.firestore.Timestamp.fromDate(oneMonthAgo);


        const snapshot = await db
          .collection("events")
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



// Start writing functions
// https://firebase.google.com/docs/functions/typescript

// For cost control, you can set the maximum number of containers that can be
// running at the same time. This helps mitigate the impact of unexpected
// traffic spikes by instead downgrading performance. This limit is a
// per-function limit. You can override the limit for each function using the
// `maxInstances` option in the function's options, e.g.
// `onRequest({ maxInstances: 5 }, (req, res) => { ... })`.
// NOTE: setGlobalOptions does not apply to functions using the v1 API. V1
// functions should each use functions.runWith({ maxInstances: 10 }) instead.
// In the v1 API, each function can only serve one request per container, so
// this will be the maximum concurrent request count.


// export const helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
