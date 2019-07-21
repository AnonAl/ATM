package com.company.classes;

import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReaderWriter {
    private String accountsDataFileName;
    private String accountsMoneyFileName;
    private FileReader fileReader;
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    FileReaderWriter(String accountsDataFileName, String accountsMoneyFileName) {
        this.accountsDataFileName = accountsDataFileName;
        this.accountsMoneyFileName = accountsMoneyFileName;
    }

    List<Pair<String, String>> getAccountsData() throws IOException {
        this.fileReader = new FileReader(this.accountsDataFileName);
        this.bufferedReader = new BufferedReader(this.fileReader);
        String line;
        List<Pair<String, String>> answer = new ArrayList<>();
        while((line = this.bufferedReader.readLine()) != null){
            String[] cardNumberPin = line.split(" ");
            answer.add(new Pair<>(cardNumberPin[0], cardNumberPin[1]));
        }
        this.fileReader.close();
        this.bufferedReader.close();
        return answer;
    }

    List<Pair<String, Integer>> getAccountsMoney() throws IOException {
        this.fileReader = new FileReader(this.accountsMoneyFileName);
        this.bufferedReader = new BufferedReader(this.fileReader);
        String line;
        List<Pair<String, Integer>> answer = new ArrayList<>();
        while((line = this.bufferedReader.readLine()) != null){
            String[] cardNumberPin = line.split(" ");
            answer.add(new Pair<>(cardNumberPin[0], Integer.parseInt(cardNumberPin[1])));
        }
        this.fileReader.close();
        this.bufferedReader.close();
        return answer;
    }

    public void saveAccountsData(List<Pair<String, String>> accountsData) throws IOException {
        this.fileWriter = new FileWriter(this.accountsDataFileName, false);
        this.bufferedWriter = new BufferedWriter(this.fileWriter);
        accountsData.forEach(account -> {
            try {
                this.bufferedWriter.write(account.getKey() + " " + account.getValue());
                this.bufferedWriter.newLine();
            } catch (IOException e) {
            }
        });
        this.bufferedWriter.close();
        this.fileWriter.close();
    }

    void saveAccountsMoney(List<Pair<String, Integer>> accountsData) throws IOException {
        this.fileWriter = new FileWriter(this.accountsMoneyFileName, false);
        this.bufferedWriter = new BufferedWriter(this.fileWriter);
        accountsData.forEach(account -> {
            try {
                this.bufferedWriter.write(account.getKey() + " " + account.getValue());
                this.bufferedWriter.newLine();
            } catch (IOException ignored) {
            }
        });
        this.bufferedWriter.close();
        this.fileWriter.close();
    }
}
