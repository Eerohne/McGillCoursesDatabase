package courses;

import com.github.cliftonlabs.json_simple.JsonObject;

/**
 * Holds all timeless data for a given course at McGill
 */
public class Course {
    String title, id, credits, description, prereq, coreq, notes, faculty, restrictions, terms, profs;

    public Course(String title, String id, String credits, String description, String prereq,
                  String coreq, String notes, String faculty, String restrictions, String terms, String profs) {
        this.title = title.trim();
        this.id = id.trim();
        this.credits = credits.trim();
        this.description = description.trim();
        this.prereq = prereq.trim();
        this.coreq = coreq.trim().replace(".", "");
        this.notes = notes.trim();
        this.faculty = faculty.trim();
        this.restrictions = restrictions.trim();
        this.terms = terms.trim();
        this.profs = profs.trim();

        if(this.title.isEmpty()) this.title = this.id;
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
        course.put("restrictions", restrictions);
        course.put("terms", terms);
        course.put("profs", profs);

        return course;
    }

    public String toTSV(){
        return title+"\t"+id+"\t"+credits+"\t"+description+"\t"+prereq+"\t"+coreq+"\t"+notes+"\t"+faculty+"\t"+restrictions;
    }
}
