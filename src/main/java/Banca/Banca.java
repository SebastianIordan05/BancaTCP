/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Banca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author seba2
 */
public class Banca {

    private final static String FILE_PATH = "./users.dat";
    private final static String FILE_PATH2 = "./conti.dat";
    
    private final static Map<String, Utente> users = load(new File(FILE_PATH), Utente.class);
    private final static Map<String, ContoCorrente> conti = load(new File(FILE_PATH2), ContoCorrente.class);

    public static String getFILE_PATH() {
        return FILE_PATH;
    }

    public static String getFILE_PATH2() {
        return FILE_PATH2;
    }
    
    public static Map<String, Utente> getUsers() {
        return users;
    }

    public static Map<String, ContoCorrente> getConti() {
        return conti;
    }

    public static Utente getUser(String username) {
        return users.get(username);
    }

    public static ContoCorrente getConto(String username) {
        return conti.get(username);
    }
    
    public static void addUser(String username, Utente user) {
        users.put(username, user);
    }
    
    public static void removeUser(String username) {
        users.remove(username);
    }
    
    public static void addConto(String username, ContoCorrente conto) {
        conti.put(username, conto);
    }
    
    public static void removeConto(String username) {
        conti.remove(username);
    }

    private static <T> Map<String, T> load(final File f, Class<T> v) {
        try {
            if (!f.exists()) {
                f.createNewFile();
                return new HashMap<>();
            }

            if (!f.canRead()) {
                return new HashMap<>();
            }

            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(f))) {
                return (Map<String, T>) inputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException ex) {
        }

        return new HashMap<>();
    }

    public static <T> void save(final Map<String, T> map, final File f) {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            if (!f.canWrite()) {
                return;
            }

            final ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(f));
            outputStream.writeObject(map);
        } catch (final IOException ex) {
        }
    }
}
