package jmp.application;


import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import jmp.cloud.bank.impl.BankImplementation;
import jmp.cloud.service.impl.ServiceImplementation;
import jmp.dto.BankCard;
import jmp.dto.BankCardType;
import jmp.dto.Subscription;
import jmp.dto.User;
import jmp.service.api.Service;

public class AppMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Creating DB Schema
        createSchema();

        //Creating services
        var bankImp = new BankImplementation();
        var serviceImp = new ServiceImplementation();

        ServiceLoader<Service> loader = ServiceLoader.load(Service.class);

        Map<String, Service> services = new HashMap<>();
        for (Service service : loader) {
            System.out.println("I've found a service called '" + service.getClass().toString() + "' !");
            services.put(service.getClass().toString(), service);
        }

        System.out.println("Found " + services.size() + " services!");

        //Creating and adding users to DB
        var user1 = new User("Juancho", "Perez", LocalDate.of(1970, Month.AUGUST, 26));
        serviceImp.InsertUserInDB(user1);

        var user2 = new User("Jose", "Carlos", LocalDate.of(1996, Month.JUNE, 14));
        serviceImp.InsertUserInDB(user2);

        var user3 = new User("Fortino", "Eduardo", LocalDate.of(1996, Month.FEBRUARY, 20));
        serviceImp.InsertUserInDB(user3);

        //Creating and adding bankCards to DB with BankImplementation
        var bankCard1 = bankImp.createBankCard(user1, BankCardType.DEBIT);
        var bankCard2 = bankImp.createBankCard(user2, BankCardType.CREDIT);

        //Creating and adding Subscription to DB with ServiceImplementation
        serviceImp.Subscribe(bankCard1);
        serviceImp.Subscribe(bankCard2);

        //Getting a subscription from DB with ServiceImplementation
        Optional<Subscription> optional = serviceImp.getSubscriptionByBankCardNumber(bankCard1.getNumber());

        if (optional.isEmpty()) {
            System.out.println("Bank Card number not found");
        } else {
            System.out.println("Bank Card number founded: " + optional.get().getBankCard());
        }

        //Getting all users
        var users = serviceImp.getAllUsers();
        users.stream().forEach(e -> System.out.println(e.getName() + " " + e.getSurname() + " " + e.getBirthday()));

        //Getting Average age of users
        DecimalFormat decimalForm = new DecimalFormat("0.00");
        System.out.println("Average age of users: " + decimalForm.format(serviceImp.getAverageUsersAge()));

        //Evaluating if a user is a payable user (over 18 years old)
        System.out.println(user1.getName() + ", born in " + user1.getBirthday() + " is a payable user: " + serviceImp.isPayableUser(user1));
        System.out.println(user2.getName() + ", born in " + user2.getBirthday() + " is a payable user: " + serviceImp.isPayableUser(user2));
        System.out.println(user3.getName() + ", born in " + user3.getBirthday() + " is a payable user: " + serviceImp.isPayableUser(user3));

        //Creating a Predicate<Subscription> for using the method getAllSubscriptionsByCondition
        Predicate<Subscription> predicate = i -> (i.getStartDate().getYear() > 2023);
        var subscriptions = serviceImp.getAllSubscriptionsByCondition(predicate);

        subscriptions.stream().map(e -> e.getBankCard() + e.getStartDate()).forEach(System.out::println);
    }

    public static void createSchema() {
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            Statement st = conn.createStatement();
            st.execute("CREATE SCHEMA IF NOT EXISTS TEST");
            st.execute("CREATE TABLE IF NOT EXISTS TEST.users (name VARCHAR(50) NOT NULL,"
                    + "surname VARCHAR(20) NOT NULL, birthday DATE)");
            st.execute("CREATE TABLE IF NOT EXISTS TEST.subscriptions (bankCard VARCHAR(50) NOT NULL,"
                    + " localDate DATE)");
            st.execute("CREATE TABLE IF NOT EXISTS TEST.bankCardS (number VARCHAR(50) NOT NULL,"
                    + "username VARCHAR(50) NOT NULL, bankCardType VARCHAR(1) NOT NULL)");
            //D for Debit, C for Credit

            st.close();
            conn.close();
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
    }

}
