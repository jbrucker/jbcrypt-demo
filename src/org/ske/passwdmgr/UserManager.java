package org.ske.passwdmgr;

import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.HashMap;

/**
 * Save and return user account information.
 * This class adds users and returns users by username.
 * While running, user data is stored in a map.
 * Its also written to a file (USERFILE) for persistence.
 * The file contains one line for each user:
 * 
 * username:encrypted_password:real_name
 * 
 * In a real application, you'd probably use a database
 * instead of a file.
 * 
 * @author jim
 */
public class UserManager {
	// Name of file containing user info
	private static final String USERFILE = "userinfo.txt";
	// Map of known users
	private Map<String,UserInfo> users = new HashMap<String,UserInfo>();
	// Singleton instance of UserManager
	private static UserManager instance = new UserManager() ;
	// Logging
	private static Logger logger;
	
	/** Constructor is private to prevent creating objects. */
	private UserManager() {
		logger = Logger.getLogger(this.getClass().getSimpleName());
		loadUsers();
	}
	
	/** Load user data from external resource (file or database). */
	private void loadUsers() {
		final String FIELD_SEP = ":";
		// try to open the file as a classpath resource
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream in = loader.getResourceAsStream(USERFILE);
		if (in == null) try {
			// then try to open file as file system object
			in = new FileInputStream(USERFILE);
		} catch (FileNotFoundException ex) {
			// give up
		}
		if (in == null) {
			logger.warning("No users file "+USERFILE);
			return;
		}
		// read all lines from userinfo file
		// format: username:password:real_name
		Scanner reader = new Scanner(in);
		while(reader.hasNextLine()) {
			String line = reader.nextLine().trim();
			String[] fields = line.split(FIELD_SEP);
			if (fields.length != 3) continue; // should log the error
			// constructor:     UserInfo(username, realname, password)
			UserInfo info = new UserInfo(fields[0], fields[2], fields[1]);
			users.put(info.username, info);
		}
		reader.close();
	}

	/** Get a singleton instance of UserManager. */
	public static UserManager getInstance() {
		return instance;
	}

	public boolean addUser(String username, String realname, String password) {
		// validate input data
		if ( !validate(username) || !validate(realname) ) return false;
		
		if (users.containsKey(username)) return false; // username exists
		
		// encrypt the password
		String encrypted = BCrypt.hashpw(password, BCrypt.gensalt());
		
		// save user info to database
		UserInfo info = new UserInfo(username,realname,encrypted);
		saveUser(info);
		return true;
	}
	
	/** Add new user info object to hashmap and database. */
	private void saveUser(UserInfo info) {
		String name = info.username;
		boolean newuser = users.containsKey(name);
		users.put(name, info);
		// update users file
		OutputStream out = null;
		if (newuser) {
			// Just append new user
			try {
				out = new FileOutputStream(USERFILE, true /*append*/);
				PrintStream p = new PrintStream(out);
				p.printf("%s:%s:%s\n", info.username, info.password, info.realname);
				p.close();
			} catch(IOException ioe) {
				logger.warning("Exception while writing user info: " + ioe.getMessage());
			}
		}
		else {
			// rewrite entire file
			try {
				out = new FileOutputStream(USERFILE, false /*overwrite*/);
				PrintStream p = new PrintStream(out);
				for(UserInfo ui: users.values())
					p.printf("%s:%s:%s\n", ui.username, ui.password, ui.realname);
				 p.close();	
			} catch(IOException ioe) {
				logger.warning("Exception while writing user info: " + ioe.getMessage());
			}
		}
	}
	
	/**
	 * Validate input values for username and real name.
	 * This is to prevent hacking.
	 * @param arg string to validate
	 * @return true if arg is OK, false if has disallowed characters.
	 */
	private boolean validate(String arg) {
		// special chars allowed in names
		final String SPECIAL_CHARS = "@#$'?()~-_";
		// don't allow field separator!
		if (arg.contains(":")) return false;
		if (arg.length()>255) return false;
		char[] chars = arg.toCharArray();
		for(char c: chars ) {
			if (Character.isLetterOrDigit(c)) continue;
			if (Character.isWhitespace(c)) continue;
			if (SPECIAL_CHARS.indexOf(c) >= 0) continue;
			return false;
		}
		return true;
	}
	
	/**
	 * Get the user information for one user, by login name.
	 * @param username user's login name
	 * @return UserInfo or null if username not found
	 */
	public UserInfo getUser(String username) {
		return users.get(username);
	}
}
