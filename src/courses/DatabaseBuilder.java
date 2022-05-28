package courses;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import java.io.FileWriter;
import java.io.IOException;

public class DatabaseBuilder {
    static final String fileName = "mcgillCourses";
    static final int cap = 300;

    public static void main(String[] args) throws IOException {
        Scrapper.retrieveData();

        try {
            makeJSON();
            makeCSV();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void makeJSON() throws IOException{
        JsonArray courseArray = new JsonArray();
        for (int i = 0; i < Scrapper.courses.length; i++) {
            courseArray.add(Scrapper.courses[i].toJSON());
        }

        JsonObject db = new JsonObject();
        db.put("courses", courseArray);

        FileWriter file = new FileWriter(fileName + ".json");
        file.write(db.toJson());
        file.flush();
    }

    private static void makeCSV() throws IOException{
        String db = "";
        for (int i = 0; i < Scrapper.courses.length; i++) {
            db += Scrapper.courses[i].toCSV() + "\n";
        }

        FileWriter file = new FileWriter(fileName + ".csv");
        file.write(db);
        file.flush();
    }
}
