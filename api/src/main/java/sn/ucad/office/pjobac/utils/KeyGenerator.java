package sn.ucad.office.pjobac.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        // Générer une clé secrète aléatoire de 16 octets (128 bits)
        byte[] secretKey = generateRandomKey(16);

        // Convertir la clé secrète en chaîne Base64 pour une représentation plus conviviale
        String base64Key = Base64.getEncoder().encodeToString(secretKey);

        // Afficher la clé secrète générée
        System.out.println("Clé secrète générée : " + base64Key);
    }

    // Fonction pour générer une clé secrète aléatoire
    public static byte[] generateRandomKey(int keyLength) {
        byte[] key = new byte[keyLength];
        new SecureRandom().nextBytes(key);
        return key;
    }
}
