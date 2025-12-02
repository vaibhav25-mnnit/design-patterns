public class StudentDirector {

    StudentBuilder studentBuilder;

    StudentDirector(StudentBuilder studentBuilder)
    {
        this.studentBuilder = studentBuilder;
    }

    public  Student createStudent(int rollNo, String firstName, String lastName)
    {
        return  studentBuilder.setRollNo(rollNo)
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setSubjects()
                    .build();
    }
}
