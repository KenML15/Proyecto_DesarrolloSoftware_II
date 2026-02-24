/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.data;

import java.io.*;
import java.util.ArrayList;
import model.entities.Clerk;
import model.entities.Administrator;

public class StaffDataFile {

    private final String fileName = "Staff.txt";
    private static final String DELIMITER = ";";

    public StaffDataFile() {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar archivo.");
        }
    }

    public boolean saveAll(ArrayList<Clerk> allStaff) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, false))) {
            for (Clerk e : allStaff) {
                String type = (e instanceof Administrator) ? "ADMIN" : "CLERK";
                pw.println(type + DELIMITER + e.getIdentification() + DELIMITER + e.getName()
                        + DELIMITER + e.getUsername() + DELIMITER + e.getPassword()
                        + DELIMITER + e.getEmployeeCode() + DELIMITER + e.getSchedule()
                        + DELIMITER + e.getAge());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public ArrayList<Clerk> getAllStaff() {
        ArrayList<Clerk> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(DELIMITER);
                if (p.length < 8) {
                    continue;
                }
                if (p[0].equals("ADMIN")) {
                    list.add(new Administrator(Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7]), null, p[1], p[2], p[3], p[4]));
                } else {
                    list.add(new Clerk(Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7]), null, p[1], p[2], p[3], p[4]));
                }
            }
        } catch (Exception e) {

        }
        return list;
    }
}
