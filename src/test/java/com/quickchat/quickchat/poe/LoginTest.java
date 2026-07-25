/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchat.quickchat.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 *
 * @author mazwi
 */
public class LoginTest {

    @Test
    public void testCheckUserNameValid() {
        Login login = new Login();
        boolean result = login.checkUserName("kyl_1");
        assertTrue(result);
    }

    @Test
    public void testCheckUserNameInvalid() {
        Login login = new Login();
        boolean result = login.checkUserName("kyle!!!!!!!");
        assertFalse(result);
    }

    @Test
    public void testCheckPasswordComplexityValid() {
        Login login = new Login();
        boolean result = login.checkPasswordComplexity("Ch&&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        Login login = new Login();
        boolean result = login.checkPasswordComplexity("password");
        assertFalse(result);
    }

    @Test
    public void testCheckCellPhoneNumberValid() {
        Login login = new Login();
        boolean result = login.checkCellPhoneNumber("+27838968976");
        assertTrue(result);
    }

    @Test
    public void testCheckCellPhoneNumberInvalid() {
        Login login = new Login();
        boolean result = login.checkCellPhoneNumber("08966553");
        assertFalse(result);
    }

    @Test
    public void testLoginUserSuccessful() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean result = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testLoginUserFailed() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean result = login.loginUser("kyl_1", "WrongPassword1!");
        assertFalse(result);
    }

    @Test
    public void testRegisterUserSuccessMessage() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username successfully captured. Password successfully captured. Cell phone number successfully added.", result);
    }

    @Test
    public void testReturnLoginStatusWelcomeMessage() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean loginSuccess = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        String result = login.returnLoginStatus(loginSuccess);
        assertEquals("Welcome Kyle, Smith it is great to see you again.", result);
    }

}
