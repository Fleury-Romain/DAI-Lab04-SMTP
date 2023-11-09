package ch.heig.dai.lab.SMTP;

import java.io.FileNotFoundException;

public class MainTest{
    public static void main(String[] args){
        System.out.println("Hello world !");
        System.out.println("How are you sunshine ?");

        MailContentFileReader mcfr = null;
        try {
            mcfr = new MailContentFileReader("C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\MailingCore.txt");
            String[] test = mcfr.getMail();

            MailContent mc = new MailContent(test[0], test[1]);

            System.out.println(mc.getSubject());
            System.out.println(mc.getContent());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}