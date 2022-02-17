/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jmp.cloud.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jmp.dto.BankCard;
import jmp.dto.Subscription;
import jmp.dto.User;
import jmp.service.api.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Jose_Grandio
 */
public class ServiceImplementation implements Service {

    @Override
    public void Subscribe(BankCard bankCard) {

        Subscription subscription = new Subscription(bankCard.getNumber(), LocalDate.now());
        InsertSubscriptionInDB(subscription);
    }

    public boolean isPayableUser(User user) {
        return Service.isPayableUser(user);
    }

    private void InsertSubscriptionInDB(Subscription subscription) {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement st = conn.createStatement();

            st.execute("INSERT INTO TEST.SUBSCRIPTIONS VALUES('" + subscription.getBankCard()
                    + "', parseDateTime('"
                    + subscription.getStartDate().toString() + "','yyyy-MM-dd'));");

        } catch (java.sql.SQLException ex) {
            printSQLException(ex);
        }
    }

    @Override
    public Optional<Subscription> getSubscriptionByBankCardNumber(String bankCardNumber) {

        Optional<Subscription> subscriptionOpt = Optional.empty();

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT BANKCARD, LOCALDATE FROM"
                     + " TEST.SUBSCRIPTIONS WHERE BANKCARD =?;");) {
            preparedStatement.setString(1, bankCardNumber);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String bankCard = rs.getString("BANKCARD");
                Date date = rs.getDate("LOCALDATE");
                LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
                Subscription sub = new Subscription(bankCard, localDate);
                subscriptionOpt = Optional.of(sub);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        try {
            return Optional.ofNullable(subscriptionOpt).orElseThrow(() -> new OptionalCustomException("Subscription not founded"));
        } catch (OptionalCustomException ex) {
            System.out.println(ex.toString());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        User user = null;

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT NAME, SURNAME, BIRTHDAY FROM TEST.USERS;");) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("NAME");
                String surname = rs.getString("SURNAME");
                Date birthday = rs.getDate("BIRTHDAY");
                LocalDate localBirthday = new java.sql.Date(birthday.getTime()).toLocalDate();
                user = new User(name, surname, localBirthday);
                users.add(user);
                user = null;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return users.stream().collect(Collectors.toUnmodifiableList());

    }

    private static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicate) {

        List<Subscription> subs = new ArrayList<>();
        Subscription sub = null;

        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT BANKCARD, LOCALDATE, FROM TEST.SUBSCRIPTIONS;");) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("BANKCARD");
                Date lcldate = rs.getDate("LOCALDATE");
                LocalDate localDate = new java.sql.Date(lcldate.getTime()).toLocalDate();
                sub = new Subscription(name, localDate);
                subs.add(sub);
                sub = null;
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return subs.stream()
                .filter(predicate)
                .collect(Collectors.<Subscription>toList());
    }

    @Override
    public double getAverageUsersAge() {
        return Service.super.getAverageUsersAge();
    }

    public static void InsertUserInDB(User user) {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement st = conn.createStatement();

            st.execute("INSERT INTO TEST.USERS VALUES('" + user.getName()
                    + "', '" + user.getSurname() + "', parseDateTime('"
                    + user.getBirthday().toString() + "','yyyy-MM-dd'));");

        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

}
