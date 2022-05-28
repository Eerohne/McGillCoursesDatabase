package courses;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileWriter;
import java.io.IOException;

public class Scrapper {
    public static Course[] courses = new Course[DatabaseBuilder.coursecap];
    private static int counter = 0;
    private static String log = "";

    static void retrieveData(){
        String baseURL = "https://www.mcgill.ca/study/2022-2023/courses/search?page=";

        try{
            for (int i = 0; i <= 516; i++) {
                Document searchPage = Jsoup.connect(baseURL + i).get();

                for(Element courseBox : searchPage.select("div.views-field-field-course-title-long")){
                    String courseURL = "https://www.mcgill.ca" + courseBox.select("a").first().attr("href");

                    courses[counter] = scrapCourse(courseURL);
                    log("Progress : " + counter++);

                    if(counter >= DatabaseBuilder.coursecap) return;
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Course scrapCourse(String url) throws IOException {
        String title = "", id = "", credits = "", description = "", prereq = "", coreq = "", notes = "", faculty = "",
                restrictions = "", terms = "", profs = "";


        Document coursePage = Jsoup.connect(url).get();

        try{
            /*
                Retrieve header with id, title and credits

                Example of header: COMP 402D2 Honours Project in Computer Science and Biology (3 credits)

                ID is the two first words
                Title is between word 2 and the two last words
                Credit is right before the last word
             */
            String header = coursePage.select("h1#page-title").first().text();

            // Retrieve the three pieces of information found above
            String[] headerSplit = header.split(" ");
            id = headerSplit[0] + " " + headerSplit[1]; // Course ID

            for (int i = 2; i < headerSplit.length - 2; i++) {
                title += headerSplit[i] + " "; // Title
            }

            credits = headerSplit[headerSplit.length - 2].replace("(", ""); //Credit
            if(credits.trim().length() > 1) credits = "";
        } catch (Exception e){
            log("Course " + counter + ": An error with Title, credit or ID occured - " + e.getMessage());
        }

        Element body = coursePage.select("div.node-catalog").first(); // Selects the div that contains the rest of the information

        try {
            faculty = body.select("a").first().text(); // Faculty
        } catch (Exception e) {
            log("Course " + counter + ": An error with Faculty occured");
        }

        try {
            description = body.selectFirst("div.content").selectFirst("p").text();
            if(description.contains(":")) description = description.split(":")[1];
        } catch (Exception e) {
            log("Course " + counter + ": An error with Description occured - " + e.getMessage());
        }

        try {
            String termbox = body.selectFirst("p.catalog-terms").text();

            if(termbox.contains("not scheduled")) terms = "Not Scheduled";
            else terms = termbox.split(":")[1];
        } catch (Exception e) {
            log("Course " + counter + ": An error with Terms occured - " + e.getMessage());
        }

        try {
            String profbox = body.selectFirst("p.catalog-instructors").text();

            if(!profbox.contains("no professors")) profs = profbox.split(":")[1];
        } catch (Exception e) {
            log("Course " + counter + ": An error with Profs occured - " + e.getMessage());
        }

        try{
            Element details = body.selectFirst("ul.catalog-notes");

            for (Element element : details.select("li")) {
                String text = element.selectFirst("p").text();

                if (text.startsWith("Prereq")) {
                    prereq += text.split(":")[1] + " | ";    // Get Prerequisites
                } else if (text.startsWith("Coreq")) {
                    coreq += text.split(":")[1] + " | ";     // Get Corequisites
                } else if (text.startsWith("Restr")) {
                    coreq += text.split(":")[1] + " | ";     // Get Restrictions
                } else {
                    notes += text + " | ";                        // Get Additional Notes
                }
            }

            //Format Prereq, Coreq and Notes
            if (!prereq.isEmpty()) prereq = prereq.substring(0, prereq.length() - 2);
            if (!coreq.isEmpty()) coreq = coreq.substring(0, coreq.length() - 2);
            if (!notes.isEmpty()) notes = notes.substring(0, notes.length() - 2);
            if (!restrictions.isEmpty()) restrictions = restrictions.substring(0, restrictions.length() - 2);
        } catch (Exception e) {
            log("Course " + counter + ": An error with Notes occured - " + e.getMessage());
        }

        return new Course(title, id, credits, description, prereq, coreq, notes, faculty, restrictions, terms, profs);
    }

    static void log(String message){
        System.out.println(message);
        log += message + "\n";
    }

    public static void outputLog() throws IOException {
        FileWriter file = new FileWriter("log.log");
        file.write(log);
        file.flush();
    }
}
