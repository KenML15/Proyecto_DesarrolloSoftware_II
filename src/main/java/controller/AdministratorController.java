/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.entities.Customer;
import model.entities.User;
import model.entities.UserOperations;

/**
 *
 * @author 50687
 */
public class AdministratorController implements UserOperations{

    @Override
    public User searchUser(String identification) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User searchUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
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
