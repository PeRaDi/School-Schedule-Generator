package pt.ipca.data;

public class Course {
    private int id;
    private String label;

    public Course(int id, String label) {
        this.id = id;
        this.label = label;
    }

    @Override
    public String toString() {
        return "[" + this.id + ", " + this.label + "]";
    }
}
