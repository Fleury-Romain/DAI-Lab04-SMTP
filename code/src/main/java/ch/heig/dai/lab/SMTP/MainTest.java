package ch.heig.dai.lab.SMTP;

import java.io.FileNotFoundException;

public class MainTest{
    public static void main(String[] args){
        try (
                MailContentFileReader mcfr = new MailContentFileReader("C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\MailingCore.txt");
                //MailContentFileReader mafr = new MailAddressFileReader(/* FILEPATH */);
        ){
            MailContent mc = new MailContent(mcfr.getMail());
            MailAddress ma = new MailAddress(3, "romain.fleury@heig-vd.ch", new String[]{"t411galas@gmail.com", "r.fleury.1400@gmail.com"});

        MailContentFileReader mcfr = null;
        try {
            mcfr = new MailContentFileReader("C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\MailingCore.txt");
            String[] test = mcfr.getMail();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}