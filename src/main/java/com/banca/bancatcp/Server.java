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
                            System.out.println("New utente creato con successo.");
                            System.out.println(u.toString());
                            System.out.println("New conto creato con successo.");
                            System.out.println(cc.toString());
                            
                            Banca.addUser(u.getUsername(), u);
                            Banca.addConto(u.getUsername(), cc);
                            
                            outputToClient.println("Utente creato con successo, all'utente è stato assegnato un nuovo conto.");
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
                                outputToClient.println("Login effettuato con successo.");
                            }
                        }
                        case "bye" -> {
                            closeSave();
                        }
                        default -> {
                            continue;
                        }
                    }

                    thirdLoop:
                    while (true) {
                        outputToClient.println("""
                                               Cosa vuoi fare? (back to return to login/register & bye to exit) 
                                               - Prelievo (comandi: prv || prelievo)
                                               - Versamento (comandi: vrs || versamento)
                                               - Visualizza saldo (comandi: sd || saldo)
                                               - Visualizza informazioni conto (comandi: infoc || infoconto || conto)
                                               - Visualizza informazioni utente (comandi: infou || infoutente || utente)""");
                        final String option = inputFromClient.readLine();

                        switch (option) {
                            case "sd", "saldo" -> {
                                outputToClient.println("Saldo: " + cc.getConto());
                                System.out.println("Saldo: " + cc.getConto());
                            }
                            case "prv", "prelievo" -> {
                                outputToClient.println("Importo: ");
                                final String input = inputFromClient.readLine();
                                double importo = Double.parseDouble(input);
                                final double finalImporto = cc.getConto() - importo;
                                
                                System.out.println("Old saldo: " + cc.getConto());
                                cc.setConto(finalImporto);
                                System.out.println("New saldo: " + cc.getConto());
                            }
                            case "vrs", "versamento" -> {
                                outputToClient.println("Importo: ");
                                final String input = inputFromClient.readLine();
                                double importo = Double.parseDouble(input);
                                final double finalImporto = cc.getConto() + importo;
                                
                                System.out.println("Old saldo: " + cc.getConto());
                                cc.setConto(finalImporto);
                                System.out.println("New Saldo: " + cc.getConto());
                            }
                            case "infoc", "infoconto", "conto" -> {
                                outputToClient.println(cc.toString());
                                System.out.println(cc.toString());
                            }
                            case "infou", "infoutente", "utente" -> {
                                outputToClient.println(u.toString());
                                System.out.println(u.toString());
                            }
                            case "bye" -> {
                                closeSave();
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
