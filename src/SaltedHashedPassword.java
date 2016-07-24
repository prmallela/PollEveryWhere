
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SaltedHashedPassword {

    private static final int SALT_BYTES = 16;
    private static final String HASH_ALGO = "SHA-256";
    private static final char SEPARATOR = '$';

    private byte[] salt;
    private byte[] hash;

    private SaltedHashedPassword(byte[] salt, byte[] hash) {
        this.salt = salt;
        this.hash = hash;
    }

    public SaltedHashedPassword(String str) {
        int i = str.indexOf(SEPARATOR);
        this.salt = hexStringToByteArray(str.substring(0, i));
        this.hash = hexStringToByteArray(str.substring(i+1));
    }

    @Override
    public String toString() {
        return bytesAsHexString(salt) + SEPARATOR + bytesAsHexString(hash);
    }

    public boolean check(String loginPassword) throws NoSuchAlgorithmException {
        byte[] loginHash = hash(salt, loginPassword);
        System.out.println(bytesAsHexString(loginHash));
        return Arrays.equals(hash, loginHash);
    }

    static String bytesAsHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < bytes.length; i++)
        {
            builder.append(String.format("%02x", bytes[i]));
        }
        return builder.toString();
    }

    public static byte[] hash(byte[] salt, String plainPassword) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
        md.update(salt);
        md.update(plainPassword.getBytes());
        return md.digest();
    }

    public static SaltedHashedPassword generate(String plainPassword)
            throws NoSuchAlgorithmException {
        SecureRandom rng = new SecureRandom();
        byte[] salt = new byte[SALT_BYTES];
        rng.nextBytes(salt);
        return new SaltedHashedPassword(salt, hash(salt, plainPassword));
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Create new password: ");
        String origPassword = reader.readLine();
        SaltedHashedPassword origHashed = generate(origPassword);
        System.out.println(origHashed);
        //////
        SaltedHashedPassword restoredHash = new SaltedHashedPassword(origHashed.toString());
        boolean ok = false;
        while(!ok) {
            System.out.print("Login with password: ");
            String loginPassword = reader.readLine();
            ok = restoredHash.check(loginPassword);
            System.out.println(ok);
        }
    }

    // from http://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
