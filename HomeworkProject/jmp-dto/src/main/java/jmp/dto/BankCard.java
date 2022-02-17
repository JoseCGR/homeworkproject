/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jmp.dto;

/**
 *
 * @author Jose_Grandio
 */
public class BankCard {
        
    private String number;
    private User user;

    public BankCard(String number, User user) {
        this.number = number;
        this.user = user;
    }
    
        public BankCard(User user) {
        this.user = user;
    }
    
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    
    
}
