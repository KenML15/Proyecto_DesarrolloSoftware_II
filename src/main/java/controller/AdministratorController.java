/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.AdministratorData;
import model.data.StaffDataFile;
import model.entities.Administrator;
import model.entities.Clerk;
import model.entities.User;
import model.entities.UserOperations;

public class AdministratorController implements UserOperations {

    private static AdministratorData adminData = new AdministratorData();

    public void loadFromDisk() {
        StaffDataFile file = new StaffDataFile();
        ArrayList<Clerk> fromFile = file.getAllStaff();
        adminData.getAllAdministrators().clear();
        for (Clerk c : fromFile) {
            if (c instanceof Administrator) {
                adminData.insertAdministrator((Administrator) c);
            }
        }
    }

    public boolean insertAdministrator(Administrator admin) {
        if (admin == null) {
            return false;
        }
        adminData.insertAdministrator(admin);
        return syncDisk();
    }

    public boolean deleteAdministrator(String identification) {
        adminData.deleteAdministrator(identification);
        return syncDisk();
    }

    private boolean syncDisk() {
        StaffDataFile file = new StaffDataFile();
        ArrayList<Clerk> all = new ArrayList<>();
        all.addAll(adminData.getAllAdministrators());
        all.addAll(ClerkController.clerkData.getAllClerks());
        return file.saveAll(all);
    }

    public ArrayList<Administrator> getAllAdministrators() {
        return adminData.getAllAdministrators();
    }

    @Override
    public User searchUser(String id) {
        for (Administrator a : adminData.getAllAdministrators()) {
            if (a.getIdentification().equals(id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public User searchUser(User u) {
        return null;
    }

    @Override
    public ArrayList<User> sortUsers(model.entities.Customer[] u) {
        return null;
    }

    @Override
    public ArrayList<User> sortUsers(String id, User[] u) {
        return null;
    }
}
