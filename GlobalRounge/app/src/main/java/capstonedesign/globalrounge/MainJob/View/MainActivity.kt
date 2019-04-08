package capstonedesign.globalrounge.MainJob.View

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Presenter.MainPresenter
import capstonedesign.globalrounge.MainJob.User
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainMVP.View {

    //데이터 바인딩 변수
    private lateinit var binding: ActivityMainBinding

    //MVP의 Presenter
    private val presenter = MainPresenter()

    //뒤로가기 버튼에 사용되는 변수
    private var time: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        //기본 View Setting
        super.onCreate(savedInstanceState)
        //데이터바인딩을 통한 XML코드와 연결
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //presenter Setting
        presenter.init(this, this)

        //Login Button Listener
        binding.loginBtn.setOnClickListener {

            val user = with(User()){
                id = binding.id.text.toString()
                pw = binding.pw.text.toString()
                return@with this
            }

            presenter.loginClicked(user)
        }

        //자동 로그인을 체크
        binding.checkBox.isChecked = presenter.checkAutoLogin()

        //CheckBox Listener
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.changeCheckState(isChecked)
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

    override fun startActivity() {
        makeToast("앙 제대로 동작띠")
    }
}
