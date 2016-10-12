package by.bsu.csds.config;

public class Config {
    public static final int PORT = 12345;
    public static final int RSA_KEY_LENGTH = 2048;
    public static final String RSA_CLIENT_KEY_FILE = "keys.bin";
    public static final String RSA_KEYGEN_ALGORITHM = "RSA";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String SERPENT_ALGORITHM = "SERPENT/CBC/PKCS5Padding";
    public static final String SERPENT_KEYGEN_ALGORITHM = "Serpent";
    public static final String FILES_FOLDER = "files/";
    public static final byte[] INIT_VECTOR = "IVStringIVString".getBytes();
    public static final int ERROR_FILE_NOT_FOUND = -1;
    public static final String EXIT_COMMAND = "exit";
}
