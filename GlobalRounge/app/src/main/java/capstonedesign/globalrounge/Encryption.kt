package Encryption

import android.util.Base64
import capstonedesign.globalrounge.Student
import com.google.gson.Gson
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


object Encryption {
    private var keyPair: KeyPair? = null

    private var bKey: ByteArray? = null
    var strPublicKey: String? = null
    var strPrivateKey: String? = null
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    fun newKey() {

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(512, SecureRandom())
        keyPair = keyPairGenerator.genKeyPair()

        strPublicKey = Base64.encodeToString(keyPair!!.public.encoded, 0)
        strPrivateKey = Base64.encodeToString(keyPair!!.private.encoded, 0)
    }

    fun getEncodedString(info: Student, publicKey: String): String? {
        var encodedInfo: String? = null
        val key: PublicKey

        bKey = Base64.decode(publicKey.toByteArray(), 0)
        val pKeySpec = X509EncodedKeySpec(bKey)
        key = KeyFactory.getInstance("RSA").generatePublic(pKeySpec)

        cipher.init(Cipher.ENCRYPT_MODE, key)
        encodedInfo = Gson().toJson(info)

        val strToByte = encodedInfo!!.toByteArray()
        val size = strToByte.size / 52 // size % 52 == 0 size <<6  else size +1 <<6
        val encodedByte = when (size % 52) {
            0 -> ByteArray(size shl 6)
            else -> ByteArray(size + 1 shl 6)
        }

        var temp = ByteArray(52)
        for (i in 0 until size) {
            System.arraycopy(strToByte, i * 52, temp, 0, 52)
            System.arraycopy(cipher.doFinal(temp), 0, encodedByte, i shl 6, 64)
        }
        if (strToByte.size % 52 != 0) {
            temp = ByteArray(strToByte.size - size * 52)
            System.arraycopy(strToByte, size * 52, temp, 0, strToByte.size - size * 52)
            System.arraycopy(cipher.doFinal(temp), 0, encodedByte, size shl 6, 64)
        }
        encodedInfo = Base64.encodeToString(encodedByte, 0)

        return encodedInfo

    }

    fun getDecodedString(data: String): Student? {
        val str = StringBuilder()

        cipher.init(Cipher.DECRYPT_MODE, keyPair!!.private)
        val decoded = Base64.decode(data.toByteArray(), Base64.DEFAULT)
        val temp = ByteArray(64)

        for (i in 0 until decoded.size / 64) {
            System.arraycopy(decoded, i shl 6, temp, 0, 64)
            val appendString = String(cipher.doFinal(temp), StandardCharsets.UTF_8)
            str.append(appendString)
        }

        return Gson().fromJson<Any>(str.toString(), Student::class.java) as Student
    }

}
