/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchat.quickchat.poe;

/**
 *
 * @author mazwi
 */
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        boolean hasMinLength = password.length() >= 8;

        Pattern capitalPattern = Pattern.compile("[A-Z]");
        Matcher capitalMatcher = capitalPattern.matcher(password);
        boolean hasCapital = capitalMatcher.find();

        Pattern numberPattern = Pattern.compile("[0-9]");
        Matcher numberMatcher = numberPattern.matcher(password);
        boolean hasNumber = numberMatcher.find();

        Pattern specialPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher specialMatcher = specialPattern.matcher(password);
        boolean hasSpecial = specialMatcher.find();
        return hasMinLength && hasCapital && hasNumber && hasSpecial;
    }
    // Regex pattern based on Java Pattern/Matcher documentation
    // Oracle. (2024). Class Pattern. Available at: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
     public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        Pattern phonePattern = Pattern.compile("^\\+27[0-9]{9}$");
        Matcher phoneMatcher = phonePattern.matcher(cellPhoneNumber);
        return phoneMatcher.matches();
    }
     
     public String registerUser(String username, String password, String cellPhoneNumber, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        } else if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        } else if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        } else {
            this.username = username;
            this.password = password;
            this.cellPhoneNumber = cellPhoneNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            return "Username successfully captured. Password successfully captured. Cell phone number successfully added.";
     
    }
        
  }
     
     public boolean loginUser(String username, String password) {
         return this.username.equals(username) && this.password.equals(password);
     }
     
     public String returnLoginStatus(boolean loginSuccess) {
         if (loginSuccess) {
             return "Welcome " + this.firstName +", " + this.lastName + " it is great to see you again.";
         } else {
             return "Username or password incorrect, please try again.";
         }
     }
     
}


