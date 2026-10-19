/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchat.quickchat.poe;

import java.util.ArrayList;

import java.util.Random;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author mazwi
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessages = 0;
    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";

        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

    }

    public String createMessageHash() {
        String[] words = messageText.trim().split(" ");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
        messageHash = hash.toUpperCase();
        return messageHash;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
            default:
                return "Invalid choice. Please enter 1, 2 or 3.";
        }
    }

    public static String generateMessageID() {
        Random random = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }
        return id;
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public String recordSent() {
        totalMessages++;
        String details = "Message ID: " + messageID + "\nMessage Hash: " + createMessageHash() + "\nRecipient: " + recipient + "\nMessage: " + messageText;
        sentMessages.add(details);
        return details;
    }

    public static String printMessages() {
        String all = "";
        for (String details : sentMessages) {
            all = all + details + "\n\n";
        }
        return all;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Gson usage based on the Gson user guide, Google (n.d.).
    // Available at : https://github.com/google/gson/blob/main/UserGuide.md
    public void storeMessage() {
        createMessageHash();
        storedMessages.add(this);
        try (FileWriter writer = new FileWriter("messages.json")) {
            new Gson().toJson(storedMessages, writer);
        } catch (IOException e) {
            System.out.println("Could not store message: " + e.getMessage());
        }
    }
}
