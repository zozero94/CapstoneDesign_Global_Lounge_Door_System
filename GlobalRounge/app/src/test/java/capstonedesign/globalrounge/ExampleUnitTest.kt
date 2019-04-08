package capstonedesign.globalrounge

import capstonedesign.globalrounge.MainJob.Model.SejongPermission
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        SejongPermission(object :
            SejongPermission.RejectionCallback {
            override fun approval(id: String) {
                System.out.println("기모찌이 $id")

            }

            override fun rejectId() {
                println("아이디 거부")
            }

            override fun rejectPw() {
                println("패스워드 거부")
            }

        }).requestUserInformation("14011038","wodud31")
    }
}
