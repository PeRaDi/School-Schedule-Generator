package pt.ipca.data;

public class Teacher {

    private int id;
    private String name;
    private boolean preference;

    public Teacher(int id, String name, boolean preference) {
        this.id = id;
        this.name = name;
        this.preference = preference;
    }

    @Override
    public String toString() {
        return "[" + this.id + ", " + this.name + ", " + this.preference + "]";
    }
}
