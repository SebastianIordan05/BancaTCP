/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Banca;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author seba2
 */
public class Utente implements Serializable {
    private final String username;
    private final String password;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Utente{" + "username=" + username + ", password=" + password + '}';
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.username);
        hash = 31 * hash + Objects.hashCode(this.password);
        return hash;
    }
}
