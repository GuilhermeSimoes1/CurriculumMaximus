/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.curriculumdigital;

/**
 *
 * @author Gui and Rodrigo
 */
public class Hash {
    
    // Método para converter um inteiro em uma representação hexadecimal
     public static String toHexString(int n) {
        return Integer.toHexString(n).toUpperCase();
    }
     
    // Método para gerar um hash a partir de uma string
    // Converte a string em um hash code e depois representa-a em hexadecimal
    public static String getHash(String data) {
        return toHexString(Math.abs(data.hashCode()));
    }
    
        
}
