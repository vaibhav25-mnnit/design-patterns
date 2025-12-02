public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world! THis is a builder design pattern");

        StudentDirector csDirector = new StudentDirector(new CSStudentBuilder());
        StudentDirector mbaDirctor = new StudentDirector(new MBAStudentBuilder());


        Student csStudent = csDirector.createStudent(5,"vaibhav","bagate");
        Student mbaStudent = mbaDirctor.createStudent(123,"sumit","bagate");


        System.out.println(csStudent.toString());
        System.out.println(mbaStudent.toString());

    }
}