package org.ske.passwdmgr;

/**
 * Contains user info.
 * Out of shear laziness, the fields are package scope.
 * @author jim
 */
public class UserInfo {
	final String username;
	final String realname;
	String password;
	
	/**
	 * Initialize a new UserInfo object.
	 * @param username login name
	 * @param realname real name
	 * @param password encrypted password
	 */
	public UserInfo(String username, String realname, String password) {
		this.username = username;
		this.realname = realname;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return realname;
	}	
}
