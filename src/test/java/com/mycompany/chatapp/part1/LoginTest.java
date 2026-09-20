package com.mycompany.chatapp.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    Login loginObj = new Login();

    @Test
    public void testUsernameFormat() {
        boolean validResult = loginObj.checkUserName("kyl_1");
        assertTrue(validResult);

        boolean invalidResult = loginObj.checkUserName("kyle!!!!!!");
        assertFalse(invalidResult);
    }

    @Test
    public void testPasswordSecurity() {
        boolean goodPass = loginObj.checkPasswordComplexity("Ch33se1*");
        assertTrue(goodPass);

        boolean badPass = loginObj.checkPasswordComplexity("password");
        assertFalse(badPass);
    }

    @Test
    public void testCellNumberValidation() {
        boolean correctCell = loginObj.checkCellPhoneNumber("0821234567");
        assertTrue(correctCell);

        boolean wrongCell = loginObj.checkCellPhoneNumber("12345");
        assertFalse(wrongCell);
    }

    @Test
    public void testUserAuthentication() {
        boolean passLogin = loginObj.loginUser("kyl_1", "Ch33se*", "kyl_1", "Ch33se*");
        assertTrue(passLogin);

        boolean failLogin = loginObj.loginUser("kyl_1", "Ch33se*", "wrongUser", "wrongPass");
        assertFalse(failLogin);
    }

    @Test
    public void testLoginStatusMessages() {
        String successMessage = loginObj.returnLoginStatus(true, "Adivhaho", "Davhana");
        assertEquals("Welcome Adivhaho Davhana, it is great to see you again.", successMessage);

        String errorMessage = loginObj.returnLoginStatus(false, "Adivhaho", "Davhana");
        assertEquals("Username or password incorrect, please try again.", errorMessage);
    }
}
