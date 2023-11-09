package ch.heig.dai.lab.SMTP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ConnectionHandler {
    private String ip;
    private int port;
    private MailAddress mailAddress;
    private MailContent mailContent;

    public ConnectionHandler(String ip, int port, MailAddress mailAddress, MailContent mailContent){
        this.ip = ip;
        this.port = port;
        this.mailAddress = mailAddress;
        this.mailContent = mailContent;
    }

    public int run(){
        try(
                Socket s = new Socket(ip, port);
                var in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                var out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
        ) {
            String line;
            line = in.readLine();
            System.out.println(line);

            out.write("EHLO rfz.root.sx\n");
            out.flush();

            line = in.readLine();
            System.out.println(line);
            line = in.readLine();
            System.out.println(line);
            line = in.readLine();
            System.out.println(line);

            out.write("MAIL FROM:<"+mailAddress.getFrom()+">\n");
            out.flush();

            line = in.readLine();
            System.out.println(line);

            out.write("RCPT TO:<"+mailAddress.getTo(1)+">\n");
            out.flush();

            line = in.readLine();
            System.out.println(line);
            line = in.readLine();
            System.out.println(line);

            out.write("DATA\n");
            out.flush();

            line = in.readLine();
            System.out.println(line);

            out.write("FROM: <"+mailAddress.getFrom()+">\n");
            out.write("TO: <"+mailAddress.getTo(1)+">\n");
            out.write("DATE: April 1st, 2021\n");
            out.write("SUBJECT: " + mailContent.getSubject() + "\n");
            out.write("\n");
            out.write(mailContent.getContent() + "\n");
            out.write("\r\n");
            out.write(".\r\n");
            out.flush();

            line = in.readLine();
            System.out.println(line);

            out.write("QUIT\n");
            out.flush();

            line = in.readLine();
            System.out.println(line);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 1;
    }
}
