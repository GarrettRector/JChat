package com.muc;

import java.io.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.BufferedReader;
import java.io.FileReader;

public class ServerLogger<line> {
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        String login = reader.readLine();
        String password = reader.readLine();


        FileWriter fw = new FileWriter("login.csv",true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("\n");
        bw.write(login);
        bw.write(",");
        bw.write(password);
        bw.close();

        try(
                BufferedReader br = new BufferedReader(new FileReader("login.csv"));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(br)
        ) {
            for(CSVRecord record : parser) {
                String logintoken = record.get("login");
                System.out.println(logintoken);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
