package capstonedesign.globalrounge.QRJob

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import capstonedesign.globalrounge.MainJob.User
import capstonedesign.globalrounge.MainJob.View.MainActivity.Companion.REQUEST_CODE
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.databinding.ActivityQrBinding

/**
 * 미완성 더미 액티비티
 */
class QRActivity : AppCompatActivity(){

    //데이터바인딩 변수
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)
        val user = intent.getSerializableExtra("user") as User
        binding.textView.text = user.id
        binding.logout.setOnClickListener {
            setResult(REQUEST_CODE)
            finish()
        }
    }



    override fun onBackPressed() {
        finishAffinity()
    }
}