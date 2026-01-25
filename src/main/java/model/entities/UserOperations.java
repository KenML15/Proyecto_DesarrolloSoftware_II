/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model.entities;

import java.util.ArrayList;

/**
 *
 * @author 50687
 */
public interface UserOperations {
    public User searchUser(String identification);

    public User searchUser(User user);

    public ArrayList<User> sortUsers(Customer allUsers[]); 
    public ArrayList<User> sortUsers(String identification, User allUsers[]);
    
 
}
