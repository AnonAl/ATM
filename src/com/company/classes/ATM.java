package com.company.classes;

import java.io.*;
import java.util.Scanner;


public class ATM {
    private int moneyCount;
    private final int MAX_ADD_MONEY_COUNT = 1000000;
    private int inputPinAttempts = 3;
    private ATMAccountsService atmAccountsService;
    private Validator validator;
    private String insertedCard;
    private String cardRegEx = "\\d\\d\\d\\d-\\d\\d\\d\\d-\\d\\d\\d\\d-\\d\\d\\d\\d";
    private String pinRegEx = "\\d\\d\\d\\d";
    private String input;
    private Scanner scanner;

    public ATM() {
        this.atmAccountsService = new ATMAccountsService();
        this.validator = new Validator();
        this.scanner = new Scanner(System.in);
        this.loadAtmData();
    }

    public void init() {
        while (true) {
            if (!this.inputCard()) {
                System.out.println("Error: card number not match pattern: XXXX-XXXX-XXXX-XXXX");
                continue;
            }
            this.inputPinAttempts = 3;
            if (!this.inputPin()) {
                return;
            }
            this.showInterface();
        }
    }

    private boolean inputCard() {
        System.out.println("Please input card number: ");
        this.input = scanner.nextLine();
        this.insertedCard = this.input;
        return this.validator.validate(this.input, this.cardRegEx);
    }

    private boolean inputPin() {
        while (this.inputPinAttempts > 0) {
            System.out.println("Please input PIN: ");
            this.input = this.scanner.nextLine();
            if (!this.validator.validate(this.input, this.pinRegEx)) {
                System.out.println("Error: PIN not match pattern XXXX");
                continue;
            }
            if (!this.atmAccountsService.verifyCardOwner(this.insertedCard, this.input)) {
                System.out.println("Error: wrong PIN!");
                this.inputPinAttempts--;
                continue;
            }
            return true;
        }
        System.out.println("Error: You are used 3 attempts, your card was blocked!");
        return false;
    }

    private void showInterface() {
        while (true) {
            System.out.println("\nAvailable operations:");
            System.out.println("1. Check the balance.");
            System.out.println("2. Withdraw money.");
            System.out.println("3. Replenish balance.");
            System.out.println("0. Eject card.");
            System.out.println("Choose operation: ");
            this.input = this.scanner.nextLine();
            switch (this.input) {
                case "1": {
                    this.checkBalance();
                    break;
                }
                case "2": {
                    this.withdrawMoney();
                    break;
                }
                case "3": {
                    this.replenishMoney();
                    break;
                }
                case "0": {
                    System.out.println("Take your card.");
                    return;
                }
                default: {
                    System.out.println("Error: wrong input!");
                }
            }
        }
    }

    private void checkBalance() {
        int money = this.atmAccountsService.checkAccountBalance(this.insertedCard);
        System.out.println("Account money: " + money);
    }

    private void withdrawMoney() {
        int moneyValue;
        while (true) {
            System.out.println("Write sum: ");
            this.input = this.scanner.nextLine();
            try {
                moneyValue = Integer.parseInt(this.input);
            } catch (NumberFormatException e) {
                System.out.println("Error: please input number value!");
                continue;
            }
            if (moneyValue > this.moneyCount) {
                System.out.println("Error: ATM don't have enough money!");
                continue;
            }
            if (!this.atmAccountsService.withdrawMoney(this.insertedCard, moneyValue)) {
                System.out.println("Error: Not enough a money in your account!");
                continue;
            }
            this.moneyCount -= moneyValue;
            this.saveAtmData();
            System.out.println("\nSuccess: Take your money.");
            return;
        }
    }

    private void replenishMoney() {
        int moneyValue;
        while (true) {
            System.out.println("Write sum: ");
            this.input = this.scanner.nextLine();
            try {
                moneyValue = Integer.parseInt(this.input);
            } catch (NumberFormatException e) {
                System.out.println("Error: please input number value!");
                continue;
            }
            if (moneyValue > this.MAX_ADD_MONEY_COUNT) {
                System.out.println("Error: Replenish value must be less than " + this.MAX_ADD_MONEY_COUNT + "!");
                continue;
            }
            this.atmAccountsService.replenishMoney(this.insertedCard, moneyValue);
            this.moneyCount += moneyValue;
            this.saveAtmData();
            System.out.println("\nSuccess: Balance replenished.");
            return;
        }
    }

    private void loadAtmData() {
        try {
            FileReader fileReader = new FileReader("./data/ATMData.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            this.moneyCount = Integer.parseInt(bufferedReader.readLine());
            bufferedReader.close();
            fileReader.close();
        } catch (IOException ignored) {
        }
    }

    private void saveAtmData() {
        try {
            FileWriter fileWriter = new FileWriter("./data/ATMData.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.valueOf(this.moneyCount));
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException ignored) {
        }
    }
}

