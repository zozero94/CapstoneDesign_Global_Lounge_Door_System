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


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

    private byte[] bPublicKey;
    private byte[] bPrivateKey;
    private byte[] bKey;

    public RSAencoded() {
        try {
            publicKey = null;
            privateKey = null;
            secureRandom = new SecureRandom();
            keyFactory = KeyFactory.getInstance("RSA");
            cipher = Cipher.getInstance("RSA");
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
        bPublicKey = publicKey.getEncoded();
        bPrivateKey = privateKey.getEncoded();
    }// Client(어플리케이션)에서 생성

    public String getEncodedString(Student info, String publicKey){
        String encodedInfo = null;
        PublicKey Key;
        try{
            bKey = Base64.decodeBase64(publicKey.getBytes());
            pKeySpec = new PKCS8EncodedKeySpec(bKey);
            Key = keyFactory.generatePublic(pKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, Key);

            info.toString();







        } finally {
            return encodedInfo;
        }
    }

    public byte[] getbPublicKey() { return bPublicKey; }
    public void setbPublicKey(byte[] bPublicKey) { this.bPublicKey = bPublicKey; }
    public byte[] getbPrivateKey() { return bPrivateKey; }
    public void setbPrivateKey(byte[] bPrivateKey) {    this.bPrivateKey = bPrivateKey; }

}
