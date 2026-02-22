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

    public StaffDataFile() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) file.createNewFile();
    }

    public void insertStaff(Clerk e) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, true))) {
            String type = (e instanceof Administrator) ? "ADMIN" : "CLERK";
            pw.println(type + DELIMITER + e.getIdentification() + DELIMITER + e.getName() + 
                       DELIMITER + e.getUsername() + DELIMITER + e.getPassword() + 
                       DELIMITER + e.getEmployeeCode() + DELIMITER + e.getSchedule() + 
                       DELIMITER + e.getAge());
        }
    }

    public ArrayList<Clerk> getAllStaff() throws IOException {
        ArrayList<Clerk> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(DELIMITER);
                if (p[0].equals("ADMIN")) {
                    list.add(new Administrator(Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7]), null, p[1], p[2], p[3], p[4]));
                } else {
                    list.add(new Clerk(Integer.parseInt(p[5]), p[6], Integer.parseInt(p[7]), null, p[1], p[2], p[3], p[4]));
                }
            }
        }
        return list;
    }
}
