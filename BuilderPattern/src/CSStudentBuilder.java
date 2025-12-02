import java.util.ArrayList;
import java.util.List;

public class CSStudentBuilder extends StudentBuilder{
    @Override
    StudentBuilder setSubjects() {
       List<String> subjects = new ArrayList<>();

       subjects.add("CN");
       subjects.add("OS");
       subjects.add("DBMS");
       this.subjects = subjects;
       return this;
    }
}
