package courses;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Main class to build the Data files
 */
public class DatabaseBuilder {
    static final String fileName = "courses2425";
    static final int coursecap = 10477;
    static final int pagecap = 524;
    static final String year = "2024-2025";
    static final String school = "McGill";


    public static void main(String[] args) throws IOException {
        Scrapper.retrieveData();

        try {
            Scrapper.outputLog();
            makeJSON();
            makeTSV();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void makeJSON() throws IOException{
        JsonArray courseArray = new JsonArray();
        for (int i = 0; i < Scrapper.courses.length; i++) {
            if(Scrapper.courses[i] == null) break;
            courseArray.add(Scrapper.courses[i].toJSON());
        }

        JsonObject db = new JsonObject();
        db.put("school", school);
        db.put("year", year);
        db.put("courses", courseArray);

        FileWriter file = new FileWriter(fileName + ".json");
        file.write(db.toJson());
        file.flush();
    }

    private static void makeTSV() throws IOException{
        String db = "title\tid\tcredits\tdescription\tprereq\tcoreq\tnotes\tfaculty\trestrictions\tterms\tprofs\n";
        for (int i = 0; i < Scrapper.courses.length; i++) {
            if(Scrapper.courses[i] == null) break;
            db += Scrapper.courses[i].toTSV() + "\n";
        }

        FileWriter file = new FileWriter(fileName + ".tsv");
        file.write(db);
        file.flush();
    }
}
