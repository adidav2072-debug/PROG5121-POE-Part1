package com.mycompany.chatapp.part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageStore {

    private final Path jsonFilePath;
    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    public MessageStore() {
        this(Paths.get("stored_messages.json"));
    }

    public MessageStore(Path jsonFilePath) {
        this.jsonFilePath = jsonFilePath;
    }

    public void recordMessage(Message msg, int choice) {
        if (msg == null) {
            return;
        }
        if (msg.getMessageID() != null && !msg.getMessageID().isEmpty()) {
            messageIDs.add(msg.getMessageID());
        }
        if (msg.getMessageHash() != null && !msg.getMessageHash().isEmpty()) {
            messageHashes.add(msg.getMessageHash());
        }

        if (choice == 1) {
            msg.setStatus("SENT");
            sentMessages.add(msg);
        } else if (choice == 2) {
            msg.setStatus("DISREGARDED");
            disregardedMessages.add(msg);
        } else if (choice == 3) {
            msg.setStatus("STORED");
            storedMessages.add(msg);
            saveStoredMessagesToJson();
        }
    }

    public void addSentMessage(Message msg) {
        recordMessage(msg, 1);
    }

    public void addDisregardedMessage(Message msg) {
        recordMessage(msg, 2);
    }

    public void addStoredMessage(Message msg) {
        recordMessage(msg, 3);
    }

    public void saveStoredMessagesToJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append(storedMessages.get(i).toJsonObject());
            if (i < storedMessages.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("]\n");

        try {
            if (jsonFilePath.getParent() != null) {
                Files.createDirectories(jsonFilePath.getParent());
            }
            Files.writeString(jsonFilePath, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Unable to save stored_messages.json: " + e.getMessage());
        }
    }

    public Message[] loadStoredMessagesFromJson() {
        storedMessages.clear();
        if (!Files.exists(jsonFilePath)) {
            return new Message[0];
        }

        try {
            String content = Files.readString(jsonFilePath, StandardCharsets.UTF_8).trim();
            if (content.isEmpty() || content.equals("[]")) {
                return new Message[0];
            }

            List<String> objects = splitJsonObjects(content);
            for (String obj : objects) {
                String id = extractJsonString(obj, "messageID");
                int num = extractJsonInt(obj, "messageNumber");
                String sender = extractJsonString(obj, "sender");
                String recipient = extractJsonString(obj, "recipientCell");
                String text = extractJsonString(obj, "messageText");
                String hash = extractJsonString(obj, "messageHash");
                String status = extractJsonString(obj, "status");

                Message loaded = new Message(id, num, sender, recipient, text, hash, status);
                storedMessages.add(loaded);
                if (loaded.getMessageID() != null && !loaded.getMessageID().isEmpty() && !messageIDs.contains(loaded.getMessageID())) {
                    messageIDs.add(loaded.getMessageID());
                }
                if (loaded.getMessageHash() != null && !loaded.getMessageHash().isEmpty() && !messageHashes.contains(loaded.getMessageHash())) {
                    messageHashes.add(loaded.getMessageHash());
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read stored_messages.json: " + e.getMessage());
        }

        return getStoredMessagesArray();
    }

    private List<String> splitJsonObjects(String jsonArrayText) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        boolean escaping = false;
        int depth = 0;
        int start = -1;

        for (int i = 0; i < jsonArrayText.length(); i++) {
            char c = jsonArrayText.charAt(i);
            if (escaping) {
                escaping = false;
                continue;
            }
            if (c == '\\' && inQuotes) {
                escaping = true;
                continue;
            }
            if (c == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (!inQuotes) {
                if (c == '{') {
                    if (depth == 0) {
                        start = i;
                    }
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0 && start != -1) {
                        result.add(jsonArrayText.substring(start, i + 1));
                        start = -1;
                    }
                }
            }
        }
        return result;
    }

    private String extractJsonString(String obj, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher matcher = pattern.matcher(obj);
        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }
        return "";
    }

    private int extractJsonInt(String obj, String key) {
        Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+)");
        Matcher matcher = pattern.matcher(obj);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String unescapeJson(String value) {
        return value.replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
    }

    // a. Display the sender and recipient of all stored messages
    public String displaySendersAndRecipients() {
        Message[] allMessages = combineActiveMessages();
        if (allMessages.length == 0) {
            return "No stored or sent messages available.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allMessages.length; i++) {
            sb.append("Sender: ").append(allMessages[i].getSender())
              .append(" | Recipient: ").append(allMessages[i].getRecipientCell());
            if (i < allMessages.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    // b. Display the longest stored message
    public String displayLongestMessage() {
        Message[] pool = getMessagePoolForSearch();
        if (pool.length == 0) {
            return "";
        }
        Message longest = pool[0];
        for (Message m : pool) {
            if (m.getMessageText() != null && m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        return longest.getMessageText();
    }

    // c. Search for a message ID and display the corresponding recipient and message
    public String searchByMessageID(String targetID) {
        if (targetID == null || targetID.trim().isEmpty()) {
            return "Message ID not found.";
        }
        Message[] pool = getMessagePoolForSearch();
        for (Message m : pool) {
            if (targetID.trim().equals(m.getMessageID())) {
                return "Recipient: " + m.getRecipientCell() + " | Message: " + m.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    // d. Search for all messages stored for a particular recipient
    public String searchByRecipient(String recipient) {
        if (recipient == null || recipient.trim().isEmpty()) {
            return "";
        }
        List<String> matches = new ArrayList<>();
        Message[] pool = getMessagePoolForSearch();
        for (Message m : pool) {
            if (recipient.trim().equals(m.getRecipientCell())) {
                matches.add(m.getMessageText());
            }
        }
        return String.join("\n", matches);
    }

    // e. Delete a message using the message hash
    public String deleteMessageByHash(String hash) {
        if (hash == null || hash.trim().isEmpty()) {
            return "Message hash not found.";
        }
        String trimmedHash = hash.trim();

        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            if (trimmedHash.equalsIgnoreCase(m.getMessageHash())) {
                String deletedText = m.getMessageText();
                storedMessages.remove(i);
                messageHashes.remove(m.getMessageHash());
                messageIDs.remove(m.getMessageID());
                saveStoredMessagesToJson();
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        for (int i = 0; i < sentMessages.size(); i++) {
            Message m = sentMessages.get(i);
            if (trimmedHash.equalsIgnoreCase(m.getMessageHash())) {
                String deletedText = m.getMessageText();
                sentMessages.remove(i);
                messageHashes.remove(m.getMessageHash());
                messageIDs.remove(m.getMessageID());
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    // f. Display a report that lists the full details of all the stored messages
    public String displayFullReport() {
        Message[] pool = getMessagePoolForSearch();
        if (pool.length == 0) {
            return "No messages to display in report.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pool.length; i++) {
            Message m = pool[i];
            sb.append("Message Hash: ").append(m.getMessageHash())
              .append(" | Recipient: ").append(m.getRecipientCell())
              .append(" | Message: ").append(m.getMessageText());
            if (i < pool.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private Message[] getMessagePoolForSearch() {
        List<Message> combined = new ArrayList<>();
        combined.addAll(sentMessages);
        combined.addAll(storedMessages);
        return combined.toArray(new Message[0]);
    }

    private Message[] combineActiveMessages() {
        List<Message> combined = new ArrayList<>();
        combined.addAll(sentMessages);
        combined.addAll(storedMessages);
        return combined.toArray(new Message[0]);
    }

    public Message[] getSentMessagesArray() {
        return sentMessages.toArray(new Message[0]);
    }

    public Message[] getDisregardedMessagesArray() {
        return disregardedMessages.toArray(Message[]::new);
    }

    public Message[] getStoredMessagesArray() {
        return storedMessages.toArray(Message[]::new);
    }

    public String[] getMessageHashesArray() {
        return messageHashes.toArray(String[]::new);
    }

    public String[] getMessageIDsArray() {
        return messageIDs.toArray(String[]::new);
    }
}