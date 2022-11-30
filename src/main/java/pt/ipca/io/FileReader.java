package pt.ipca.io;

import pt.ipca.data.Class;
import pt.ipca.data.Course;
import pt.ipca.data.Teacher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    public static List<Class> readClasses() throws FileNotFoundException {
        List<Class> classes = new ArrayList<Class>();

        Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "\\classes.csv"));

        boolean header = true;
        while(sc.hasNext()) {
            if (header) {
                header = false;
                sc.nextLine();
            } else {
                String line = sc.nextLine();
                String values[] = line.split(",");

                classes.add(new Class(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3])));
            }
        }
        return classes;
    }

    public static List<Course> readCourses() throws FileNotFoundException {
        List<Course> courses = new ArrayList<Course>();

        Scanner sc = new Scanner(new File(System.getProperty("user.dir") + "\\courses.csv"));

        boolean header = true;
        while(sc.hasNext()) {
            if (header) {
                header = false;
                sc.nextLine();
            } else {
                String line = sc.nextLine();
                String values[] = line.split(",");

                courses.add(new Course(Integer.parseInt(values[0]), values[1]));
            }
        }
        return courses;
    }

    public static List<Teacher> readTeachers() throws FileNotFoundException {
        List<Teacher> teachers = new ArrayList<Teacher>();
        File f = new File(System.getProperty("user.dir") + "\\teachers.csv");
        Scanner sc = new Scanner(f);

        boolean header = true;
        while(sc.hasNext()) {
            if (header) {
                header = false;
                sc.nextLine();
            } else {
                String line = sc.nextLine();
                String values[] = line.split(",");
                boolean pref = values[2] == "true" ? true : false;
                teachers.add(new Teacher(Integer.parseInt(values[0]), values[1], pref));
            }
        }
        return teachers;
    }
}
