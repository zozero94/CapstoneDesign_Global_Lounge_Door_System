package capstonedesign.globalrounge.qrjob

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.databinding.ActivityQrBinding
import capstonedesign.globalrounge.dto.Student
import capstonedesign.globalrounge.mainjob.MainActivity.Companion.REQUEST_CODE
import com.bumptech.glide.Glide


class QrActivity : AppCompatActivity(), QrContract.View {


    //데이터바인딩 변수
    private val binding: ActivityQrBinding by lazy {
        DataBindingUtil.setContentView<ActivityQrBinding>(this@QrActivity, R.layout.activity_qr)
    }

    private val presenter = QrPresenter(this@QrActivity)
    private val params by lazy { window.attributes }
    private var origin: Float =0.0f

    init {
        presenter.subscribe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (intent.getSerializableExtra(EXTRA_USER) as Student).let { binding.user = it }

        binding.logout.setOnClickListener {
            setResult(REQUEST_CODE)
            presenter.logout()
            finish()
        }

    }

    override fun makeQrCode(bitmap: Bitmap) {
        binding.qr.setImageBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()

        origin = params.screenBrightness
        params.screenBrightness = 1f
        window.attributes = params

        presenter.stateRequest()
    }


    override fun onPause() {
        super.onPause()

        params.screenBrightness = origin
        window.attributes = params

        presenter.stateDelete()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    override fun alertToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun drawUserImages(url: String) {
        Glide
            .with(this)
            .load(url)
            .asBitmap()
            .error(R.mipmap.jaeho)
            .into(binding.userImage)
    }


    companion object {
        private const val EXTRA_USER = "EXTRA_USER"
        fun getIntent(context: Context?, student: Student) = Intent(context, QrActivity::class.java).apply {
            putExtra(EXTRA_USER, student)
        }

    }
}