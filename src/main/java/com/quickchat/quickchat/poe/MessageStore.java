/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchat.quickchat.poe;

/**
 *
 * @author mazwi
 */
public class MessageStore {

    private static final int MAX_MESSAGES = 100;

    private String[] messageIDs = new String[MAX_MESSAGES];
    private String[] messageHashes = new String[MAX_MESSAGES];
    private String[] recipients = new String[MAX_MESSAGES];
    private String[] sentMessages = new String[MAX_MESSAGES];
    private String[] disregardedMessages = new String[MAX_MESSAGES];
    private String[] storedMessages = new String[MAX_MESSAGES];
    private int count = 0;

    public boolean addMessage(String id, String hash, String recipient, String text, String flag) {
        if (count >= MAX_MESSAGES) {
            return false;
        }

        String type = flag.trim().toLowerCase();
        if (!type.equals("sent") && !type.equals("stored") && !type.equals("disregard")) {
            return false;
        }

        messageIDs[count] = id;
        messageHashes[count] = hash;
        recipients[count] = recipient;

        switch (type) {
            case "sent":
                sentMessages[count] = text;
                break;
            case "stored":
                storedMessages[count] = text;
                break;
            default:
                disregardedMessages[count] = text;
        }

        count++;
        return true;
    }

    public String displayStoredSenderAndRecipient(String sender) {
        String result = "";
        for (int i = 0; i < count; i++) {
            if (storedMessages[i] != null) {
                result = result + "Sender: " + sender + " | Recipient: " + recipients[i] + "\n";
            }
        }
        return result;
    }

    public String displayLongestStoredMessage() {
        String longest = "";
        for (int i = 0; i < count; i++) {
            if (storedMessages[i] != null && storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        if (longest.isEmpty()) {
            return "No stored messages.";
        }
        return longest;
    }

    private String textAt(int i) {
        if (sentMessages[i] != null) {
            return sentMessages[i];
        }
        if (storedMessages[i] != null) {
            return storedMessages[i];
        }
        return disregardedMessages[i];
    }

    public String searchByMessageID(String id) {
        for (int i = 0; i < count; i++) {
            if (messageIDs[i].equals(id)) {
                return "Recipient: " + recipients[i] + " | Message: " + textAt(i);
            }
        }
        return "Message ID not found.";
    }

    public String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (recipients[i].equals(recipient) && disregardedMessages[i] == null) {
                sb.append(textAt(i)).append("\n");
            }
        }
        return sb.length() == 0 ? "No messages found for that recipient." : sb.toString().trim();
    }

    public String deleteByHash(String hash) {
        for (int i = 0; i < count; i++) {
            if (messageHashes[i].equalsIgnoreCase(hash)) {
                String text = textAt(i);
                for (int j = i; j < count - 1; j++) {
                    sentMessages[j] = sentMessages[j + 1];
                    storedMessages[j] = storedMessages[j + 1];
                    disregardedMessages[j] = disregardedMessages[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    recipients[j] = recipients[j + 1];
                }
                int last = count - 1;
                sentMessages[last] = null;
                storedMessages[last] = null;
                disregardedMessages[last] = null;
                messageIDs[last] = null;
                messageHashes[last] = null;
                recipients[last] = null;
                count--;
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // 2f. Report of all sent messages: hash, recipient, message
    public String displayReport() {
        StringBuilder sb = new StringBuilder("----- MESSAGE REPORT -----\n");
        for (int i = 0; i < count; i++) {
            if (sentMessages[i] != null) {
                sb.append("Message Hash: ").append(messageHashes[i])
                        .append(" | Recipient: ").append(recipients[i])
                        .append(" | Message: ").append(sentMessages[i]).append("\n");
            }
        }
        return sb.toString();
    }

    public String getHash(int i) {
        return messageHashes[i];
    }

}
