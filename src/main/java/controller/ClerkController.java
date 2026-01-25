/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.ClerkData;
import model.entities.Clerk;
import model.entities.Customer;
import model.entities.User;
import model.entities.UserOperations;

/**
 *
 * @author 50687
 */
public class ClerkController implements UserOperations{
    ClerkData clerkData = new ClerkData();
    
    public void insertClerk(Clerk clerk){
        clerkData.insertClerk(clerk);
    }
    
    public ArrayList<Clerk> getAllClerks(){
        return clerkData.getAllClerks();
    }

    //Si se hubiera creado una instancia de User igual funcionaria ya que
    //identification pertenece a la clase padre. Sin embargo, no es específico,
    //osea que no se especificaría qué user
    @Override
    public User searchUser(String identification) {
        ArrayList<Clerk> clerks = clerkData.getAllClerks();
        
        Clerk clerkToReturn = null;
        for (Clerk clerk : clerks) {
            if (clerk.getIdentification().equalsIgnoreCase(identification)) {
                clerkToReturn = clerk;
            }
        }
        return clerkToReturn;
    }

    @Override
    public User searchUser(User user) {
        ArrayList<Clerk> clerks = clerkData.getAllClerks();
        
        Clerk clerkToReturn = null;
        for (Clerk clerk : clerks) {
            if (clerk.getUsername().equalsIgnoreCase(user.getUsername()) && clerk.getPassword().equals(user.getPassword())) {
                clerkToReturn = clerk;
            }
        }
        return clerkToReturn;
    }

    @Override
    public ArrayList<User> sortUsers(Customer[] allUsers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<User> sortUsers(String identification, User[] allUsers) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
