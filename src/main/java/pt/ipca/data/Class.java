package pt.ipca.data;

public class Class {

    private int id;
    private String label;
    private int teacherId;
    private int courseId;

    public Class(int id, String label, int teacherId, int courseId) {
        this.id = id;
        this.label = label;
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "[" + this.id + ", " + this.label + ", " + this.teacherId + ", " + this.courseId + "]";
    }
}
