package com.quickchat.quickchat.poe;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Holds every message in parallel arrays: position i in each array describes
 * the same message. A message's text is in exactly one of the sent, stored or
 * disregarded arrays; the other two stay null at that position.
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

    /**
     * Adds a message to every array. The flag is "sent", "stored" or
     * "disregard".
     */
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

    /**
     * Reads messages.json back into the arrays as stored messages.
     */
    public int loadStoredMessages() {
        int loaded = 0;
        for (Message m : Message.readStoredMessages()) {
            if (addMessage(m.getMessageID(), m.getMessageHash(), m.getRecipient(), m.getMessageText(), "stored")) {
                loaded++;
            }
        }
        return loaded;
    }

    /**
     * Rewrites messages.json so it matches the stored messages currently in the
     * arrays.
     */
    public void saveStoredMessagesToFile() {
        ArrayList<Message> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (storedMessages[i] != null) {
                Message m = new Message(messageIDs[i], 0, recipients[i], storedMessages[i]);
                m.setMessageHash(messageHashes[i]);
                list.add(m);
            }
        }
        Message.writeStoredMessages(list);
    }

    // The text of the message at position i, whichever array it is in
    private String textAt(int i) {
        if (sentMessages[i] != null) {
            return sentMessages[i];
        }
        if (storedMessages[i] != null) {
            return storedMessages[i];
        }
        return disregardedMessages[i];
    }

    private String statusAt(int i) {
        if (sentMessages[i] != null) {
            return "Sent";
        }
        if (storedMessages[i] != null) {
            return "Stored";
        }
        return "Disregarded";
    }

    public String displayStoredSenderAndRecipient(String sender) {
        String result = "";
        for (int i = 0; i < count; i++) {
            if (storedMessages[i] != null) {
                result = result + "Sender: " + sender + " | Recipient: " + recipients[i] + "\n";
            }
        }
        if (result.isEmpty()) {
            return "No stored messages.";
        }
        return result.trim();
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

    public String searchByMessageID(String id) {
        for (int i = 0; i < count; i++) {
            if (messageIDs[i].equals(id)) {
                return "Recipient: " + recipients[i] + " | Message: " + textAt(i);
            }
        }
        return "Message ID not found.";
    }

    /**
     * All sent or stored messages for one recipient (disregarded ones are not
     * included).
     */
    public String searchByRecipient(String recipient) {
        String result = "";
        for (int i = 0; i < count; i++) {
            boolean sentOrStored = sentMessages[i] != null || storedMessages[i] != null;
            if (sentOrStored && recipients[i].equals(recipient)) {
                result = result + textAt(i) + "\n";
            }
        }
        if (result.isEmpty()) {
            return "No messages found for this recipient.";
        }
        return result.trim();
    }

    public String deleteByHash(String hash) {
        if (hash == null) {
            return "Message hash not found.";
        }
        for (int i = 0; i < count; i++) {
            if (messageHashes[i] != null && messageHashes[i].equalsIgnoreCase(hash.trim())) {
                String text = textAt(i);
                removeAt(i);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // Closes the gap left by a deleted message so the arrays stay parallel
    private void removeAt(int index) {
        for (int i = index; i < count - 1; i++) {
            messageIDs[i] = messageIDs[i + 1];
            messageHashes[i] = messageHashes[i + 1];
            recipients[i] = recipients[i + 1];
            sentMessages[i] = sentMessages[i + 1];
            disregardedMessages[i] = disregardedMessages[i + 1];
            storedMessages[i] = storedMessages[i + 1];
        }
        int last = count - 1;
        messageIDs[last] = null;
        messageHashes[last] = null;
        recipients[last] = null;
        sentMessages[last] = null;
        disregardedMessages[last] = null;
        storedMessages[last] = null;
        count--;
    }

    public String displayReport() {
        if (count == 0) {
            return "No messages to report.";
        }
        String report = "";
        for (int i = 0; i < count; i++) {
            report = report + "Message " + (i + 1) + " (" + statusAt(i) + ")\n"
                    + "Message ID: " + messageIDs[i] + "\n"
                    + "Message Hash: " + messageHashes[i] + "\n"
                    + "Recipient: " + recipients[i] + "\n"
                    + "Message: " + textAt(i) + "\n\n";
        }
        return report.trim();
    }

    public int getCount() {
        return count;
    }

    public String[] getSentMessages() {
        return collect(sentMessages);
    }

    public String[] getDisregardedMessages() {
        return collect(disregardedMessages);
    }

    public String[] getStoredMessages() {
        return collect(storedMessages);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, count);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, count);
    }

    // Only the filled positions of one array, in order, with no gaps
    private String[] collect(String[] source) {
        int filled = 0;
        for (int i = 0; i < count; i++) {
            if (source[i] != null) {
                filled++;
            }
        }
        String[] result = new String[filled];
        int next = 0;
        for (int i = 0; i < count; i++) {
            if (source[i] != null) {
                result[next] = source[i];
                next++;
            }
        }
        return result;
    }
}
