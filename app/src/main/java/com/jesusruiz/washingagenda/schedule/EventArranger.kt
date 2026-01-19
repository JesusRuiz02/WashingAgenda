package com.jesusruiz.washingagenda.schedule

import com.jesusruiz.washingagenda.models.EventModel
import com.jesusruiz.washingagenda.toLocalDateTime
import kotlin.math.max

data class VisualEvent(
    val event: EventModel,
    var widthFraction: Float = 1f,
    var xOffsetFraction: Float = 0f
)

fun arrangeEvents(events: List<EventModel>): List<VisualEvent> {
    if (events.isEmpty()) return emptyList()

    val visualEvents = events.map { VisualEvent(it) }

    val sortedVisualEvents = visualEvents.sortedBy { it.event.startDate }

    var i = 0
    while (i < sortedVisualEvents.size) {
        val currentEvent = sortedVisualEvents[i]
        val collisionGroup = mutableListOf(currentEvent)

        var j = i + 1
        while (j < sortedVisualEvents.size) {
            val nextEvent = sortedVisualEvents[j]
            if (nextEvent.event.startDate!! < currentEvent.event.endDate!!) {
                collisionGroup.add(nextEvent)
            }
            j++
        }

        if (collisionGroup.size > 1) {
            val tracks = mutableListOf<MutableList<VisualEvent>>()

            val sortedGroup = collisionGroup.sortedBy { it.event.startDate }

            for (eventInGroup in sortedGroup) {
                var placed = false
                for (track in tracks) {
                    val lastEventInTrack = track.last()
                    if (eventInGroup.event.startDate!! >= lastEventInTrack.event.endDate!!) {
                        track.add(eventInGroup)
                        placed = true
                        break
                    }
                }
                if (!placed) {
                    tracks.add(mutableListOf(eventInGroup))
                }
            }

            val numDivisions = tracks.size

            for ((trackIndex, track) in tracks.withIndex()) {
                for (visualEventInTrack in track) {

                    visualEventInTrack.widthFraction = (1f / numDivisions).coerceAtMost(visualEventInTrack.widthFraction)
                    visualEventInTrack.xOffsetFraction = max(visualEventInTrack.xOffsetFraction, trackIndex.toFloat() / numDivisions)
                }
            }
        }
        i++
    }

    return sortedVisualEvents
}
