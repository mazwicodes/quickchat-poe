package com.quickchat.quickchat.poe;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * One chat message: its ID, number, recipient, text and hash, plus the checks
 * and JSON storage that go with it.
 */
public class Message {

    private static final String STORE_FILE = "messages.json";

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Shared by every message: the running total and the details of sent messages
    private static int totalMessages = 0;
    private static ArrayList<String> sentMessages = new ArrayList<>();

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
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

        // First word that still has letters or digits once punctuation is removed
        String firstWord = "";
        for (int i = 0; i < words.length && firstWord.isEmpty(); i++) {
            firstWord = words[i].replaceAll("[^a-zA-Z0-9]", "");
        }

        // Last word, working backwards, skipping anything that is only punctuation
        String lastWord = "";
        for (int i = words.length - 1; i >= 0 && lastWord.isEmpty(); i--) {
            lastWord = words[i].replaceAll("[^a-zA-Z0-9]", "");
        }

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
        String details = "Message ID: " + messageID + "\nMessage Hash: " + createMessageHash()
                + "\nRecipient: " + recipient + "\nMessage: " + messageText;
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

    // Gson usage based on the Gson User Guide, Google (n.d.).
    // Available at: https://github.com/google/gson/blob/main/UserGuide.md
    public static ArrayList<Message> readStoredMessages() {
        ArrayList<Message> loaded = new ArrayList<>();
        File file = new File(STORE_FILE);
        if (!file.exists()) {
            return loaded;
        }
        try (FileReader reader = new FileReader(file)) {
            Message[] fromFile = new Gson().fromJson(reader, Message[].class);
            if (fromFile != null) {
                loaded.addAll(Arrays.asList(fromFile));
            }
        } catch (IOException | JsonParseException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }
        return loaded;
    }

    // Gson usage based on the Gson User Guide, Google (n.d.).
    // Available at: https://github.com/google/gson/blob/main/UserGuide.md
    public static void writeStoredMessages(ArrayList<Message> list) {
        try (FileWriter writer = new FileWriter(STORE_FILE)) {
            new Gson().toJson(list, writer);
        } catch (IOException e) {
            System.out.println("Could not store message: " + e.getMessage());
        }
    }

    public void storeMessage() {
        createMessageHash();
        ArrayList<Message> all = readStoredMessages();
        all.add(this);
        writeStoredMessages(all);
    }
}
