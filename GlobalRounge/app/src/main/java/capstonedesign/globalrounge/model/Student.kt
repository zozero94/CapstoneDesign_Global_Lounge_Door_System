package capstonedesign.globalrounge.model

import java.io.Serializable

/**
 * 학생 개인정보가 담기는 dataClass
 */
data class Student(
    val studentID: String,
    val name: String,
    val gender: String,
    val nationality: String,
    val department: String,
    val college: String
) : Serializable