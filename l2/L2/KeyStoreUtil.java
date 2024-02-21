import java.io.FileInputStream;
import java.security.KeyStore;

public class KeyStoreUtil {

    public static KeyStore getKeyStore(String keystorePath, char[] keystorePassword) {
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(fis, keystorePassword);
            return keystore;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
