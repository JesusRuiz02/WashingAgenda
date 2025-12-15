package com.jesusruiz.washingagenda.models

data class UserModel(val userID: String,
                     var userType: String,
                     val name:String,
                     val departmentN: String,
                     val email: String,
                     val building: String,
                     val hours: Int)