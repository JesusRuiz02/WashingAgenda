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

data class FirestoreEvent(
    val id: String = "",
    val userID: String = "",
    val departmentN: String = "",
    val building: String = "",
    val startDate: Date? = null,
    val endDate: Date? = null,
    val color: String = "#FFFFFFFF",
    val status: String = "Active"
)




enum class EventStatus {Pending, Active, Completed,Canceled}