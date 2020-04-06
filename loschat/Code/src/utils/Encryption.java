package utils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption implements Serializable {
    /**
     * A method uses the MD5 encrypt users' password.
     * @param raw the original password
     * @return the encrypted password
     */
    public String Encryption(String raw) {
        StringBuffer encryptedPwd = new StringBuffer("");
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(raw.getBytes());
            byte[] hash = md5.digest();
            int currentByte;

            for (int i = 0; i < hash.length; i++) {
                currentByte = hash[i] & 0xff;
                String part = Integer.toHexString(currentByte);
                if (part.length() == 1) {
                    encryptedPwd.append(0);
                }
                encryptedPwd.append(part);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error! " + e.getMessage());
        }
        return encryptedPwd.toString();
    }
}
