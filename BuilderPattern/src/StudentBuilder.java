import java.util.List;

abstract class StudentBuilder {


    //mandatory
    int rollNO;
    String firstName;
    String lastName;;

    //optional
     List<String> subjects;


    public StudentBuilder setFirstName(String firstName)
    {
        this.firstName = firstName;
        return this;
    }

    public StudentBuilder setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }


    public StudentBuilder setRollNo(int rollNO)
    {
        this.rollNO = rollNO;
        return this;
    }

    abstract StudentBuilder setSubjects();

    public Student build()
    {
        return new Student(this);
    }
}
