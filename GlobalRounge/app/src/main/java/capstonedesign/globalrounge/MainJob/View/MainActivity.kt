package capstonedesign.globalrounge.MainJob.View

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.Toast
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Presenter.MainPresenter
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainMVP.View {


    private lateinit var binding: ActivityMainBinding
    private val presenter = MainPresenter(this)
    private var time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        //기본 View Setting
        super.onCreate(savedInstanceState)
        //데이터바인딩을 통한 XML코드와 연결
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.loginBtn.setOnClickListener {
            presenter.loginClicked(
                binding.id.text.toString(),
                binding.pw.text.toString()
            )
        }
    }




    override fun noInformation(text: String) {
        makeToast(text)
    }


    override fun rejectPermission(text: String) {
        makeToast(text)
    }


    private fun makeToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            makeToast("뒤로 버튼을 한번 더 누르면 종료합니다.")
        } else if (System.currentTimeMillis() - time < 2000) {
            finish()
        }
    }
}
