package com.jesusruiz.washingagenda.models

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime


data class EventModel(val id: String = "",
                      val userID: String = "",
                      val departmentN: String = "",
                      val building: String = "",
                      var startDate: LocalDateTime,
                      var endDate: LocalDateTime,
                      var status: EventStatus = EventStatus.Pending,
                      var color: Color
    )

enum class EventStatus {Pending, Active, Completed,Canceled}