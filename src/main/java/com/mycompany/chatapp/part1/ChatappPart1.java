/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp.part1;

import java.util.Scanner;

public class ChatappPart1 {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login loginObj = new Login();

        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        String username = "";
        while (true) {
            System.out.print("Enter Username: ");
            username = input.nextLine();

            if (loginObj.checkUserName(username)) {
                System.out.println("The user has been registered successfully.");
                break;
            } else {
                System.out.println("Username must contain an underscore (_) and be 5 characters or fewer.");
            }
        }

        String password = "";
        while (true) {
            System.out.print("Enter Password: ");
            password = input.nextLine();

            if (loginObj.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            } else {
                System.out.println("Password must be at least 8 characters long with a capital letter, a number, and a special character.");
            }
        }

        String cellNumber = "";
        while (true) {
            System.out.print("Enter Cell Phone Number: ");
            cellNumber = input.nextLine();

            if (loginObj.checkCellPhoneNumber(cellNumber)) {
                System.out.println("Cell phone number successfully captured.");
                break;
            } else {
                System.out.println("Please enter a valid phone number (e.g., 0821234567 or +27821234567).");
            }
        }

        String regStatus = loginObj.registerUser(username, password, cellNumber);
        System.out.println("\n" + regStatus + "\n");

        System.out.print("Enter Username: ");
        String enteredUser = input.nextLine();

        System.out.print("Enter Password: ");
        String enteredPass = input.nextLine();

        boolean isLoggedIn = loginObj.loginUser(username, password, enteredUser, enteredPass);
        String loginStatus = loginObj.returnLoginStatus(isLoggedIn, firstName, lastName);

        System.out.println("\n" + loginStatus);

       
        if (isLoggedIn) {
            System.out.println("\nWelcome to QuickChat.");

            boolean running = true;
            int totalSent = 0;

            while (running) {
                System.out.println("\nPlease choose an option:");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.print("Enter option: ");

                int option = Integer.parseInt(input.nextLine());

                if (option == 1) {
                    System.out.print("How many messages would you like to enter? ");
                    int numMessages = Integer.parseInt(input.nextLine());

                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " ---");

                        String cell = "";
                        Message msg = new Message();
                        while (true) {
                            System.out.print("Enter Recipient Cell Number: ");
                            cell = input.nextLine();
                            String cellStatus = msg.checkRecipientCell(cell);
                            System.out.println(cellStatus);
                            if (cellStatus.equals("Cell phone number successfully captured.")) {
                                break;
                            }
                        }

                        String text = "";
                        while (true) {
                            System.out.print("Enter Message (max 250 chars): ");
                            text = input.nextLine();
                            String lenStatus = msg.checkMessageLength(text);
                            System.out.println(lenStatus);
                            if (lenStatus.equals("Message ready to send.")) {
                                break;
                            }
                        }

                        Message currentMsg = new Message(i, cell, text);
                        System.out.println("Message ID generated: " + currentMsg.getMessageID());
                        System.out.println("Message Hash: " + currentMsg.getMessageHash());

                        System.out.println("\nChoose action:");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");
                        System.out.print("Choice: ");
                        int action = Integer.parseInt(input.nextLine());

                        String actionResult = currentMsg.SentMessage(action);
                        System.out.println(actionResult);

                        if (action == 1) {
                            totalSent++;
                        }

                        System.out.println("\n--- Message Details ---");
                        System.out.println(currentMsg.printMessages());
                    }

                    System.out.println("\nTotal messages sent so far: " + totalSent);

                } else if (option == 2) {
                    System.out.println("Coming Soon.");
                } else if (option == 3) {
                    running = false;
                    System.out.println("Goodbye!");
                } else {
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
                }
            }
        }

        input.close();
    }
}
