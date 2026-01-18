package com.jesusruiz.washingagenda.models


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.time.LocalDateTime
import java.util.Date




data class EventModel(
                      val id: String = "",
                      val userID: String = "",
                      val departmentN: String = "",
                      val building: String = "",
                      @ServerTimestamp
                      var startDate: Date? = null,
                      @ServerTimestamp
                      var endDate: Date? = null,
                      var status: EventStatus = EventStatus.Active,
                      var color: String = "FF6200EE"
    )


enum class EventStatus {Pending, Active, Completed,Canceled}