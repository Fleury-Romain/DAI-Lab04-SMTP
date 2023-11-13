package ch.heig.dai.lab.SMTP;

public class MainTest {
    public static void main(String[] args) {
        args = new String[]{
                "--ip", "localhost",
                "-p", "1025",
                "-g", "3",
                "-m", "1",
                "-ma", "C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\mailAddress.txt",
                "-mc", "C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\MailingCore.txt"
        };
        CmdHandler cmdh = new CmdHandler(args);

        //cmdh.run();

       /* try (
                MailContentFileReader mcfr = new MailContentFileReader("C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\MailingCore.txt");
                MailAddressFileReader mafr = new MailAddressFileReader("C:\\HEIG\\Semestre 3\\DAI\\Laboratoire\\DAI-Lab04-SMTP\\Data\\mailAddress.txt");
        ) {
            // Code fonctionnel pour la connection smtp
            MailContent mc = new MailContent(mcfr);
            MailAddress ma = new MailAddress(mafr);


            ConnectionHandler ch = new ConnectionHandler("localhost", 1025, ma, mc);
            ch.run();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}