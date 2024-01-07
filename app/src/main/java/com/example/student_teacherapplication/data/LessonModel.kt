package com.example.student_teacherapplication.data

data class LessonModel(val name: String, val teachers: List<String>) {
    constructor() : this("", listOf())
}