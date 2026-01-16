package com.jesusruiz.washingagenda.models


import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime


data class EventModel(val id: String = "",
                      val userID: String = "",
                      val departmentN: String = "",
                      val building: String = "",
                      var startDate: LocalDateTime = LocalDateTime.now(),
                      var endDate: LocalDateTime = LocalDateTime.now(),
                      var status: EventStatus = EventStatus.Active,
                      var color: Color = Color.DarkGray
    )

enum class EventStatus {Pending, Active, Completed,Canceled}