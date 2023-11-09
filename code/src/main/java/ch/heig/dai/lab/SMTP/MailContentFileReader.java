package ch.heig.dai.lab.SMTP;

import java.io.*;

public class MailContentFileReader {
    private BufferedReader file;
    
    MailContentFileReader(String filePath) throws FileNotFoundException {
        file = new BufferedReader(new FileReader(filePath));
    }

    public String[] getMail() {
        String[] mail;
        String line;
        while(true){
            try {
                if (!((line = file.readLine()) != null)) break;
                if(line.contains("#") || line.equals("\n")) break;
                line.split()


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new String[]{"salut", "c'est cool"};
    }

    public String[] getMail(int id){

        return new String[]{"salut", "c'estcool"};
    }
}
