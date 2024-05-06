/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.banca.bancatcp;

import Banca.ContoCorrente;
import Banca.Utente;
import Banca.Banca;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author seba2
 */
public class Server {

    private static ServerSocket ss;
    private static Socket cs;
    private static BufferedReader inputFromClient;
    private static PrintWriter outputToClient;

    private static Utente u;
    private static ContoCorrente cc;

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            System.out.println("Utenti: " + Banca.getUsers());
            System.out.println("Conti: " + Banca.getConti());

            mainLoop:
            while (true) {

                cs = ss.accept();
                System.out.println("New client connected: " + cs);

                inputFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                outputToClient = new PrintWriter(cs.getOutputStream(), true);

                secondLoop:
                while (true) {
                    outputToClient.println("Vuoi creare un'account o accedere ad uno esistente? (login or register) (bye to exit)");
                    final String response = inputFromClient.readLine();

                    switch (response) {
                        case "register" -> {
                            outputToClient.println("Inserisci il nome utente:");
                            final String username = inputFromClient.readLine();
                            outputToClient.println("Inserisci la password:");
                            final String password = inputFromClient.readLine();
                            
                            u = new Utente(username, password);
                            cc = new ContoCorrente(0, username);
                            
                            Banca.addUser(u.getUsername(), u);
                            Banca.addConto(u.getUsername(), cc);
                            
                            System.out.println(u.toString());
                            System.out.println(cc.toString());
                            
                            outputToClient.println(u.toString() + ", " + cc.toString());
                        }
                        case "login" -> {
                            outputToClient.println("Inserisci il nome utente:");
                            final String username = inputFromClient.readLine();
                            outputToClient.println("Inserisci la password:");
                            final String password = inputFromClient.readLine();
                            
                            u = Banca.getUser(username);
                            cc = Banca.getConto(username);
                            
                            if (u == null && cc == null) {
                                outputToClient.println("Nessun utente con quel nome!");
                                closeSave();
                            }
                            
                            System.out.println(u.toString());
                            System.out.println(cc.toString());
                            
                            if (username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                                outputToClient.println("Accesso consentito.");
                            }
                        }
                        case "bye" -> {
                            closeSave();
                            break mainLoop;
                        }
                        default -> {
                            continue;
                        }
                    }

                    thirdLoop:
                    while (true) {

                        outputToClient.println("Cosa vuoi fare? (back to return to login/register & bye to exit)");
                        outputToClient.println("- Prelievo (prv || prelievo)");
                        outputToClient.println("- Versamento (vrs || versamento)");
                        outputToClient.println("- Visualizza conto corrente (vs || visualizza || conto)");
                        final String option = inputFromClient.readLine();

                        switch (option) {
                            case "vs", "visualizza", "conto" ->
                                outputToClient.println("Conto: " + cc.getConto());
                            case "prv", "prelievo" -> {
                                outputToClient.println("Importo: ");
                                final String input = inputFromClient.readLine();
                                double importo = Double.parseDouble(input);
                                final double finalImporto = cc.getConto() - importo;
                                cc.setConto(finalImporto);
                            }
                            case "vrs", "versamento" -> {
                                outputToClient.println("Importo: ");
                                final String input = inputFromClient.readLine();
                                double importo = Double.parseDouble(input);
                                final double finalImporto = cc.getConto() + importo;
                                cc.setConto(finalImporto);
                            }
                            case "bye" -> {
                                closeSave();
                                break mainLoop;
                            }
                            case "back" -> {
                                continue secondLoop;
                            }
                            default -> {
                                continue;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    private static void closeSave() {
        Banca.save(Banca.getUsers(), new File(Banca.getFILE_PATH()));
        Banca.save(Banca.getConti(), new File(Banca.getFILE_PATH2()));
        System.out.println("Utenti: " + Banca.getUsers());
        System.out.println("Conti: " + Banca.getConti());

        try {
            cs.close();
            ss.close();
        } catch (IOException ex) {
        }

    }
}
