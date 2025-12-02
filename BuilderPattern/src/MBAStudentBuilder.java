import java.util.ArrayList;
import java.util.List;

public class MBAStudentBuilder extends  StudentBuilder{

    @Override
    StudentBuilder setSubjects() {
        List<String> subjects = new ArrayList<>();

        subjects.add("MBA1");
        subjects.add("MBA2");
        this.subjects = subjects;
        return this;
    }

}
