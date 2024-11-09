package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.example.db.DB;
import org.example.utils.Input;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static org.example.db.DB.users;

public class AuthController {

    public static void signIn() throws MessagingException {
        String userLogin = Input.input_STRING("Enter user name or email: ");
        String password = Input.input_STRING("Enter password: ");
        User user = null;

        user = findUser(userLogin, password);
        if (user != null) {
            showUserDisplay(user);
        } else {
            System.out.println("\u001B[31m🔴 Invalid username/email or password.\u001B[0m");
        }
    }

    private static void showUserDisplay(User user) throws MessagingException {
        while (true) {
            System.out.println("--------------------------------------");
            System.out.println("🟩 Welcome " + user.getUserName());
            ArrayList<User> tempUsers = new ArrayList<>(users);
            tempUsers.removeIf(user1 -> user1.equals(user));
            int number = 1;
            for (User tempUser : tempUsers) {
                System.out.println(number++ + ".USERNAME: " + tempUser.getUserName() + ", EMAIL: " + tempUser.getEmail());
            }
            String indexUser = Input.input_STRING("""
                    0.LOg Out
                    00.ALL USERS
                    choose: """);

            if (indexUser.equals("00")) {
                if (!tempUsers.isEmpty()) {
                    String messageTitle = Input.input_STRING("Enter message title: ");
                    String messageText = Input.input_STRING("Enter message text: ");
                    sendMessagesToEmails(tempUsers, messageTitle, messageText);
                } else {
                    System.out.println("‼️Sizda do'stlar mavjud emas!");
                }
            } else if (indexUser.equals("0")) {
                Main.startprogramm();
            } else {
                String messageTitle = Input.input_STRING("Enter message title: ");
                String messageText = Input.input_STRING("Enter message text: ");
                int index = Integer.parseInt(indexUser) - 1;
                User toUser = tempUsers.get(index);
                sendMessageToEmail(toUser, messageTitle, messageText);
            }
        }
    }

    private static User findUser(String userLogin, String password) {
        return users.stream()
                .filter(user -> (user.getUserName().equalsIgnoreCase(userLogin) || user.getEmail().equalsIgnoreCase(userLogin)) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public static void registration() throws MessagingException {
        Random random = new Random();
        int code = random.nextInt(10000, 99999);
        String username = "";
        while (true) {
            String temp = Input.input_STRING("Enter Username: ");
            if (checkUsername(temp)) {
                username = temp;
                break;
            } else {
                System.out.println("\u001B[31mBu username band qilingan, Iltimos boshqa username kiriting!\u001B[0m");
            }
        }

        String email = Input.input_STRING("Enter email adress: ");
        System.out.println("📬Send code your email, please verification code.");
        System.out.println("loading...");

        User user = User.builder()
                .userName(username)
                .email(email)
                .code(code)
                .build();
        sendCodeToEmail(user);
        checkCode(user);

    }

    private static boolean checkUsername(String username) {
        return !users.stream()
                .anyMatch(user -> user.getUserName().equalsIgnoreCase(username));
    }


    private static void checkCode(User user) {
        System.out.println(user.getCode());
        Integer gmailCode = Input.input_INT("Enter code: ");
        if (gmailCode.equals(user.getCode())) {
            user.setPassword(Input.input_STRING("Enter User password: "));
            users.add(user);
            System.out.println("✅Sucsess added User");
        } else {
            System.out.println("🔴Failed code");
            checkCode(user);
        }
    }


    private static void sendCodeToEmail(User user) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        String username = "jalol2622@gmail.com";
        String password = "zqzotcbjthndozvf";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        message.setSubject("Kodni hech kimga berma.");
        Multipart multipart = new MimeMultipart();
        BodyPart bodyPart1 = new MimeBodyPart();
        bodyPart1.setContent("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Title</title>
                </head>
                <body>
                <h4 style="color: red">Bu kodni hech kimga bermang!</h4>
                <h1> code: %s </h1>
                </body>
                </html>""".formatted(user.getCode()), "text/html");
        multipart.addBodyPart(bodyPart1);
        message.setContent(multipart);
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    private static void sendMessagesToEmails(List<User> users, String titleMessage, String textMessage) {
        users.forEach(user -> {
            try {
                sendMessageToEmail(user, titleMessage, textMessage);
            } catch (MessagingException e) {
                System.out.println("‼️" + user.getEmail() + " bilan Xatolik yuz berdi: " + e);
            }
        });
    }


    private static void sendMessageToEmail(User user, String titleMessage, String textMessage) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        String username = "jalol2622@gmail.com";
        String password = "zqzotcbjthndozvf";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        message.setSubject(titleMessage);
        message.setText(textMessage);

        try {
            Transport.send(message);
            System.out.println("✅" + user.getEmail() + " ga Email muvaffaqiyatli yuborildi!");
        } catch (MessagingException e) {
            throw new RuntimeException("‼️" + user.getEmail() + " bilan Xatolik yuz berdi: " + e);
        }
    }


}
