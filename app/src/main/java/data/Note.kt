package data

import java.io.Serializable

data class Note(
    val id: String,
    val subjectName: String,
    val text: String,
    var completed: Boolean = false
) : Serializable

