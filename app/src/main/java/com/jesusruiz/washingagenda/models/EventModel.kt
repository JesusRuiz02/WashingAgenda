package com.jesusruiz.washingagenda.models

class EventModel(userID: String = "",
                 departmentN: String = "",
                 building: String = "",
                 startDate: String = "",
                 endDate: String = "",
                 status: EventStatus = EventStatus.Pending,
    )

enum class EventStatus {Pending, Active, Completed,Canceled}