/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.banca.bancatcp;

import Banca.Banca;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author seba2
 */
public class Server {

    private static ServerSocket ss;
    private static Socket cs;

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            System.out.println("Utenti: " + Banca.getUsers());
            System.out.println("Conti: " + Banca.getConti());

            while (true) {
                cs = ss.accept();
                System.out.println("New client connected: " + cs);

                Thread clientThread = new Thread(new ClientHandler(cs));
                clientThread.start();
            }
        } catch (IOException ex) {
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static void save() {
        Banca.save(Banca.getUsers(), new File(Banca.getFILE_PATH()));
        Banca.save(Banca.getConti(), new File(Banca.getFILE_PATH2()));
        System.out.println("Utenti: " + Banca.getUsers());
        System.out.println("Conti: " + Banca.getConti());
    }
}
