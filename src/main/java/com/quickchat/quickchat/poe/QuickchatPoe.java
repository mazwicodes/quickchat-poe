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

        String sender = registerAndLogin(scanner, login);
        if (sender == null) {
            return;
        }

        System.out.println("\nWelcome to QuickChat.");

        MessageStore store = new MessageStore();
        int loaded = store.loadStoredMessages();
        System.out.println(loaded + " stored message(s) loaded from messages.json.");

        int numMessages = askNumberOfMessages(scanner);
        runMainMenu(scanner, login, store, numMessages, sender);
    }

    // Registration and login from Part 1. Returns the user's full name, or null if login failed.
    private static String registerAndLogin(Scanner scanner, Login login) {
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

        System.out.println(login.registerUser(username, password, cellPhoneNumber, firstName, lastName));

        System.out.print("\nEnter your username to login: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter your password to login: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginSuccess));

        if (!loginSuccess) {
            return null;
        }
        return firstName + " " + lastName;
    }

    private static int askNumberOfMessages(Scanner scanner) {
        int numMessages = 0;
        while (numMessages < 1) {
            System.out.print("How many messages do you want to enter? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                numMessages = 0;
            }
            if (numMessages < 1) {
                System.out.println("Please enter a whole number of 1 or more.");
            }
        }
        return numMessages;
    }

    // The main menu. Runs until the user chooses Quit.
    private static void runMainMenu(Scanner scanner, Login login, MessageStore store, int numMessages, String sender) {
        boolean messagesEntered = false;
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesEntered) {
                        System.out.println("You have already entered your " + numMessages + " messages.");
                    } else {
                        enterMessages(scanner, login, store, numMessages);
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
                case "4":
                    storedMessagesMenu(scanner, store, sender);
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }
    }

    // Enter exactly numMessages messages, one at a time
    private static void enterMessages(Scanner scanner, Login login, MessageStore store, int numMessages) {
        for (int i = 0; i < numMessages; i++) {
            Message message = createValidMessage(scanner, login, i, numMessages);
            int choice = askSendChoice(scanner);
            System.out.println(message.SentMessage(choice));
            handleChoice(scanner, store, message, choice);
        }
    }

    // Keeps asking until the recipient and the message are both valid
    private static Message createValidMessage(Scanner scanner, Login login, int index, int total) {
        Message message;
        boolean valid;
        do {
            System.out.print("\nMessage " + (index + 1) + " of " + total + " - recipient cell number: ");
            String recipient = scanner.nextLine().trim();
            System.out.print("Enter your message: ");
            String text = scanner.nextLine();

            message = new Message(Message.generateMessageID(), index, recipient, text);
            System.out.println(message.checkRecipientCell());
            System.out.println(message.checkMessageLength());
            if (text.trim().isEmpty()) {
                System.out.println("A message cannot be empty.");
            }
            valid = login.checkCellPhoneNumber(recipient) && text.length() <= 250 && !text.trim().isEmpty();
        } while (!valid);
        return message;
    }

    private static int askSendChoice(Scanner scanner) {
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.println("\n1) Send Message\n2) Disregard Message\n3) Store Message to send later");
            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
        return choice;
    }

    // Does what the user chose and records the message in the arrays
    private static void handleChoice(Scanner scanner, MessageStore store, Message message, int choice) {
        if (choice == 1) {
            System.out.println(message.recordSent());
            saveToStore(store, message, "sent");
        } else if (choice == 2) {
            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Message deleted.");
            }
            saveToStore(store, message, "disregard");
        } else {
            message.storeMessage();
            saveToStore(store, message, "stored");
        }
    }

    private static void saveToStore(MessageStore store, Message message, String flag) {
        boolean added = store.addMessage(message.getMessageID(), message.createMessageHash(),
                message.getRecipient(), message.getMessageText(), flag);
        if (!added) {
            System.out.println("Message storage is full, so this message was not recorded in the arrays.");
        }
    }

    // Menu option 4
    private static void storedMessagesMenu(Scanner scanner, MessageStore store, String sender) {
        boolean back = false;
        while (!back) {
            System.out.println("\nStored Messages");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message by message ID");
            System.out.println("4) Search for messages by recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display a report of all messages");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.println(store.displayStoredSenderAndRecipient(sender));
                    break;
                case "2":
                    System.out.println(store.displayLongestStoredMessage());
                    break;
                case "3":
                    System.out.print("Enter the message ID: ");
                    System.out.println(store.searchByMessageID(scanner.nextLine().trim()));
                    break;
                case "4":
                    System.out.print("Enter the recipient's cell number: ");
                    System.out.println(store.searchByRecipient(scanner.nextLine().trim()));
                    break;
                case "5":
                    System.out.print("Enter the message hash: ");
                    System.out.println(store.deleteByHash(scanner.nextLine()));
                    store.saveStoredMessagesToFile();
                    break;
                case "6":
                    System.out.println(store.displayReport());
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 7.");
            }
        }
    }
}
