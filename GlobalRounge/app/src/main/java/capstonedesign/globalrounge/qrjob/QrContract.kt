package capstonedesign.globalrounge.qrjob

import android.graphics.Bitmap

interface QrContract {
    interface View {
        fun alertToast(text: String)
        fun makeQrCode(bitmap: Bitmap)
    }

    interface Presenter {

        fun stateRequest()
        fun dispose()
        fun stateDelete()
        fun logout()
    }
}