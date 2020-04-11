package demo;

import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Demonstrate how to encrypt a password (read from console)
 * using JBCrypt, and then check the password later using the
 * encrypted original password.
 * 
 * This program first asks the user for a password to encrypt.
 * It encrypts the password and discards the original (unencrypted).
 * Then is asks for some strings (test passwords) to compare
 * with the encrypted password.
 * 
 * @author jim
 */
public class ConsolePasswordDemo {
	public static final Scanner console = new Scanner(System.in);
	
	/** The user's password.  This value has already been encrypted with
	 *  BCrypt so that there is no plain-text password to store. 
	 */
	private String encryptedPassword;
	
	/**
	 * Validate a user input password by comparing the input
	 * with a previously encrypted and saved password.  
	 * We use BCrypt.checkpw() so that we can compare the 
	 * plain text (testpass) to an already-encrypted password.
	 * 
	 * @param testpasswd plain-text password to compare
	 * @return true if testpass matches the saved password
	 */
	public boolean verifyPassword(String testpasswd) {
		return BCrypt.checkpw(testpasswd, encryptedPassword);
	}
	
	/**
	 * Save a user's password.  This method takes the plaintext password
	 * (the parameter), encrypts it, and saves it as the encryptedPassword
	 * attribute.
	 * 
	 * @param passwd  user's plain text password to encrypt (hash) and store.
	 */
	public void savePassword(String passwd) {
		// encrypt (hash) the password.  This is what the app needs to remember.
		this.encryptedPassword = BCrypt.hashpw(passwd, BCrypt.gensalt());
		
		// for demonstration:
		System.out.println("Your encrypted password is: "+this.encryptedPassword );
	}

	/** 
     *  Interactive demo of encrypting a password entered at the console.
	 *  If you don't want the password to be visible, use java.io.Console.readPassword(),
	 *  but it doesn't work if running inside Eclipse or other IDE.
	 */
	public void consoleDialog() {
		System.out.print("Enter a new password: ");
		
		String password = console.nextLine().trim();
		savePassword( password );
		
		System.out.println("\nEnter strings to compare with your original password.");
		System.out.println("Enter a blank line to quit.\n");
		while(true) {
			System.out.print("Password to try: ");
			String candidate = console.nextLine().trim();
			if (candidate.isEmpty()) break;
			if ( verifyPassword(candidate) ) System.out.println("Matches!");
			else System.out.println("No match.");
		}
	}
	
	public static void main(String[] args) {
		ConsolePasswordDemo demo = new ConsolePasswordDemo();
		demo.consoleDialog();
		
		System.out.println("Wasn't that easy?  Bye.");
	}
}
