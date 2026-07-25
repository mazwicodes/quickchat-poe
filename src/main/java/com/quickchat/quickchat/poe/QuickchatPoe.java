/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.quickchat.quickchat.poe;

import java.util.Scanner;

/**
 *
 * @author mazwi
 */
public class QuickchatPoe {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your South African cell phone number: ");
        String cellPhoneNumber = scanner.nextLine();

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        String registrationResult = login.registerUser(username, password, cellPhoneNumber, firstName, lastName);
        System.out.println(registrationResult);

        System.out.print("\nEnter your username to login: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password to login: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
        String loginStatusMessage = login.returnLoginStatus(loginSuccess);
        System.out.println(loginStatusMessage);
    }
}
