package capstonedesign.globallounge.qrjob

import android.graphics.Bitmap

interface QrContract {
    interface View {
        fun alertToast(text: String)
        fun makeQrCode(bitmap: Bitmap)
        fun drawUserImages(url: String)
    }

    interface Presenter {
        fun subscribe()
        fun dispose()
        fun stateRequest()
        fun stateDelete()
        fun logout()
        fun beaconConnect()
        fun beaconDisconnect()
    }
}