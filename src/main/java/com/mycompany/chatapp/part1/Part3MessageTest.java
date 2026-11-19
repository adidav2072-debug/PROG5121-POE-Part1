package com.mycompany.chatapp.part1;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Part3MessageTest {

    private MessageStore buildSampleStore() throws Exception {
        Path tempJson = Files.createTempFile("stored_messages_test", ".json");
        tempJson.toFile().deleteOnExit();
        MessageStore store = new MessageStore(tempJson);

        Message m1 = new Message(1, "Adi", "+27821112222",
                "Hey, is Adi bringing snacks to class today?", "SENT");
        Message m2 = new Message(2, "Adi", "+27838884567",
                "Hey Adi, I am stuck in traffic right now and might be a few minutes late for our study session.",
                "STORED");
        Message m3 = new Message(3, "Adi", "+27838884567",
                "Adi just sent the notes over to the group.", "STORED");
        Message m4 = new Message(4, "Adi", "0838884567",
                "Let's grab lunch after lecture.", "SENT");
        Message disregarded = new Message(5, "Adi", "+27829990000",
                "Skipping this draft.", "DISREGARDED");

        store.addSentMessage(m1);
        store.addStoredMessage(m2);
        store.addStoredMessage(m3);
        store.addSentMessage(m4);
        store.addDisregardedMessage(disregarded);

        return store;
    }

    @Test
    public void testSentMessagesArrayPopulated() throws Exception {
        MessageStore store = buildSampleStore();
        Message[] sent = store.getSentMessagesArray();
        assertEquals(2, sent.length);
        assertEquals("Hey, is Adi bringing snacks to class today?", sent[0].getMessageText());
        assertEquals("Let's grab lunch after lecture.", sent[1].getMessageText());
    }

    @Test
    public void testDisplayLongestMessage() throws Exception {
        MessageStore store = buildSampleStore();
        String expected = "Hey Adi, I am stuck in traffic right now and might be a few minutes late for our study session.";
        assertEquals(expected, store.displayLongestMessage());
    }

    @Test
    public void testSearchByMessageID() throws Exception {
        MessageStore store = buildSampleStore();
        Message target = store.getSentMessagesArray()[1];
        String expected = "Recipient: 0838884567 | Message: Let's grab lunch after lecture.";
        assertEquals(expected, store.searchByMessageID(target.getMessageID()));
    }

    @Test
    public void testSearchByRecipient() throws Exception {
        MessageStore store = buildSampleStore();
        String expected = "Hey Adi, I am stuck in traffic right now and might be a few minutes late for our study session.\n"
                       + "Adi just sent the notes over to the group.";
        assertEquals(expected, store.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteByMessageHash() throws Exception {
        MessageStore store = buildSampleStore();
        Message target = store.getStoredMessagesArray()[0];
        String expected = "Message: \"" + target.getMessageText() + "\" successfully deleted.";
        assertEquals(expected, store.deleteMessageByHash(target.getMessageHash()));
    }

    @Test
    public void testDisplayReport() throws Exception {
        MessageStore store = buildSampleStore();
        Message firstSent = store.getSentMessagesArray()[0];
        String expectedFirstLine = "Message Hash: " + firstSent.getMessageHash()
                + " | Recipient: +27821112222"
                + " | Message: Hey, is Adi bringing snacks to class today?";
        String firstLine = store.displayFullReport().split("\n")[0];
        assertEquals(expectedFirstLine, firstLine);
    }
}
