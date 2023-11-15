package ch.heig.dai.lab.SMTP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MailContentFileReader implements AutoCloseable {
    private BufferedReader file;
    private final List<String> subjects;
    private final List<String> contents;

    MailContentFileReader(String filePath) throws IOException {
        subjects = retrieveSubjects(filePath);
        contents = retrieveContents(filePath);
    }

    private List<String> retrieveSubjects(String filePath) throws IOException {
        file = new BufferedReader(new FileReader(filePath));
        String line;
        List<String> subjects = new ArrayList<>();
        while((line = file.readLine()) != null) {
            if (line.split(" ")[0].equals("#") || line.equals("\n")) continue;

            if(line.contains("subject:")) {
                String[] subject = line.split("subject:");
                if (subject.length > 1) {
                    subjects.add(subject[1]);
                }
            }
        }
        return subjects;
    }

    private List<String> retrieveContents(String filePath) throws IOException {
        file = new BufferedReader(new FileReader(filePath));
        String line;
        List<String> contents = new ArrayList<>();
        while((line = file.readLine()) != null) {
            if (line.split(" ")[0].equals("#") || line.equals("\n")) continue;
            if(line.contains("content:")) {
                String[] content = line.split("content:");
                if (content.length > 1) {
                    contents.add(content[1]);
                }
            }
        }
        return contents;
    }

    protected List<String> getSubjects(){
        return subjects;
    }

    protected List<String> getContents(){
        return contents;
    }
    @Override
    public void close() throws Exception {
        file.close();
    }
}
