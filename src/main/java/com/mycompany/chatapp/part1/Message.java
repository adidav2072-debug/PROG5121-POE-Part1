package com.mycompany.chatapp.part1;

import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String sender;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String status;

    public Message() {
        this.sender = "Adi";
        this.status = "PENDING";
    }

    public Message(int messageNumber, String recipientCell, String messageText) {
        this(messageNumber, "Adi", recipientCell, messageText, "PENDING");
    }

    public Message(int messageNumber, String sender, String recipientCell, String messageText, String status) {
        this.messageNumber = messageNumber;
        this.sender = (sender == null || sender.trim().isEmpty()) ? "Adi" : sender.trim();
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.status = (status == null || status.trim().isEmpty()) ? "PENDING" : status.trim();
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    public Message(String messageID, int messageNumber, String sender, String recipientCell,
                   String messageText, String messageHash, String status) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = (sender == null || sender.trim().isEmpty()) ? "Adi" : sender.trim();
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.status = status;
        if (messageHash == null || messageHash.trim().isEmpty()) {
            this.messageHash = createMessageHash();
        } else {
            this.messageHash = messageHash;
        }
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
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        if (firstWord.isEmpty()) {
            firstWord = words[0];
        }
        if (lastWord.isEmpty()) {
            lastWord = words[words.length - 1];
        }

        String rawHash = firstTwoID + ":" + this.messageNumber + ":" + firstWord + lastWord;
        return rawHash.toUpperCase();
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.status = "SENT";
                return "Message successfully sent.";
            case 2:
                this.status = "DISREGARDED";
                return "Press 0 to delete the message.";
            case 3:
                this.status = "STORED";
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public String printMessages() {
        return "Message ID: " + messageID + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Sender: " + sender + "\n"
             + "Recipient: " + recipientCell + "\n"
             + "Message: " + messageText;
    }

    public int returnTotalMessagess(int sentCount) {
        return sentCount;
    }

    public String toJsonObject() {
        return "  {\n"
             + "    \"messageID\": \"" + escapeJson(messageID) + "\",\n"
             + "    \"messageNumber\": " + messageNumber + ",\n"
             + "    \"sender\": \"" + escapeJson(sender) + "\",\n"
             + "    \"recipientCell\": \"" + escapeJson(recipientCell) + "\",\n"
             + "    \"messageText\": \"" + escapeJson(messageText) + "\",\n"
             + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
             + "    \"status\": \"" + escapeJson(status) + "\"\n"
             + "  }";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r");
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {
        return sender;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}