/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.util.ArrayList;
import model.entities.Administrator;

/**
 *
 * @author pablo
 */
public class AdministratorData {

    private ArrayList<Administrator> administrators;

    public AdministratorData() {
        this.administrators = new ArrayList<>();
    }

    public void insertAdministrator(Administrator admin) {
        if (admin != null) {
            this.administrators.add(admin);
        }
    }

    public ArrayList<Administrator> getAllAdministrators() {
        if (this.administrators == null) {
            this.administrators = new ArrayList<>();
        }
        return this.administrators;
    }

    public void deleteAdministrator(String identification) {
        this.administrators.removeIf(admin -> admin.getIdentification().equals(identification));
    }

}
