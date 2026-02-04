/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Clerk;

/**
 *
 * @author 50687
 */
public class ClerkData {

    private ArrayList<Clerk> clerks = new ArrayList<>();
    
    // Método para insertar
    public void insertClerk(Clerk clerk){
        if (clerk != null) {
            clerks.add(clerk);
        }
    }
    
    // Método para obtener todos
    public ArrayList<Clerk> getAllClerks() {
        if (this.clerks == null) {
            this.clerks = new ArrayList<>();
        }
        return this.clerks;
    }
}
