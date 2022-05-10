package application.controllers.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    /*
    * Used to hash and verify hashes on password strings.
    * */

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(11));
    }

    public static boolean verify(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

}
