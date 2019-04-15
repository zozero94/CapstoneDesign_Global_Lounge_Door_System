package capstonedesign.globalrounge.model

import java.io.Serializable

data class User(val id: String = "", val pw: String = "", var tag: Int = STUDENT) : Serializable

const val STUDENT = 0
const val ADMIN = 1