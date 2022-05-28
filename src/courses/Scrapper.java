package courses;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class Scrapper {
    public static Course[] courses = new Course[1];
    private static int counter = 0;

    static Course[] retrieveData(){
        String baseURL = "https://www.mcgill.ca/study/2022-2023/courses/search?page=";

        try{
            for (int i = 0; i <= 516; i++) {
                Document searchPage = Jsoup.connect(baseURL + i).get();

                for(Element courseBox : searchPage.select("div.views-field-field-course-title-long")){
                    String courseURL = "https://www.mcgill.ca" + courseBox.select("a").first().attr("href");

                    courses[counter++] = scrapCourse(courseURL);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        return courses;
    }

    public static Course scrapCourse(String url) throws IOException {
        String title = "", id, credits, description, prereq ="", coreq = "", notes = "", faculty;

        Document coursePage = Jsoup.connect(url).get();

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

        credits = headerSplit[headerSplit.length-2].replace("(", ""); //Credit

        Element body = coursePage.select("div.node-catalog").first(); // Selects the div that contains the rest of the information
        faculty = body.select("a").first().text(); // Faculty

        description = body.select("div.content").first().select("p").first().text().split(":")[1];

        Element details = body.selectFirst("ul.catalog-notes");

        for (Element element : details.select("li")){
            String text = element.selectFirst("p").text();

            if(text.startsWith("Prereq")){
                prereq += text.split(":")[1] + " | ";    // Get Prerequisites
            }
            else if(text.startsWith("Coreq")){
                coreq += text.split(":")[1] + " | ";     // Get Corequisites
            }
            else {
                notes += text + " | ";                        // Get Additional Notes
            }
        }

        //Format Prereq, Coreq and Notes
        if(!prereq.isEmpty()) prereq = prereq.substring(0, prereq.length()-2);
        if(!coreq.isEmpty()) coreq = coreq.substring(0, coreq.length()-2);
        if(!notes.isEmpty()) notes = notes.substring(0, notes.length()-2);



        return new Course(title, id, credits, description, prereq, coreq, notes, faculty);
    }
}
