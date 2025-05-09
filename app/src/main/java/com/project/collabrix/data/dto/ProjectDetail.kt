package com.project.collabrix.data.dto

data class ProjectDetail(
    val id: Int,
    val title: String,
    val description: String,
    val requirements: List<String>,
    val startDate: String,
    val endDate: String,
    val deadline: String,
    val students: List<Student> = emptyList(),
    val professorId: Int? = null,
    val professorName: String? = null
)

data class Student(
    val id: Int,
    val name: String
) 