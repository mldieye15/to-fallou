package sn.ucad.office.pjobac.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "HBAe81ObPhzJ7o3iStBJdw=="; // La clé de chiffrement (doit être de longueur appropriée pour l'algorithme choisi)

    public static String encrypt(String value) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    public static String decrypt(String encryptedValue) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

//    public static void main(String[] args) throws Exception {
//        String originalText = "Hello, world!";
//        String encryptedText = encrypt(originalText);
//        String decryptedText = decrypt(encryptedText);
//
//        System.out.println("Original: " + originalText);
//        System.out.println("Encrypted: " + encryptedText);
//        System.out.println("Decrypted: " + decryptedText);
//    }
}
