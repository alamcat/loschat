package bean;

import utils.Encryption;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class generates an account when a user
 * register on the application.
 * @author WenBin Hu
 * @version 2020-03-06
 */
public class RegisterPackage implements Serializable {

	private static final long serialVersionUID = 8094494300385452676L;
	private Encryption encryption;
	private String account;
	private String password;
	private String gender;
	private int age;

	/**
	 * Constructor without parameter.
	 */
	public RegisterPackage() {
		encryption = new Encryption();
	}

	/**
	 * Constructor with all parameters.
	 */
	public RegisterPackage(String account, String password, String gender, int age) {
		encryption = new Encryption();
		this.account = account;
		this.password = encryption.Encryption(password);
		this.gender = gender;
		this.age = age;
	}

	/**
	 * Getter for the user's gender.
	 * @return a user's gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Setter for the user's gender.
	 * @param gender a user's gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Getter for the user's age.
	 * @return a user's age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Setter for the user's age
	 * @param age a user's age
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Getter for the user's account.
	 * @return a user's account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Setter for the user's account.
	 * @param account a user's account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Getter for the user's password.
	 * @return a user's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for the user's password.
	 * @param password a user's password
	 */
	public void setPassword(String password) {
		this.password = encryption.Encryption(password);
	}

}
