/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

/**
 *
 * @author 50687
 */
public abstract class User {
    private String identification;
    private String name;
    private String username;
    private String password;
    
    public abstract boolean verifyUserLogin(String loginDetails);
    
    //método intensionalmente polimorfico, pero qué vamos a usar si 
    //los usuarios se autentican de formadiferente
    public boolean verifyUserLogin(String[] loginDetails, int id) {
        return username.equals(loginDetails[0]) &&
               password.equals(loginDetails[1]);
    }

    public User() {
    }

    public User(String identification, String name, String username, String password) {
        this.identification = identification;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "identification=" + identification + ", name=" + name + ", username=" + username + ", password=" + password + '}';
    }
    
    
    
}
