/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banca.bancatcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author seba2
 */
public class Client {

    public static void main(String[] args) {
        try (
                final Socket socket = new Socket("localhost", 8080); final BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream())); final PrintWriter outputToServer = new PrintWriter(socket.getOutputStream(), true); final BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));) {
            System.out.println("Connessione stabilita con il server.");

            final Thread receiveThread = new Thread(() -> {
                try {
                    String input;
                    while ((input = inputFromServer.readLine()) != null) {
                        System.out.println(input);
                    }
                } catch (IOException ex) {
                }
            });

            final Thread sendThread = new Thread(() -> {
                try {
                    String output;
                    while ((output = userInput.readLine()) != null) {
                        outputToServer.println(output);
                    }
                } catch (IOException ex) {
                }
            });

            receiveThread.start();
            sendThread.start();

            try {
                receiveThread.join();
                sendThread.join();
            } catch (InterruptedException e) {
            }
        } catch (IOException e) {
        }
    }
}
