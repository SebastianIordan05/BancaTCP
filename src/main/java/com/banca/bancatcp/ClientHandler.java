/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.banca.bancatcp;

import Banca.Banca;
import Banca.ContoCorrente;
import Banca.Utente;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author seba2
 */
public class ClientHandler implements Runnable {

    private Socket cs;
    private BufferedReader inputFromClient;
    private PrintWriter outputToClient;
    private Utente u;
    private ContoCorrente cc;

    public ClientHandler(Socket cs) {
        this.cs = cs;
        try {
            inputFromClient = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            outputToClient = new PrintWriter(cs.getOutputStream(), true);
        } catch (IOException ex) {
            System.err.println("Error opening input/output streams with client: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                outputToClient.println("Vuoi creare un'account o accedere ad uno esistente? (login or register) (bye to exit)");
                String response = inputFromClient.readLine();
                if (response == null || response.equalsIgnoreCase("bye")) {
                    break;
                }
                handleRequest(response);

            }
        } catch (IOException ex) {
        } finally {
            try {
                if (cs != null) {
                    cs.close();
                    System.out.println("Client connection closed: " + cs);
                }
            } catch (IOException ex) {
            }
        }
    }

    private void handleRequest(String request) {
        switch (request) {
            case "register" ->
                handleRegister();
            case "login" ->
                handleLogin();
            default -> {
                outputToClient.println("Invalid command");
                run();
            }
        }
    }

    private void handleRegister() {
        try {
            outputToClient.println("Inserisci il nome utente:");
            String username = inputFromClient.readLine();
            outputToClient.println("Inserisci la password:");
            String password = inputFromClient.readLine();
            
            if (username.equals("admin") || password.equals("admin")) {
                outputToClient.println("Non puoi creare un utente con queste credenziali");
                handleRegister();
            }

            u = new Utente(username, password);
            cc = new ContoCorrente(0, username);
            Banca.addUser(username, u);
            Banca.addConto(username, cc);
            
            outputToClient.println("Utente creato con successo, all'utente è stato assegnato un nuovo conto.");
            
            handleOptions();
        } catch (IOException ex) {
        }
    }

    private void handleLogin() {
        try {
            outputToClient.println("Inserisci il nome utente:");
            String username = inputFromClient.readLine();
            outputToClient.println("Inserisci la password:");
            String password = inputFromClient.readLine();
            
            if (username.equals("admin") && password.equals("admin"))
                handleAdminOptions();

            u = Banca.getUser(username);
            cc = Banca.getConto(username);

            if (u == null) {
                outputToClient.println("Nessun utente trovato con quel username!");
                return;
            }

            if (!password.equals(u.getPassword())) {
                outputToClient.println("Password errata!");
                return;
            }

            if (cc == null) {
                cc = new ContoCorrente(0, username);
                Banca.addConto(username, cc);
            }

            outputToClient.println("Login effettuato con successo.");
            
            handleOptions();
        } catch (IOException ex) {
        }
    }
    
    private void handleAdminOptions() {
        try {
            while (true) {
                outputToClient.println("""
                                               Cosa vuoi fare? (back to return to login/register & bye to exit)
                                               - Visualizza uteanti salvati nella banca (comandi: vsu || visualizzautenti)
                                               - VIsualizza conti e relativi saldi salvati nella banca (comandi: vsc || visualizzaconti)
                                               - Elimina utente (comandi: elmu || eliminautente)
                                               - Elimina conto (comandi: elmc || eliminaconto)""");
                final String option = inputFromClient.readLine();

                switch (option) {
                    case "vsu", "visualizzautenti" -> {
                        outputToClient.println("Utenti:");
                        System.out.println("Utenti: ");
                        for (final Utente user : Banca.getUsers().values()) {
                            outputToClient.println(user);
                            System.out.println(user);
                        }
                    }
                    case "vsc", "visualizzaconti" -> {
                        outputToClient.println("Conti:");
                        System.out.println("Conti: ");
                        for (final ContoCorrente conto : Banca.getConti().values()) {
                            outputToClient.println(conto);
                            System.out.println(conto);
                        }
                    }
                    case "elmu", "eliminautente" -> {
                        outputToClient.println("Utenti:");
                        for (final Utente user : Banca.getUsers().values()) {
                            outputToClient.println(user);
                        }
                        outputToClient.println("Quale utente vuoi eliminare? (scrivi il suo username)");
                        String username = inputFromClient.readLine();
                        
                        Banca.removeUser(username);
                        Banca.removeConto(username);
                        outputToClient.println("Utente (e conto associato) rimosso dalla banca con successo.");
                    }
                    case "elmc", "eliminaconto" -> {
                        outputToClient.println("Conti:");
                        for (final ContoCorrente conto : Banca.getConti().values()) {
                            outputToClient.println(conto);
                        }
                        outputToClient.println("Quale conto vuoi eliminare? (scrivi 'username dell'utente a cui appartiene)");
                        String username = inputFromClient.readLine();
                        
                        Banca.removeConto(username);
                        outputToClient.println("Conto rimosso dalla banca con successo.");
                    }
                    case "bye" -> {
                        handleCloseSave();
                    }
                    case "back" -> {
                        run();
                    }
                    default -> {
                        continue;
                    }
                }
            }
        } catch (IOException ex) {
        }
    }

    private void handleOptions() {
        try {
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
                        handleCloseSave();
                    }
                    case "back" -> {
                        run();
                    }
                    default -> {
                        continue;
                    }
                }
            }
        } catch (IOException ex) {
        }
    }

    private void handleCloseSave() {
        Server.save();

        try {
            cs.close();
        } catch (IOException ex) {
        }
    }
}
