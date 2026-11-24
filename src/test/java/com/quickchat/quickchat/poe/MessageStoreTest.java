package com.quickchat.quickchat.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageStoreTest {

    // Test data from the brief. Message 4 uses the number 0838884567 as its ID, as in the brief's ID search test.
    private static final String MSG1 = "Did you get the cake?";
    private static final String MSG2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String MSG3 = "Yohoooo, I am at your gate.";
    private static final String MSG4 = "It is dinner time !";
    private static final String MSG5 = "Ok, I am leaving without you.";

    // Adds one message the way the app does: the hash comes from a real Message
    private void add(MessageStore store, String id, int number, String recipient, String text, String flag) {
        Message message = new Message(id, number, recipient, text);
        store.addMessage(id, message.createMessageHash(), recipient, text, flag);
    }

    private MessageStore loadTestData() {
        MessageStore store = new MessageStore();
        add(store, "0011111111", 0, "+27834557896", MSG1, "Sent");
        add(store, "0022222222", 1, "+27838884567", MSG2, "Stored");
        add(store, "0033333333", 2, "+27834484567", MSG3, "Disregard");
        add(store, "0838884567", 3, "0838884567", MSG4, "Sent");
        add(store, "0055555555", 4, "+27838884567", MSG5, "Stored");
        return store;
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertArrayEquals(new String[]{MSG1, MSG4}, loadTestData().getSentMessages());
    }

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        assertArrayEquals(new String[]{MSG3}, loadTestData().getDisregardedMessages());
    }

    @Test
    public void testStoredMessagesArrayPopulated() {
        assertArrayEquals(new String[]{MSG2, MSG5}, loadTestData().getStoredMessages());
    }

    @Test
    public void testMessageHashesAndIDsArraysPopulated() {
        MessageStore store = loadTestData();
        assertArrayEquals(new String[]{"00:0:DIDCAKE", "00:1:WHERETIME", "00:2:YOHOOOOGATE",
            "08:3:ITTIME", "00:4:OKYOU"}, store.getMessageHashes());
        assertArrayEquals(new String[]{"0011111111", "0022222222", "0033333333",
            "0838884567", "0055555555"}, store.getMessageIDs());
    }

    @Test
    public void testSenderAndRecipientOfStoredMessages() {
        assertEquals("Sender: Kyle Smith | Recipient: +27838884567\nSender: Kyle Smith | Recipient: +27838884567",
                loadTestData().displayStoredSenderAndRecipient("Kyle Smith"));
    }

    @Test
    public void testLongestStoredMessage() {
        assertEquals(MSG2, loadTestData().displayLongestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        assertEquals("Recipient: 0838884567 | Message: " + MSG4,
                loadTestData().searchByMessageID("0838884567"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        assertEquals("Message ID not found.", loadTestData().searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        assertEquals(MSG2 + "\n" + MSG5, loadTestData().searchByRecipient("+27838884567"));
    }

    @Test
    public void testSearchByRecipientWithNoMessages() {
        assertEquals("No messages found for this recipient.", loadTestData().searchByRecipient("+27000000000"));
    }

    @Test
    public void testSearchByRecipientIgnoresDisregardedMessages() {
        assertEquals("No messages found for this recipient.", loadTestData().searchByRecipient("+27834484567"));
    }

    @Test
    public void testDeleteByHash() {
        MessageStore store = loadTestData();
        assertEquals("Message: \"" + MSG2 + "\" successfully deleted.", store.deleteByHash("00:1:WHERETIME"));
        assertEquals(4, store.getCount());
        assertArrayEquals(new String[]{MSG5}, store.getStoredMessages());
        assertEquals("Message hash not found.", store.deleteByHash("00:1:WHERETIME"));
    }

    @Test
    public void testDeleteKeepsArraysParallel() {
        MessageStore store = loadTestData();
        store.deleteByHash("00:1:WHERETIME");
        assertEquals("Recipient: +27838884567 | Message: " + MSG5, store.searchByMessageID("0055555555"));
    }

    @Test
    public void testDeleteByHashNotFound() {
        assertEquals("Message hash not found.", loadTestData().deleteByHash("XX:9:NOPE"));
    }

    @Test
    public void testReportShowsAllDetails() {
        String report = loadTestData().displayReport();
        assertTrue(report.contains("Message Hash: 00:1:WHERETIME"));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: " + MSG2));
        assertTrue(report.contains("Message ID: 0838884567"));
        assertTrue(report.contains("Message 3 (Disregarded)"));
    }

    @Test
    public void testEmptyStoreMessages() {
        MessageStore store = new MessageStore();
        assertEquals("No stored messages.", store.displayLongestStoredMessage());
        assertEquals("No stored messages.", store.displayStoredSenderAndRecipient("Kyle Smith"));
        assertEquals("No messages to report.", store.displayReport());
    }

    @Test
    public void testInvalidFlagIsRejected() {
        MessageStore store = new MessageStore();
        assertFalse(store.addMessage("0011111111", "00:0:A", "+27834557896", "Hello", "banana"));
        assertEquals(0, store.getCount());
    }

    @Test
    public void testArraysStopAtTheirLimit() {
        MessageStore store = new MessageStore();
        for (int i = 0; i < 100; i++) {
            assertTrue(store.addMessage("0011111111", "00:" + i + ":A", "+27834557896", "Hello", "sent"));
        }
        assertFalse(store.addMessage("0011111111", "00:100:A", "+27834557896", "Hello", "sent"));
        assertEquals(100, store.getCount());
    }
}
