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
            int flag = 0;

            while (!(s.isClosed())) {
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    if(!line.contains("-")){
                        flag++;
                        break;
                    }
                }

                if(line.contains("221")){break;}

                SMTPhandler(out, flag);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 1;
    }

    private void send(BufferedWriter out, String msg) throws IOException {
        // Vérification de la présence du
        // caractère  de fin de ligne
        if(!msg.contains("\n")){msg += "\n";}
        out.write(msg);
        out.flush();
    }

    private void SMTPhandler(BufferedWriter out, int flag) throws IOException {
        switch (flag) {
            case 1:
                send(out, "EHLO rfz.root.sx\n");
                break;
            case 2:
                send(out, "MAIL FROM:<" + mailAddress.getFrom() + ">\n");
                break;
            case 3:
                send(out, "RCPT TO:<" + mailAddress.getTo(1) + ">\n");
                break;
            case 4:
                send(out, "DATA\n");
                break;
            case 5:
                int id = 8; // controle de la valeur du sujet et du contenu
                send(out, "FROM: <" + mailAddress.getFrom() + ">\n");
                send(out, "TO: <" + mailAddress.getTo(1) + ">\n"); // ajouter de nouveaux destinataire
                send(out, "DATE: April 1st, 2021\n");
                send(out, "SUBJECT: " + mailContent.getSubject(id) + "\n\n");
                send(out, mailContent.getContent(id) + "\n");
                System.out.println("CONTENT : " + mailContent.getContent(id));
                send(out, "\r\n.\r\n");
                break;
            default:
                send(out, "QUIT\n");
                break;
        }
    }
}
