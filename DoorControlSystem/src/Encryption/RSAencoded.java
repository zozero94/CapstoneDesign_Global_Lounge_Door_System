package Encryption;
import Model.Student;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;

public class RSAencoded {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Cipher cipher;
    private SecureRandom secureRandom;
    private KeyFactory keyFactory;
    private KeyPairGenerator keyPairGenerator;
    private PKCS8EncodedKeySpec pKeySpec;
    private KeyPair keyPair;

    private byte[] bKey;
    private String strPublicKey;

    private Gson gson;

    public RSAencoded() {
        try {
            publicKey = null;
            privateKey = null;
            secureRandom = new SecureRandom();
            keyFactory = KeyFactory.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
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
    }// SocketThread(어플리케이션)에서 생성

    public String getEncodedString(Student info, String publicKey){
        String encodedInfo = null;
        PublicKey key;

        try{
            bKey = Base64.decodeBase64(publicKey.getBytes());
            X509EncodedKeySpec pKeySpec = new X509EncodedKeySpec(bKey);
            key = keyFactory.generatePublic(pKeySpec);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            encodedInfo = gson.toJson(info);

            System.out.println(encodedInfo);

            byte[] strToByte = encodedInfo.getBytes();
            int size = (strToByte.length/52);
            byte[] encodedByte = new byte[(size+1)<<6];
            byte[] temp = new byte[52];
            for(int i = 0; i < size; i++){
                System.arraycopy(strToByte, (i*52), temp,0,52);
                System.arraycopy(cipher.doFinal(temp), 0, encodedByte,  (i<<6),64);
            }
            if(strToByte.length % 52 != 0) {
                temp = new byte[(strToByte.length - (size * 52))];
                System.arraycopy(strToByte, (size * 52), temp, 0, strToByte.length - (size * 52));
                System.arraycopy(cipher.doFinal(temp), 0, encodedByte, (size<< 6), 64);
            }
            encodedInfo = Base64.encodeBase64String(encodedByte);
        } finally {
            return encodedInfo;
        }
    }

    public Student getDecodedString(String data){
        Student info = null;
        StringBuilder str= new StringBuilder();
        try{
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decoded = Base64.decodeBase64(data.getBytes());
            byte[] temp = new byte[64];
            System.out.println("decode");
            for(int i = 0; i < (decoded.length/64); i++){
                System.arraycopy(decoded, (i<<6), temp,0,64);
                String s = new String(cipher.doFinal(temp), "UTF-8");
                System.out.println(s);
                str.append(s);
            }
            System.out.println(str.toString());
            info = gson.fromJson(str.toString(), Student.class);
            System.out.println(info.toString());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e1) {
            e1.printStackTrace();
        }finally {
            return info;
        }
    }

    public String getStrPublicKey() {
        return strPublicKey;
    }

}
