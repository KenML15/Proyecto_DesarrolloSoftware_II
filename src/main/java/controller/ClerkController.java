/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.data.ClerkData;
import model.data.StaffDataFile;
import model.entities.Clerk;
import model.entities.Administrator;
import model.entities.User;
import model.entities.UserOperations;

public class ClerkController implements UserOperations {

    static ClerkData clerkData = new ClerkData();

    public void loadFromDisk() {
        StaffDataFile file = new StaffDataFile();
        ArrayList<Clerk> fromFile = file.getAllStaff();
        clerkData.getAllClerks().clear();
        for (Clerk c : fromFile) {
            if (!(c instanceof Administrator)) {
                clerkData.insertClerk(c);
            }
        }
    }

    public boolean insertClerk(Clerk clerk) {
        if (clerk == null) {
            return false;
        }
        clerkData.insertClerk(clerk);
        return syncDisk();
    }

    public boolean deleteClerk(String identification) {
        clerkData.deleteClerk(identification);
        return syncDisk();
    }

    private boolean syncDisk() {
        StaffDataFile file = new StaffDataFile();
        ArrayList<Clerk> all = new ArrayList<>();
        all.addAll(new AdministratorController().getAllAdministrators());
        all.addAll(clerkData.getAllClerks());
        return file.saveAll(all);
    }

    public ArrayList<Clerk> getAllClerks() {
        return clerkData.getAllClerks();
    }

    @Override
    public User searchUser(String id) {
        for (Clerk c : clerkData.getAllClerks()) {
            if (c.getIdentification().equals(id)) {
                return c;
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
