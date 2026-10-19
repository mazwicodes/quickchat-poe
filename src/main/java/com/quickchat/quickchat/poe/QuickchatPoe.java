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

        if (!loginSuccess) {
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        int numMessages = 0;
        while (numMessages < 1) {
            System.out.print("How many messages do you want to enter? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                numMessages = 0;
            }
            if (numMessages < 1) {
                System.out.println("Please enter a whole number of 1 or more.");
            }
            boolean messagesEntered = false;
            boolean running = true;

            while (running) {
                System.out.println("\n1)Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.print("Choose an option: ");
                String option = scanner.nextLine();

                switch (option) {
                    case "1":
                        if (messagesEntered) {
                            System.out.println("You have already entered your " + numMessages + " messages.");
                        } else {
                            enterMessages(scanner, login, numMessages);
                            messagesEntered = true;
                        }
                        break;
                    case "2":
                        System.out.println("Coming Soon.");
                        break;
                    case "3":
                        System.out.println("Total messages sent: " + Message.returnTotalMessages());
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose 1, 2 or 3.");
                }

            }
        }
    }

    private static void enterMessages(Scanner scanner, Login login, int numMessages) {
        for (int i = 0; i < numMessages; i++) {
            Message message;
            boolean valid;
            do {
                System.out.print("\nMessage " + (i + 1) + " of " + numMessages + " - recipient cell number: ");
                String recipient = scanner.nextLine();
                System.out.print("Enter your message: ");
                String text = scanner.nextLine();

                message = new Message(Message.generateMessageID(), i, recipient, text);
                System.out.println(message.checkRecipientCell());
                System.out.println(message.checkMessageLength());
                valid = login.checkCellPhoneNumber(recipient) && text.length() <= 250;
            } while (!valid);

            System.out.println("\n1) Send Message\n2) Disregard Message\n3) Store Message to send later");
            System.out.print("Choose an option: ");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            System.out.println(message.SentMessage(choice));
            if (choice == 1) {
                System.out.println(message.recordSent());
            } else if (choice == 2) {
                if (scanner.nextLine().equals("0")) {
                    System.out.println("Message deleted.");
                }
            } else if (choice == 3) {
                message.storeMessage();
            }
        }
    }
}
