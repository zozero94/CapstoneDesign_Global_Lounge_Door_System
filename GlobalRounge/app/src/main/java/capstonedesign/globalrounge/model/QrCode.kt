package capstonedesign.globalrounge.model

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

object QrCode {
    private val writer = MultiFormatWriter()
    private val encoder = BarcodeEncoder()

    /**
     * QR코드를 생성한다
     */
    fun makeQrCode(data: String): Bitmap =
        writer.encode(data, BarcodeFormat.QR_CODE, 500, 500).let { encoder.createBitmap(it) }

}