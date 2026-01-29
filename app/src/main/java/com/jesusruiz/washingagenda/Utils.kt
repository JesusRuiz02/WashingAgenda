package com.jesusruiz.washingagenda

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

fun LocalDateTime.toDate(): Date{
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

fun Date.toLocalDateTime(): LocalDateTime{
    return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun Color.toHexString(): String {
    return String.format("#%08X", this.toArgb())
}

fun LocalDateTime.CeilToNextSlot(): LocalDateTime {
    if (this.minute > 0 && this.minute < 30) {
        return this.withMinute(30).withSecond(0).withNano(0)
    } else if(this.minute > 30) {
        val missingMinuteToAnHour = 60 - this.minute
        val newTime = this.plusMinutes(missingMinuteToAnHour.toLong())
        return newTime
    }
    else{
        return this
    }
}




fun String.toComposeColor(): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: IllegalArgumentException) {
        Color.Gray
    }
}

fun Long.longToLocalDate(): LocalDate {
    require(this  >= 0) { "Epoch milliseconds must be non-negative" }
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.of("UTC"))
        .toLocalDate()
}

fun LocalTime.withOutSeconds(): LocalTime {
    return LocalTime.of(this.hour, this.minute)
}