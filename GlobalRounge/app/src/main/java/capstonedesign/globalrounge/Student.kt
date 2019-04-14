package capstonedesign.globalrounge

import java.io.Serializable


data class Student (
    val studentID: String,
    val name: String,
    val gender: String,
    val nationality: String,
    val department: String,
    val college: String
):Serializable