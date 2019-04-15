package capstonedesign.globalrounge.mainjob.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import capstonedesign.globalrounge.mainjob.MainContract
import capstonedesign.globalrounge.mainjob.presenter.MainPresenter
import capstonedesign.globalrounge.mainjob.User
import capstonedesign.globalrounge.qrjob.view.QrActivity
import capstonedesign.globalrounge.R
import capstonedesign.globalrounge.Student
import capstonedesign.globalrounge.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), MainContract.View {

    //데이터 바인딩 변수
    private lateinit var binding: ActivityMainBinding

    //MVP의 Presenter
    private lateinit var presenter: MainContract.Presenter

    //뒤로가기 버튼에 사용되는 변수
    private var time: Long = 0

    companion object {
        const val REQUEST_CODE = 100
    }

    /**************** [ Override Function ] ****************/
    override fun onCreate(savedInstanceState: Bundle?) {
        //기본 View Setting
        super.onCreate(savedInstanceState)
        //데이터바인딩을 통한 XML코드와 연결
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //presenter Setting
        presenter = MainPresenter(this, this)
        //Login Button Listener
        binding.loginBtn.setOnClickListener {
            val user = User(
                id = binding.id.text.toString(),
                pw = binding.pw.text.toString()
            )
            presenter.loginClicked(user)
        }
        //CheckBox Listener
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.changeCheckState(isChecked)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.dispose()
    }

    override fun onResume() {
        super.onResume()
        //자동 로그인을 체크
        binding.checkBox.isChecked = presenter.checkAutoLogin()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //TODO Logout으로 넘어왓을 때 자동로그인 해제
        if (requestCode == REQUEST_CODE) {
            binding.id.setText("")
            binding.pw.setText("")
            presenter.deletePreference()
        }
    }

    override fun alertToast(text: String) {
        makeToast(text)
    }


    override fun onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            makeToast("뒤로 버튼을 한번 더 누르면 종료합니다.")
        } else if (System.currentTimeMillis() - time < 2000) {
            finishAffinity()
        }
    }

    override fun startActivity(student: Student) {
        startActivityForResult(QrActivity.getIntent(this@MainActivity,student), REQUEST_CODE)
    }


    /**************** [ Local Function ] ****************/
    private fun makeToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}
