package capstonedesign.globalrounge.QRJob

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import capstonedesign.globalrounge.MainJob.User
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.databinding.ActivityQrBinding

class QRActivity : AppCompatActivity(){

    //데이터바인딩 변수
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)
        val user = intent.getSerializableExtra("user") as User
        binding.textView.text = user.id

    }
}