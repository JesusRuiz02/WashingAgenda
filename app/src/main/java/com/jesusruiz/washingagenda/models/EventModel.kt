package com.jesusruiz.washingagenda.models

import kotlinx.datetime.LocalDateTime

data class EventModel(val userID: String = "",
                 val departmentN: String = "",
                val building: String = "",
                var startDate: LocalDateTime,
               var endDate: LocalDateTime,
               var status: EventStatus = EventStatus.Pending,
    )

enum class EventStatus {Pending, Active, Completed,Canceled}