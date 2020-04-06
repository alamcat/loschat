package bean;

import utils.Encryption;

import java.io.Serializable;

/**
 * This class is used when the client tries to authenticate.
 * @author Peng Jiang, WenBin Hu
 * @version 2020-03-06
 */
public class AuthenticationPackage implements Serializable {

    private static final long serialVersionUID = 1767710342274177563L;
    private Encryption encryption;
    private String account;
    private String password;

    /**
     * The constructor of the authenticationPackage.
     * @param account the account of the user
     * @param password the raw password of the user
     */
    public AuthenticationPackage(String account, String password) {
        encryption = new Encryption();
        this.account = account;
        this.password = encryption.Encryption(password);
    }

    /**
     * Getter for the account.
     * @return the account of the user
     */
    public String getAccount() {
        return account;
    }

    /**
     * Getter for the password(MD5).
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

}
