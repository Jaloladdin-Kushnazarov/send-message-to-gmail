package org.example;
import jakarta.mail.*;
import org.example.utils.Input;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws MessagingException, IOException {


        System.out.println("**************** WELCOME ****************");
        startprogramm();


    }

    public static void startprogramm() throws MessagingException {
        while (true) {
            displey();
            switch (Input.input_INT("choose: ")) {
                case 1 -> AuthController.registration();
                case 2 -> AuthController.signIn();
            }
        }
    }

    private static void displey() {
        System.out.println("""
                -------------------------------------------------
                1.Registration"
                2.Sign in
                """);
    }



}