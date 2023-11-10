package ch.heig.dai.lab.SMTP;

import java.io.*;

public class MailContentFileReader implements AutoCloseable {
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

                String[] subject = line.split("subject:");
                if(subject.length > 1){
                    mail[0] = subject[1];
                }

                String[] content = line.split("content:");
                if(content.length > 1){
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

        return new String[]{"salut", "c'est cool"};
    }

    @Override
    public void close() throws Exception {
        file.close();
    }
}
