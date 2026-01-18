package com.jesusruiz.washingagenda

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import java.time.LocalDateTime
import java.util.Date

fun LocalDateTime.toDate(): Date{
    return Date.from(this.atZone(java.time.ZoneId.systemDefault()).toInstant())
}

fun Date.toLocalDateTime(): LocalDateTime{
    return this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
}

fun Color.toHexString(): String {
    return String.format("#%08X", this.toArgb())
}

fun String.toComposeColor(): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: IllegalArgumentException) {
        Color.Gray
    }
}