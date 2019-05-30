package control.encryption;

import com.google.gson.Gson;
import model.dto.Student;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaManager {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Cipher cipher;
    private SecureRandom secureRandom;
    private KeyFactory keyFactory;
    private KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;
    private byte[] bKey;
    private String strPublicKey;
    private Gson gson;
    public RsaManager() {
        try {
            publicKey = null;
            privateKey = null;
            secureRandom = new SecureRandom();
            keyFactory = KeyFactory.getInstance("RSA");
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            gson = new Gson();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
    public void newKey() {
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512, secureRandom);

            keyPair = keyPairGenerator.genKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } // key 생성
        byte[] b = publicKey.getEncoded();
        strPublicKey = Base64.encodeBase64String(b);
    }// SocketThreadAP(어플리케이션)에서 생성
    public String getEncodedString(Student info) {
        String encodedInfo = null;
        PublicKey key;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            encodedInfo = gson.toJson(info);
            byte[] strToByte = encodedInfo.getBytes();
            int size = (strToByte.length / 52);
            byte[] encodedByte = new byte[(size + 1) << 6];
            byte[] temp = new byte[52];
            for (int i = 0; i < size; i++) {
                System.arraycopy(strToByte, (i * 52), temp, 0, 52);
                System.arraycopy(cipher.doFinal(temp), 0, encodedByte, (i << 6), 64);
            }
            if (strToByte.length % 52 != 0) {
                temp = new byte[(strToByte.length - (size * 52))];
                System.arraycopy(strToByte, (size * 52), temp, 0, strToByte.length - (size * 52));
                System.arraycopy(cipher.doFinal(temp), 0, encodedByte, (size << 6), 64);
            }
            encodedInfo = Base64.encodeBase64String(encodedByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedInfo;

    }
    public Student getDecodedString(String data) {
        Student info = null;
        StringBuilder str = new StringBuilder();
        try {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decoded = Base64.decodeBase64(data.getBytes());
            byte[] temp = new byte[64];
            for (int i = 0; i < (decoded.length / 64); i++) {
                System.arraycopy(decoded, (i << 6), temp, 0, 64);
                String s = new String(cipher.doFinal(temp), StandardCharsets.UTF_8);
                str.append(s);
            }
            info = gson.fromJson(str.toString(), Student.class);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return info;
    }
    public void setPrivateKey(String key) {
        try {
            byte[] bKey = Base64.decodeBase64(key.getBytes());
            PKCS8EncodedKeySpec pKeySpec = new PKCS8EncodedKeySpec(bKey);
            this.privateKey = keyFactory.generatePrivate(pKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }
    public String getStrPublicKey() {
        return strPublicKey;
    }
    public void setStringPublicKey(String modulus, String exponent) {
        BigInteger m = new BigInteger(modulus, 16);
        BigInteger p = new BigInteger(exponent, 16);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(m, p);
            this.publicKey = keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPublicKey(String publicKey) {
        try {
            bKey = Base64.decodeBase64(publicKey.getBytes());
            X509EncodedKeySpec pKeySpec = new X509EncodedKeySpec(bKey);
            this.publicKey = keyFactory.generatePublic(pKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
