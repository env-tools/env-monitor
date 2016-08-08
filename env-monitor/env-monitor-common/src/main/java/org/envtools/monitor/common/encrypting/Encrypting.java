package org.envtools.monitor.common.encrypting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by esemenov on 14.07.16.
 */
public class Encrypting {


    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage: Encrypting <masterkey> <algorithm>");
            System.exit(1);
        }

        EncryptionService encryptionService =
                new EncryptionServiceImpl(args[0], args[1]);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                System.out.print("please enter password what should to be encrypted: ");
                System.out.println();

                System.out.println("This is your encrypted password: " + encryptionService.encrypt(reader.readLine()));

                System.out.println("encrypt another one ? (Y\\N) ");

                if (reader.readLine().equalsIgnoreCase("n"))
                    break;
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
