package com.example.student_teacherapplication.data


data class AppointmentModel(
    val teacherId: String,
    val userId: String,
    val teacherName: String,
    val dateTime: String
) {
    constructor() : this("", "", "", "")
}
