import com.sun.istack.internal.Nullable;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.println;

public class encrypt2 {
    // Generate key pair for 1024-bit RSA encryption and decryption


    public static void main(String[] args) {
        @Nullable
        Key publicKey = null;
        @Nullable
        Key privateKey = null;

        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();

            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();

            println("공개키 :" + publicKey);
            println("암호키 :" + privateKey);

            byte[] encodedBytes = RSA_Encode("앙기모찌", publicKey);

            String result = Base64.getEncoder().encodeToString(encodedBytes);
            println("인코딩된 데이터 : " + result);

            result = RSA_Decode(encodedBytes,privateKey);
            println("디코딩된 데이터 : " + result);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] RSA_Encode(String theTestText, Key publicKey) {
        // Encode the original data with RSA private key
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            encodedBytes = c.doFinal(theTestText.getBytes());
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return encodedBytes;


    }

    public static String RSA_Decode(byte[] encodedBytes, Key privateKey) {
        // Decode the encoded data with RSA public key
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            decodedBytes = c.doFinal(encodedBytes);

        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

}


