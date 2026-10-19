package com.mycompany.chatapp.part1;

import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;

    public Message() {
    }

    public Message(int messageNumber, String recipientCell, String messageText) {
        this.messageNumber = messageNumber;
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    public String checkRecipientCell(String cellNumber) {
        if (cellNumber != null && cellNumber.matches("^(\\+27|0)[0-9]{9}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength(String message) {
        if (message == null) {
            return "Please enter a message of less than 250 characters.";
        }
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int overflow = message.length() - 250;
            return "Message exceeds 250 characters by " + overflow + "; please reduce the size.";
        }
    }

    public String createMessageHash() {
        if (this.messageID == null || this.messageID.length() < 2 || this.messageText == null || this.messageText.trim().isEmpty()) {
            return "";
        }

        String firstTwoID = this.messageID.substring(0, 2);
        String[] words = this.messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        String rawHash = firstTwoID + ":" + this.messageNumber + ":" + firstWord + lastWord;
        return rawHash.toUpperCase();
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
                return "Invalid option.";
        }
    }

    public String printMessages() {
        return "Message ID: " + messageID + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Recipient: " + recipientCell + "\n"
             + "Message: " + messageText;
    }

    public int returnTotalMessagess(int sentCount) {
        return sentCount;
    }

    public String getMessageID() { 
        return messageID; 
    }
    
    public int getMessageNumber() { 
        return messageNumber; 
    }
    
    public String getRecipientCell() { 
        return recipientCell; 
    }
    
    public String getMessageText() { 
        return messageText; 
    }
    
    public String getMessageHash() { 
        return messageHash; 
    }
}
