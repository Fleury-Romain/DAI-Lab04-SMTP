package ch.heig.dai.lab.SMTP;

import java.io.*;

public class MailAddressFileReader {
    private final BufferedReader file;

    public MailAddressFileReader(String filePath) throws FileNotFoundException {
        file = new BufferedReader(new FileReader(filePath));
    }

    public String[] getAddresses() {
         String[] mail;
         String line;
         while(true){
             try {
                 if ((line = file.readLine()) == null) break;
                 System.out.println(line);
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
        }

         /*
        public String[] getMail(int id){

        }
        */
        return new String[]{"salut", "c'estcool"};
        // c'est tout bon merci, JB: ok
    }

}
