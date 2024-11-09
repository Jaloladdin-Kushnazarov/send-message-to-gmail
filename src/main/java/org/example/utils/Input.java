package org.example.utils;

import java.util.Scanner;

public interface Input {
    static Integer input_INT(String msg) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.println("‼️Noto'g'ri kiritildi. Iltimos, butun son kiriting.");
            scanner.next();
            System.out.print(msg);
        }
        return scanner.nextInt();
    }

    static String input_STRING(String msg) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(msg);
        return scanner.nextLine();
    }
}
