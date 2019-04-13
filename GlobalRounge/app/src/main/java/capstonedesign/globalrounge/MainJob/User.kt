package capstonedesign.globalrounge.MainJob

import java.io.Serializable

data class User(var id:String="",var pw:String="",var tag:Int= STUDENT) :Serializable
const val STUDENT = 0
const val ADMIN = 1