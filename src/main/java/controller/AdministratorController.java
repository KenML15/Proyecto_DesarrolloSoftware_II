/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.AdministratorData;
import model.entities.Administrator;
import model.entities.Customer;
import model.entities.User;
import model.entities.UserOperations;

/**
 *
 * @author pablo
 */
public class AdministratorController implements UserOperations {

    private static AdministratorData adminData = new AdministratorData();

    public void insertAdministrator(Administrator admin) {
        if (admin != null) {
            adminData.insertAdministrator(admin);
            System.out.println("LOG: Administrador insertado correctamente en AdministratorData.");
        }
    }

    public User searchUser(String username, String password) {
        ArrayList<Administrator> admins = adminData.getAllAdministrators();

        for (Administrator admin : admins) {
            if (admin.getUsername().equalsIgnoreCase(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    public ArrayList<Administrator> getAllAdministrators() {
        return adminData.getAllAdministrators();
    }

    public void deleteAdministrator(String identification) {
        adminData.deleteAdministrator(identification);
    }

    @Override
    public User searchUser(String identification) {
        for (Administrator admin : adminData.getAllAdministrators()) {
            if (admin.getIdentification().equals(identification)) {
                return admin;
            }
        }
        return null;
    }

    @Override
    public User searchUser(User user) {
        return searchUser(user.getUsername(), user.getPassword());
    }

    @Override
    public ArrayList<User> sortUsers(Customer[] allUsers) {
        throw new UnsupportedOperationException("Operación no requerida para Admin en este momento.");
    }

    @Override
    public ArrayList<User> sortUsers(String identification, User[] allUsers) {
        throw new UnsupportedOperationException("Operación no requerida para Admin en este momento.");

    }
}
