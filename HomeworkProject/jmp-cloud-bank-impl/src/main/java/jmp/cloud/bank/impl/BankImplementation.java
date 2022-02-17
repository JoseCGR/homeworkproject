/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jmp.cloud.bank.impl;

import java.util.Random;
import jmp.bank.api.Bank;
import jmp.dto.BankCard;
import jmp.dto.BankCardType;
import jmp.dto.CreditBankCard;
import jmp.dto.DebitBankCard;
import jmp.dto.User;
import java.sql.*;
import java.util.function.BiFunction;

/**
 *
 * @author Jose_Grandio
 */
public class BankImplementation implements Bank {

//    @Override
//    public BankCard createBankCard(User user, BankCardType type) {
//        
//        if (type.equals(BankCardType.CREDIT)) {
//            Random r = new Random();
//            int randomInt = r.nextInt(900) + 100;
//            BankCard creditBankCard = new CreditBankCard("123123123" + randomInt, user);
//            
//            InsertBankCardInDB(creditBankCard, type);
//            
//            return creditBankCard;
//        } else if (type.equals(BankCardType.DEBIT)) {
//            Random r = new Random();
//            int randomInt = r.nextInt(900) + 100;
//            BankCard debitBankCard = new DebitBankCard("321321321" + randomInt, user);
//            
//            InsertBankCardInDB(debitBankCard, type);
//            
//            return debitBankCard;
//        } else {
//            return null;
//        }
//    }
    
    @Override
    public BankCard createBankCard(User user, BankCardType type) {

        if (type.equals(BankCardType.CREDIT)) {
            Random r = new Random();
            int randomInt = r.nextInt(900) + 100;

            BiFunction<String, User, BankCard> bnk = CreditBankCard::new;

            InsertBankCardInDB(bnk.apply("123123123" + randomInt, user), type);

            return bnk.apply("123123123" + randomInt, user);
        } else if (type.equals(BankCardType.DEBIT)) {
            Random r = new Random();
            int randomInt = r.nextInt(900) + 100;
            BiFunction<String, User, BankCard> bnk = CreditBankCard::new;

            InsertBankCardInDB(bnk.apply("321321321" + randomInt, user), type);

            return bnk.apply("123123123" + randomInt, user);
        } else {
            return null;
        }
    }

    private void InsertBankCardInDB(BankCard bankCard, BankCardType type) {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement st = conn.createStatement();

            if (type.equals(BankCardType.CREDIT)) {

                st.execute("INSERT INTO TEST.BANKCARDS VALUES('" + bankCard.getNumber()
                        + "', '" + bankCard.getUser().getName() + "', 'C');");

            } else {

                st.execute("INSERT INTO TEST.BANKCARDS VALUES('" + bankCard.getNumber()
                        + "', '" + bankCard.getUser().getName() + "', 'D');");

            }

            conn.close();

        } catch (java.sql.SQLException ex) {
            //Change the bankCard value and try inserting again
            Random r = new Random();
            int randomInt = r.nextInt(900) + 100;
            bankCard.setNumber(bankCard.getNumber().substring(0, 9) + randomInt);
            InsertBankCardInDB(bankCard, type);
        }

    }

}
