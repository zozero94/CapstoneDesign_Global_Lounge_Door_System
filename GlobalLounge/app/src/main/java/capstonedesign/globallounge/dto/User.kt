package capstonedesign.globallounge.dto

import java.io.Serializable

/**
 * 로그인 정보가 담기는 data class
 */
data class User(val id: String = "", val pw: String = "", var tag: Int = STUDENT) : Serializable

const val STUDENT = 1
const val ADMIN = 2