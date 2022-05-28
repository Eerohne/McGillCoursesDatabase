package courses;

import com.github.cliftonlabs.json_simple.JsonObject;

/**
 * Holds all timeless data for a given course at McGill
 */
public class Course {
    String title, id, credits, description, prereq, coreq, notes, faculty;

    public Course(String title, String id, String credits, String description, String prereq,
                  String coreq, String notes, String faculty) {
        this.title = title.trim();
        this.id = id.trim();
        this.credits = credits.trim();
        this.description = description.trim();
        this.prereq = prereq.trim();
        this.coreq = coreq.trim();
        this.notes = notes.trim();
        this.faculty = faculty.trim();
    }

    public JsonObject toJSON(){
        JsonObject course = new JsonObject();
        course.put("title", title);
        course.put("id", id);
        course.put("credits", credits);
        course.put("description", description);
        course.put("prereq", prereq);
        course.put("coreq", coreq);
        course.put("notes", notes);
        course.put("faculty", faculty);

        return course;
    }

    public String toCSV(){
        return title+","+id+","+credits+","+description+","+prereq+","+coreq+","+notes+","+faculty;
    }

    @Override
    public String toString() {
        return "Course{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", credits='" + credits + '\'' +
                ", description='" + description + '\'' +
                ", prereq='" + prereq + '\'' +
                ", coreq='" + coreq + '\'' +
                ", notes='" + notes + '\'' +
                ", faculty='" + faculty + '\'' +
                '}';
    }
}
