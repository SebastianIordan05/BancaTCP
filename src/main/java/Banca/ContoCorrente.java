/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Banca;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author seba2
 */
public class ContoCorrente implements Serializable {
    private double conto;
    private final String nomeProprietario;
    private final String iban;

    public ContoCorrente(double conto, String nomeProprietario) {
        this.conto = conto;
        this.nomeProprietario = nomeProprietario;
        iban = generateIBAN();
    }

    public double getConto() {
        return conto;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    @Override
    public String toString() {
        return "ContoCorrente{" + "conto=" + conto + ", nomeProprietario=" + nomeProprietario + ", iban=" + iban + '}';
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
    
    private String generateIBAN() {
        StringBuilder ibanBuilder = new StringBuilder("IT");
        Random random = new Random();

        for (int j = 0; j < 2; j++) {
            ibanBuilder.append(random.nextInt(10));
        }
        
        int randomNumber = random.nextInt(26) + 65;
        char letteraCasuale = (char) randomNumber;
        ibanBuilder.append(letteraCasuale);
        
        for (int j = 0; j < 22; j++) {
            ibanBuilder.append(random.nextInt(10));
        }
        
        System.out.println(ibanBuilder);
        return ibanBuilder.toString();
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
