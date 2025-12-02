import java.util.List;

public class Student {

    private int rollNO;
    private String firstName;
    private  String lastName;
    private List<String> subjects;

    Student(StudentBuilder studentBuilder)
    {
        this.firstName = studentBuilder.firstName;
        this.lastName = studentBuilder.lastName;
        this.rollNO = studentBuilder.rollNO;
        this.subjects = studentBuilder.subjects;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNO=" + rollNO +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}
