/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jmp.bank.api;

import jmp.dto.BankCard;
import jmp.dto.BankCardType;
import jmp.dto.User;

/**
 *
 * @author Jose_Grandio
 */
public interface Bank {
    
    public BankCard createBankCard(User user, BankCardType type);
    
}
