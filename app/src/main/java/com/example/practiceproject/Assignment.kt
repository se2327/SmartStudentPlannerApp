package com.example.practiceproject

data class Assignment (
    val subject: String="",
    val topic: String="",
    val dueDate: String="",
    var isCompleted: Boolean = false,
    val id: String = "" // Optional: useful for deleting by ID
)