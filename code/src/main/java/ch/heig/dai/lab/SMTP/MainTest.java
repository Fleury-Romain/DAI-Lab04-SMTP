package ch.heig.dai.lab.SMTP;

import java.io.FileNotFoundException;

public class MainTest{
    public static void main(String[] args){
        System.out.println("Hello world !");
        System.out.println("How are you sunshine ?");

        MailContentFileReader mcfr = null;
        try {
            mcfr = new MailContentFileReader("C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\MailingCore.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        mcfr.getMail();
    }
}