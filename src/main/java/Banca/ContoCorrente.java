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
public class ContoCorrente implements Serializable {
    private double conto;
    private final String nomeProprietario;

    public ContoCorrente(double conto, String nomeProprietario) {
        this.conto = conto;
        this.nomeProprietario = nomeProprietario;
    }

    public double getConto() {
        return conto;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    @Override
    public String toString() {
        return "ContoCorrente{" + "conto=" + conto + ", nomeProprietario=" + nomeProprietario + '}';
    }

    public void setConto(double conto) {
        this.conto = conto;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.conto) ^ (Double.doubleToLongBits(this.conto) >>> 32));
        hash = 37 * hash + Objects.hashCode(this.nomeProprietario);
        return hash;
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

    
}
