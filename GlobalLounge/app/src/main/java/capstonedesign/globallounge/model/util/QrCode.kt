package capstonedesign.globallounge.model

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder

object QrCode {
    private val writer = MultiFormatWriter()
    private val encoder = BarcodeEncoder()

    /**
     * QR코드를 생성한다
     *
     * @see capstonedesign.globallounge.qrjob.QrPresenter.subscribe
     * @param data : QR코드 생성시 이용할 String
     */
    fun makeQrCode(data: String): Bitmap =
        writer.encode(data, BarcodeFormat.QR_CODE, 500, 500).let { encoder.createBitmap(it) }

}