package pt.ipca;

import pt.ipca.data.Class;
import pt.ipca.data.Course;
import pt.ipca.data.Teacher;
import pt.ipca.io.FileReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    private static List<Class> classes;
    private static List<Course> courses;
    private static List<Teacher> teachers;

    public static void main(String[] args) {
        System.out.println("School Schedule Generator (" + System.getProperty("user.dir") + ")\n");

        try {
            teachers = FileReader.readTeachers();
            System.out.println(" - Loaded Teachers [" + teachers.size() + "].");
            courses = FileReader.readCourses();
            System.out.println(" - Loaded Courses [" + courses.size() + "].");
            classes = FileReader.readClasses();
            System.out.println(" - Loaded Classes [" + classes.size() + "].");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        /**         MON     TUE     WED
         *        ________________________
         *       |       |  0,1  |       |
         *  9-11 |  0,0  |  [ ]  |  0,2  |         [ ]  LESI -> CLASS   |   LEGI -> CLASS
         *       |_______|_______|_______|
         *       |       |       |       |
         * 11-13 |  1,0  |  1,1  |  1,2  |
         *       |_______|_______|_______|
         *       |       |       |       |
         * 14-16 |  2,0  |  2,1  |  2,2  |
         *       |_______|_______|_______|
        */
        ArrayList<ArrayList<HashMap<Course, Class>>> schedule = new ArrayList<>();

        // System.out.println(classes);
        // System.out.println(courses);
        // System.out.println(teachers);


    }
}