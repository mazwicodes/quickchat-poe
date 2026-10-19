/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.quickchat.quickchat.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author mazwi
 */
public class MessageTest {

    private static final String VALID_CELL = "+27718693002";
    private static final String MESSAGE_ONE = "Hi Mike, can you join us for dinner tonight?";

    @Test
    public void testMessageLengthSuccess() {
        Message message = new Message("0012345678", 0, VALID_CELL, MESSAGE_ONE);
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longText = "a".repeat(260);
        Message message = new Message("0012345678", 0, VALID_CELL, longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                message.checkMessageLength());
    }

    @Test
    public void testRecipientCellSuccess() {
        Message message = new Message("0012345678", 0, VALID_CELL, MESSAGE_ONE);
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void testRecipientCellFailure() {
        Message message = new Message("0012345678", 1, "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell());
    }

    @Test
    public void testMessageHashes() {
        String[] texts = {MESSAGE_ONE, "Hi Keegan, did you receive the payment?"};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};
        for (int i = 0; i < texts.length; i++) {
            Message message = new Message("0012345678", i, VALID_CELL, texts[i]);
            assertEquals(expected[i], message.createMessageHash());
        }
    }

    @Test
    public void testMessageIDGenerated() {
        String id = Message.generateMessageID();
        assertEquals(10, id.length());
        assertTrue(id.matches("[0-9]+"));
        Message message = new Message(id, 0, VALID_CELL, MESSAGE_ONE);
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testSentMessageOptions() {
        Message message = new Message("0012345678", 0, VALID_CELL, MESSAGE_ONE);
        assertEquals("Message successfully sent.", message.SentMessage(1));
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }

    @Test
    public void testTotalMessagesIncrease() {
        int before = Message.returnTotalMessages();
        Message message = new Message("0012345678", 0, VALID_CELL, MESSAGE_ONE);
        message.recordSent();
        assertEquals(before + 1, Message.returnTotalMessages());
    }
}
