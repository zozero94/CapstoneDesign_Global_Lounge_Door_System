package capstonedesign.globallounge.mainjob

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import capstonedesign.globallounge.R
import capstonedesign.globallounge.databinding.ActivityMainBinding
import capstonedesign.globallounge.dto.Student
import capstonedesign.globallounge.dto.User
import capstonedesign.globallounge.model.permission.ServerConnection
import capstonedesign.globallounge.qrjob.QrActivity


class MainActivity : AppCompatActivity(), MainContract.View {

    //데이터 바인딩 변수
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    //MVP의 Presenter
    private val presenter by lazy { MainPresenter(this@MainActivity, this) }

    private var loadingThread: Thread? = null

    //뒤로가기 버튼에 사용되는 변수
    private var time: Long = 0

    companion object {
        const val REQUEST_CODE = 100
    }

    /**************** [ Override Function ] ****************/
    override fun onCreate(savedInstanceState: Bundle?) {
        //기본 View Setting
        super.onCreate(savedInstanceState)

        if (Settings.Secure.getInt(applicationContext.contentResolver, Settings.Secure.LOCATION_MODE) == 0) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            Toast.makeText(applicationContext, "높은 정확도 사용을 권장합니다.", Toast.LENGTH_LONG).show()
        }
        BluetoothAdapter.getDefaultAdapter()?.let {
            if (!it.isEnabled)
                startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }


        //Login Button Listener
        binding.loginBtn.setOnClickListener {
            User(
                id = binding.id.text.toString(),
                pw = binding.pw.text.toString()
            ).apply { presenter.loginClicked(this) }
        }
        //CheckBox Listener
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.changeCheckState(isChecked)
        }

        //ip 변경이스터 에그
        binding.imageView.setOnClickListener {
            if (System.currentTimeMillis() - time >= 2000) {
                time = System.currentTimeMillis()

            } else if (System.currentTimeMillis() - time < 2000) {
                alertToast("ip 변경 완료")
                ServerConnection.setIp(binding.id.text.toString())
            }
        }
    }


    override fun updateUserInfo(user: User) {
        binding.id.setText(user.id)
        binding.pw.setText(user.pw)
    }

    override fun onResume() {
        super.onResume()
        //자동 로그인을 체크
        binding.checkBox.isChecked = presenter.checkAutoLogin()
    }

    override fun onStop() {
        super.onStop()
        presenter.dispose()
    }

    override fun loadingStart() {
        loadingThread =
            Thread(Runnable {
                AnimationUtils.loadAnimation(this, R.anim.rotate_anim).apply {
                    binding.loading.startAnimation(this)
                }
            })
        loadingThread?.start()
        binding.loading.visibility = View.VISIBLE

    }

    override fun loadingDestroy() {
        if ((loadingThread?.isInterrupted == false)) {
            loadingThread?.interrupt()
            binding.loading.clearAnimation()
            binding.loading.visibility = View.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            binding.id.setText("")
            binding.pw.setText("")
            presenter.deletePreference()
        }
    }

    override fun alertToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            alertToast("뒤로 버튼을 한번 더 누르면 종료합니다.")
        } else if (System.currentTimeMillis() - time < 2000) {
            finishAffinity()
        }
    }

    override fun startActivity(student: Student) {
        startActivityForResult(
            QrActivity.getIntent(this@MainActivity, student),
            REQUEST_CODE
        )
    }


}
