package com.mycompany.chatapp.part1;

import org.junit.jupiter.api.Test;
public class MessageTest {

    @Test
    public void testCheckMessageLengthSuccess() {
        Message msg = new Message();
        String result = msg.checkMessageLength("Hi Adi, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void testCheckMessageLengthFailure() {
        Message msg = new Message();
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longMsg.append("a");
        }
        String result = msg.checkMessageLength(longMsg.toString());
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void testCheckRecipientCellSuccess() {
        Message msg = new Message();
        String result = msg.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void testCheckRecipientCellFailure() {
        Message msg = new Message();
        String result = msg.checkRecipientCell("085759758891111");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    @Test
    public void testSentMessageOptions() {
        Message msg = new Message();
        assertEquals("Message successfully sent.", msg.SentMessage(1));
        assertEquals("Press 0 to delete the message.", msg.SentMessage(2));
        assertEquals("Message successfully stored.", msg.SentMessage(3));
    }

    private void assertEquals(String message_ready_to_send, String result) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}