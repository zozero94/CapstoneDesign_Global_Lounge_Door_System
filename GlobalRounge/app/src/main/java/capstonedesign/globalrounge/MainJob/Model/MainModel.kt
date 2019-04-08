package capstonedesign.globalrounge.MainJob.Model

import capstonedesign.globalrounge.MainJob.MainMVP
import capstonedesign.globalrounge.MainJob.Presenter.MainPresenter

class MainModel constructor(presenter: MainPresenter): MainMVP.Model{
    val presenter = presenter

    override fun requestPermission(id: String, pw: String) {
        val permission = SejongPermission(object :
            SejongPermission.RejectionCallback {
            override fun approval(id: String) {
                presenter.approvalPermission(id)
            }

            override fun reject(text: String) {
                presenter.rejectPermission(text)
            }


        })
        permission.requestUserInformation(id,pw)
    }

}