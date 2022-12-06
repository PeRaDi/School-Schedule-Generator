package pt.ipca;

import pt.ipca.data.Class;
import pt.ipca.data.Course;
import pt.ipca.data.Teacher;
import pt.ipca.io.FileReader;
import pt.ipca.io.JSON;

import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("School Schedule Generator (" + System.getProperty("user.dir") + ")\n");

        ArrayList<Class> classes = null;
        ArrayList<Course> courses = null;
        ArrayList<Teacher> teachers = null;

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

        ArrayList<Class> realClasses = new ArrayList<>(classes);
        realClasses.addAll(classes);


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

        ArrayList<ArrayList<ArrayList<Class>>> schedule = null;
        schedule = geneticAlgorithm(realClasses, 3);
        JSON.save(schedule, (System.getProperty("user.dir") + "\\schedule-genetic.json"));
    }

    private static ArrayList getRandomItemsAndPop(ArrayList array, int n) {
        ArrayList out = new ArrayList();
        if (array.size() < n) n = array.size();
        //else n = (int)(Math.random() * n + 1);
        for (int i = 0; i < n; i++) {
            int randomItem = (int) Math.floor(Math.random() * ((array.size() - 1) - 0 + 1) + 0);
            out.add(array.get(randomItem));
            array.remove(randomItem);
        }
        return out;
    }

    public static ArrayList<ArrayList<ArrayList<Class>>> generateSchedule(ArrayList<Class> classes, int numberOfRooms) {
        ArrayList<ArrayList<ArrayList<Class>>> schedule = new ArrayList<ArrayList<ArrayList<Class>>>();
        ArrayList<Class> auxClasses = new ArrayList<>(classes);
        for (int i = 0; i < 5; i++) {
            ArrayList<ArrayList<Class>> day = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                day.add(new ArrayList<Class>(getRandomItemsAndPop(auxClasses, numberOfRooms)));
            }
            schedule.add(day);
        }
        return schedule;
    }

    public static boolean isValid(ArrayList<ArrayList<ArrayList<Class>>> schedule) {
        if (schedule == null) return false;
        //dias da semana
        for (int i = 0; i < schedule.size(); i++) {
            HashMap<Integer, Integer> classesPerDay = new HashMap<>();
            //horas
            for (int j = 0; j < schedule.get(i).size(); j++) {
                //aulas
                for (int k = 0; k < schedule.get(i).get(j).size(); k++) {
                    Class c = schedule.get(i).get(j).get(k);

                    if (!classesPerDay.containsKey(c.getCourseId())) classesPerDay.put(c.getCourseId(), 1);
                    else classesPerDay.put(c.getCourseId(), classesPerDay.get(c.getCourseId()) + 1);

                    //check if there are 2 classes at the same time
                    for (int l = 0; l < schedule.get(i).get(j).size(); l++) {
                        if (schedule.get(i).get(j).get(l) != c && schedule.get(i).get(j).get(l).getCourseId() == c.getCourseId()) {
                            //System.out.println("Generation " + generationId + ": Invalid schedule. Two classes at the same time for the same course.");
                            return false;
                        }
                    }
                    //check if there are 2 classes with the same teacher at the same time
                    for (int l = 0; l < schedule.get(i).get(j).size(); l++) {
                        if (schedule.get(i).get(j).get(l) != c && schedule.get(i).get(j).get(l).getTeacherId() == c.getTeacherId()) {
                            //System.out.println("Generation " + generationId + ": Invalid schedule. Teacher " + c.getTeacherId() + " has 2 classes at the same time.");
                            return false;
                        }
                    }
                    /*//check if there are 2 classes of the same course at the same time
                    for (int l = 0; l < schedule.get(i).get(j).size(); l++) {
                        if(schedule.get(i).get(j).get(l) != c && schedule.get(i).get(j).get(l).getId() == c.getId()) {
                            System.out.println("Generation " + generationId + ": Invalid schedule. Course " + c.getId() + " has 2 classes at the same time.");
                            return false;
                        }
                    }*/
                }
            }
            for (Integer key : classesPerDay.keySet())
                if (classesPerDay.get(key) > 3) {
                    //System.out.println("Generation " + generationId + ": Invalid schedule. " + key + " has more than 3 classes in a day.");
                    return false;
                }
        }
        return true;
    }

    public static ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> generateRandomPopulation(int size, ArrayList<Class> classes, int numberOfRooms) {
        ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            population.add(generateSchedule(classes, numberOfRooms));
        }
        return population;
    }

    public static ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> generatePopulation(ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> population, ArrayList<Class> classes, int numberOfRooms) {
        ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> newPopulation = new ArrayList<>();
        if (population == null) population = generateRandomPopulation(100, classes, numberOfRooms);
        for (int i = 0; i < population.size(); i++) {
            ArrayList<ArrayList<ArrayList<Class>>> parent1 = population.get(i);
            ArrayList<ArrayList<ArrayList<Class>>> parent2 = population.get((int) (Math.random() * population.size()));
            ArrayList<ArrayList<ArrayList<Class>>> child = reproduce(parent1, parent2);
            if (Math.random() < 0.1) child = mutate(child);
            newPopulation.add(child);
        }
        return newPopulation;
    }

    public static int getFitness(ArrayList<ArrayList<ArrayList<Class>>> schedule) {
        int score = 0;
        //dias da semana
        for (int i = 0; i < schedule.size(); i++) {
            HashMap<Integer, Integer> classesPerDay = new HashMap<>();
            //horas
            for (int j = 0; j < schedule.get(i).size(); j++) {
                //aulas
                for (int k = 0; k < schedule.get(i).get(j).size(); k++) {
                    Class c = schedule.get(i).get(j).get(k);

                    if (!classesPerDay.containsKey(c.getCourseId())) classesPerDay.put(c.getCourseId(), 1);
                    else classesPerDay.put(c.getCourseId(), classesPerDay.get(c.getCourseId()) + 1);

                    //check if there are 2 classes at the same time
                    for (int l = 0; l < schedule.get(i).get(j).size(); l++) {
                        if(schedule.get(i).get(j).get(l) != c && schedule.get(i).get(j).get(l).getCourseId() == c.getCourseId()) {
                            score += 2;
                        }
                    }
                    //check if there are 2 classes with the same teacher at the same time
                    for (int l = 0; l < schedule.get(i).get(j).size(); l++) {
                        if(schedule.get(i).get(j).get(l) != c && schedule.get(i).get(j).get(l).getTeacherId() == c.getTeacherId()) {
                            score += 2;
                        }
                    }
                }
            }
            for (Integer key : classesPerDay.keySet()) {
                score += Math.abs(classesPerDay.get(key) - 3);
            }
        }
        return score;
    }

    public static ArrayList<ArrayList<ArrayList<Class>>> geneticAlgorithm(ArrayList<Class> classes, int numberOfRooms) {
        ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> population = generateRandomPopulation(1000, classes, numberOfRooms);
        int generation = 1;
        while (!isValid(population.get(0))) {
            population = generatePopulation(population, classes, numberOfRooms);
            Collections.sort(population, new Comparator<>() {
                @Override
                public int compare(ArrayList<ArrayList<ArrayList<Class>>> o1, ArrayList<ArrayList<ArrayList<Class>>> o2) {
                    return getFitness(o1) - getFitness(o2);
                }
            });

            HashMap a = getFitnessMap(population);
            System.out.println("Generation: " + generation + " (" +  generation * 1000 + " schedules generated)");
            System.out.println("Worst fitness: " + getFitness(population.get(population.size() - 1)));
            System.out.println("Best fitness: " + getFitness(population.get(0)));
            System.out.println("_____________________________________________________________");
            System.out.println();

            generation++;
        }
        System.out.println(generation + " generations to find a valid schedule.");
        return population.get(0);
    }

    public static HashMap<ArrayList<ArrayList<ArrayList<Class>>>, Integer> getFitnessMap(ArrayList<ArrayList<ArrayList<ArrayList<Class>>>> population) {
        HashMap<ArrayList<ArrayList<ArrayList<Class>>>, Integer> fitnessMap = new HashMap<>();
        for (int i = 0; i < population.size(); i++) {
            fitnessMap.put(population.get(i), getFitness(population.get(i)));
        }
        return fitnessMap;
    }

    public static ArrayList<ArrayList<ArrayList<Class>>> reproduce(ArrayList<ArrayList<ArrayList<Class>>> parent1, ArrayList<ArrayList<ArrayList<Class>>> parent2) {
        ArrayList<ArrayList<ArrayList<Class>>> child = new ArrayList<>();
        for (int i = 0; i < parent1.size(); i++) {
            ArrayList<ArrayList<Class>> day = new ArrayList<>();
            for (int j = 0; j < parent1.get(i).size(); j++) {
                ArrayList<Class> hour = new ArrayList<>();
                for (int k = 0; k < parent1.get(i).get(j).size(); k++) {
                    if (Math.random() < 0.5) hour.add(parent1.get(i).get(j).get(k));
                    else hour.add(parent2.get(i).get(j).get(k));
                }
                day.add(hour);
            }
            child.add(day);
        }
        return child;
    }

    public static ArrayList<ArrayList<ArrayList<Class>>> mutate(ArrayList<ArrayList<ArrayList<Class>>> schedule) {
        ArrayList<ArrayList<ArrayList<Class>>> newSchedule = new ArrayList<>();
        for (int i = 0; i < schedule.size(); i++) {
            ArrayList<ArrayList<Class>> day = new ArrayList<>();
            for (int j = 0; j < schedule.get(i).size(); j++) {
                ArrayList<Class> hour = new ArrayList<>();
                for (int k = 0; k < schedule.get(i).get(j).size(); k++) {
                    if (Math.random() < 0.1) hour.add(schedule.get(i).get(j).get(k));
                    else
                        hour.add(schedule.get((int) (Math.random() * schedule.size())).get((int) (Math.random() * schedule.get(i).size())).get((int) (Math.random() * schedule.get(i).get(j).size())));
                }
                day.add(hour);
            }
            newSchedule.add(day);
        }
        return newSchedule;
    }
}