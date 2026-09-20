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
                System.out.println("Username successfully captured.");
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

        input.close();
    }
}
