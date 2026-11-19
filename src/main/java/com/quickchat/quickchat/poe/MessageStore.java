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

}
