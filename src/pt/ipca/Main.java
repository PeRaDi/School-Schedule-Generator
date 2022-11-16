package pt.ipca;

import pt.ipca.data.Class;
import pt.ipca.data.Course;
import pt.ipca.data.Teacher;
import pt.ipca.io.FileReader;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    private static List<Class> classes;
    private static List<Course> courses;
    private static List<Teacher> teachers;

    public static void main(String[] args) {
        System.out.println("School Schedule Generator (" + System.getProperty("user.dir") + ")\n");
        try {
            classes = FileReader.readClasses();
            courses = FileReader.readCourses();
            teachers = FileReader.readTeachers();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(classes);
        System.out.println(courses);
        System.out.println(teachers);
    }
}