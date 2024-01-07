package com.example.student_teacherapplication.data

data class UserModel(
    val name: String,
    val lastname: String,
    val pictureUrl: String?
) {
    constructor() : this("", "", "")
}




