package com.jesusruiz.washingagenda.models


import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime
import java.util.Date


data class EventModel(
                      val id: String = "",
                      val userID: String = "",
                      val departmentN: String = "",
                      val building: String = "",
                      var startDate: LocalDateTime? = null,
                      var endDate: LocalDateTime? = null,
                      var status: EventStatus = EventStatus.Active,
                      var color: Color = Color.Blue
    )




enum class EventStatus {Pending, Active, Completed,Canceled}