/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jmp.dto;

/**
 *
 * @author Jose_Grandio
 */
public class DebitBankCard extends BankCard {

    public DebitBankCard(String number, User user) {
        super(number, user);
    }

    public DebitBankCard(User user) {
        super(user);
    }

}
