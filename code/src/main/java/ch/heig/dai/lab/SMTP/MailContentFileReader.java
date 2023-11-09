package ch.heig.dai.lab.SMTP;

import java.io.*;

public class MailContentFileReader {
    private BufferedReader file;

    MailContentFileReader(String filePath) throws FileNotFoundException {
        file = new BufferedReader(new FileReader(filePath));
    }

    public String[] getMail() {
        String[] mail = new String[2];
        String line;
        while(true){
            try {
                if (!((line = file.readLine()) != null)) break;
                if(line.contains("#") || line.equals("\n")) continue;

                //System.out.println(line);

                String[] subject = line.split("subject:");
                if(subject.length > 1){
                    System.out.println(subject[1]);
                    mail[0] = subject[1];
                }

                String[] content = line.split("content:");
                if(content.length > 1){
                    System.out.println(content[1]);
                    mail[1] = content[1];
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return mail;
    }

    public String[] getMail(int id){

        return new String[]{"salut", "c'estcool"};
    }
}
