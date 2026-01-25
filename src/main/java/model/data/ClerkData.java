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
     ArrayList<Clerk> clerks = new ArrayList<>();
    
    public void insertClerk(Clerk clerk){
        
        clerks.add(clerk);
    }
    
    public ArrayList<Clerk> getAllClerks(){
        
        return clerks;
    }
}
