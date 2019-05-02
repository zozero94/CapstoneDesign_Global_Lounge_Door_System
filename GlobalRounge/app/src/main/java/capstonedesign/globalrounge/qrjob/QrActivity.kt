package capstonedesign.globalrounge.qrjob

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.databinding.ActivityQrBinding
import capstonedesign.globalrounge.dto.Student
import capstonedesign.globalrounge.mainjob.MainActivity.Companion.REQUEST_CODE


class QrActivity : AppCompatActivity(), QrContract.View {

    //데이터바인딩 변수
    private lateinit var binding: ActivityQrBinding
    private val presenter = QrPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr)

        (intent.getSerializableExtra(EXTRA_USER) as Student).let { binding.user = it }

        presenter.subscribe()
        binding.logout.setOnClickListener {
            setResult(REQUEST_CODE)
            finish()
        }

    }

    override fun makeQrCode(bitmap: Bitmap) {

        binding.qr.setImageBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        presenter.stateRequest()
    }


    override fun onPause() {
        super.onPause()
        presenter.stateDelete()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.logout()
        Thread.sleep(500)
        presenter.dispose()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun alertToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun drawUserImages(bitmap: Bitmap) {
        binding.userImage.setImageBitmap(bitmap)
    }

    companion object {
        private const val EXTRA_USER = "EXTRA_USER"
        fun getIntent(context: Context?, student: Student) = Intent(context, QrActivity::class.java).apply {
            putExtra(EXTRA_USER, student)
        }

    }
}