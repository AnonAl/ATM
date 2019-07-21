package com.company.classes;

import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

class ATMAccountsService {
    private List<Pair<String, String>> accountsData;
    private List<Pair<String, Integer>> accountsMoney;
    private FileReaderWriter fileReaderWriter;

    ATMAccountsService() {
        try {
            this.fileReaderWriter = new FileReaderWriter("./data/accountsData.txt",
                    "./data/accountsMoney.txt");
            this.accountsData = this.fileReaderWriter.getAccountsData();
            this.accountsMoney = this.fileReaderWriter.getAccountsMoney();
        } catch(IOException ignored) {
        }
    }

    boolean verifyCardOwner(String cardNumber, String pinCode) {
        return this.accountsData.contains(new Pair<>(cardNumber, pinCode));
    }

    int checkAccountBalance(String cardNumber) {
       final int[] accountMoney = new int[1];
        this.accountsMoney.forEach(account -> {
            if (account.getKey().equals(cardNumber)) accountMoney[0] = account.getValue();
        });
        return accountMoney[0];
    }

    boolean withdrawMoney(String cardNumber, int value) {
        int accountMoney = 0;
        int iterator = 0;
        for (Pair<String, Integer> account : this.accountsMoney) {
            if (account.getKey().equals(cardNumber)) {
                accountMoney = account.getValue();
                if (accountMoney < value) {
                    return false;
                }
                accountMoney -= value;
                break;
            }
            iterator++;
        }
        this.accountsMoney.set(iterator, new Pair<>(cardNumber, accountMoney));
        try {
            this.fileReaderWriter.saveAccountsMoney(this.accountsMoney);
        } catch (IOException ignored) {
        }
        return true;
    }

    void replenishMoney(String cardNumber, int value) {
        int accountMoney = 0;
        int iterator = 0;
        for (Pair<String, Integer> account : this.accountsMoney) {
            if (account.getKey().equals(cardNumber)) {
                accountMoney = account.getValue();
                accountMoney += value;
                break;
            }
            iterator++;
        }
        this.accountsMoney.set(iterator, new Pair<>(cardNumber, accountMoney));
        try {
            this.fileReaderWriter.saveAccountsMoney(this.accountsMoney);
        } catch (IOException ignored) {
        }
    }
}
