## Sample application for JBCrypt

This project contains two examples of how to use JBCrypt in Java.

* **demo/ConsolePasswordDemo** - very simple. Enter a password at the console.  The app encrypts and remembers the password.  Then it prompts you to re-enter any passwords you want.  This shows that the app only needs the encrypted password to verify a user's password.
```shell
Enter a new password: secret

Now enter strings to compare with your original password.
Enter a blank line to quit
Password to try: fuzzball
No match.
Password to try: SECRET
No match.
Password to try: Secret
No match.
Password to try: secret
Matches!
``` 

* **org/ske/passwdmgr/LoginApp.java** (and other files) - GUI demo displays a Login form and AddUser form.  You can add "users" and the data is saved in a file named `userinfo.txt` in the project root directory.  

There are 2 sample users in the data file:

| Username | Password | Real name |
|----------|----------|:----------|
| harry    | hackme   |Harry Hacker |
| sally    | hackme2  |Sally Cracker |

By viewing the file, you can see that the plain text passwords aren't stored, but you can still "login" as these users.

## Installation and Run

1. Clone the repository.
2. Create a Java project in your favorite IDE.  Check that `lib/jbcrypt.jar` is added to the project buildpath. (Eclipse and Netbeans do this by default when you create a project from existing code.)
3. Run demo/ConsolePasswordDemo or org/ske/passwdmgr/LoginApp.

Running a JAR file:

The dist/LoginApp.jar runs the LoginApp.  It will look for the `userinfo.txt` file (containing usernames and passwords) in the current directory.  

To run:
1. copy `userinfo.txt` to your current directory (this file is in the project top-level directory).  If the file doesn't exist you can still run the app, but the demo users (hacker and sally) won't exist.
2. run the Jar file:
```shell
cmd> java -jar LoginApp.jar
```

## How JBCrypt Works

JBCrypt is a Java implementation of the BCrypt libary.  It creates a salted hash of a string (password or anything).  For example, the encrypted form of "secret" is:
```
$2a$10$r8CPhDjVZkeh0j/MeamolOjwr2VrAZVz45pe2ZbwfvsBcrPIDbv4a
```
The first part (`$2a$10$`) is the **salt**.  Every time you want to test if a new string is same as original string, you call:
```java
String testpassword = "some_password_to_test";
// the encrypted password
String encrypted = "$2a$10$r8CPhDjVZkeh0j/MeamolOjwr2VrAZVz45pe2ZbwfvsBcrPIDbv4a";

if ( JBCrypt.checkpw( testpassword, encrypted ) ) 
	    System.out.println("matches");
else
		System.out.println("no match");
```

BCrypt is a one-way hash. There's no known way to descrypt the result and recover the original password (except maybe, the NSA).  The "salt" is an extra input to the algorithm to prevent a pre-computed dictionary attack on encrypted passwords.

### Benefit For Your Application

Instead of storing plain text passwords for your users, you store only the encrypted (hashed) values.  If your accounts database is hacked, its computationally hard for a hacker to recover user's passwords.

### References

[http://www.mindrot.org/projects/jBCrypt/](JBCrypt home).

[https://github.com/jeremyh/jBCrypt](https://github.com/jeremyh/jBCrypt) - source code on Github.
