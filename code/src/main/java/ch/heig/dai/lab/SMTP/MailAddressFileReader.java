package ch.heig.dai.lab.SMTP;

import java.io.*;
import java.util.ArrayList;

public class MailAddressFileReader {
    private final BufferedReader file;
    public MailAddressFileReader(String filePath) throws FileNotFoundException {
        file = new BufferedReader(new FileReader(filePath));
    }

    public ArrayList<String> getAddress() {
        /*
         read line per line file, detect # as the beginning of a new group and what follows the # sign as the name of the group, sender: and what
         follows as the e-mail address of the sender, and the each line starting with receiver: and what
         follows as the e-mail address of the receiver (one receiver per line).
         finish when a line with only \n is found
         */
        System.out.println("MailAddressFileReader");
        ArrayList<String> array = new ArrayList<>();
        String line;
        int i = 0;
        while(true){
            try {
                if ((line = file.readLine()) == null) break;
                if(line.equals("\n")) break;
                if(line.contains("#")){
                    array.add(line.split("#")[1]);
                    System.out.println("group: " + line);
                }
                if(line.contains("sender:")){
                    array.add(line.split("sender:")[1]);
                    System.out.println("sender: " + line);
                }
                if(line.contains("receiver:")){
                    array.add(line.split("receiver:")[1]);
                    System.out.println("receiver: " + line);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return array;
    }


    /*
    public String[] getAddress() {
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
        return new String[]{"salut", "c'estcool"};
    }
    */

}
