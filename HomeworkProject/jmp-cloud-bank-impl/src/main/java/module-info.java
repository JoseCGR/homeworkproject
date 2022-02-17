/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module jmp.cloud.bank.impl {
    requires transitive jmp.bank.api;
    requires jmp.dto;
    requires java.sql;
            
    provides jmp.bank.api.Bank with jmp.cloud.bank.impl.BankImplementation;
    
    exports jmp.cloud.bank.impl;
}
