/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jmp.service.api;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import jmp.dto.BankCard;
import jmp.dto.Subscription;
import jmp.dto.User;

/**
 *
 * @author Jose_Grandio
 */
public interface Service {

    public void Subscribe(BankCard bankCard);

    public Optional<Subscription> getSubscriptionByBankCardNumber(String bankCardNumber);

    public List<User> getAllUsers();
    
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicate);

    default double getAverageUsersAge() {
        List<User> users = getAllUsers();
        double sum = users.stream().mapToLong(e -> ChronoUnit.YEARS.between(e.getBirthday(), LocalDate.now())).sum();
        double average = sum / users.size();
        return average;
    }

    public static boolean isPayableUser(User user) {

        long years = ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());

        if (years >= 18) {
            return true;
        }

        return false;

    }

}
