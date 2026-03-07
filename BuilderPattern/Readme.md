# Builder Design Pattern in Java

> A beginner-friendly guide to understanding the Builder Pattern with a real-world Student Registration example.

---

## What is the Builder Pattern?

The Builder Pattern is a **creational design pattern** that lets you construct complex objects **step by step**, separating the construction process from the final representation.

> Instead of passing 10 parameters to a constructor, you build the object piece by piece in a controlled, readable way.

---

## Real World Analogy

Think of **admission in a college**:

```
CS Student needs:
→ Roll Number, First Name, Last Name  (mandatory for all)
→ Subjects: CN, OS, DBMS             (specific to CS)

MBA Student needs:
→ Roll Number, First Name, Last Name  (mandatory for all)
→ Subjects: MBA1, MBA2               (specific to MBA)
```

The **construction steps are the same** (set name, roll no, subjects),  
but the **result is different** for each department.  
A **Director** controls the order of steps.  
A **Builder** knows what to fill in each step.

---

## Project Structure

```
BuilderPattern/
│
└── src/
    ├── Main.java               # Client — creates directors and triggers build
    ├── Student.java            # Product — the final object being built
    ├── StudentBuilder.java     # Abstract Builder — defines steps
    ├── CSStudentBuilder.java   # Concrete Builder — CS specific implementation
    ├── MBAStudentBuilder.java  # Concrete Builder — MBA specific implementation
    └── StudentDirector.java    # Director — controls the order of build steps
```

---

## Key Components

| Component            | Role                                    | In This Example                         |
| -------------------- | --------------------------------------- | --------------------------------------- |
| **Product**          | The complex object being built          | `Student`                               |
| **Abstract Builder** | Defines all build steps                 | `StudentBuilder`                        |
| **Concrete Builder** | Implements steps for a specific variant | `CSStudentBuilder`, `MBAStudentBuilder` |
| **Director**         | Controls the order of build steps       | `StudentDirector`                       |
| **Client**           | Chooses builder, asks director to build | `Main.java`                             |

---

## The Code

### Product — The Final Object Being Built

```java
import java.util.List;

public class Student {
    private int rollNO;
    private String firstName;
    private String lastName;
    private List<String> subjects;

    // constructor takes builder — not raw parameters
    Student(StudentBuilder studentBuilder) {
        this.firstName = studentBuilder.firstName;
        this.lastName  = studentBuilder.lastName;
        this.rollNO    = studentBuilder.rollNO;
        this.subjects  = studentBuilder.subjects;
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
```

### Abstract Builder — Defines All Build Steps

```java
import java.util.List;

abstract class StudentBuilder {
    // mandatory fields — common to all students
    int rollNO;
    String firstName;
    String lastName;

    // optional fields — vary by student type
    List<String> subjects;

    // common steps — implemented here (method chaining via return this)
    public StudentBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;  // enables method chaining
    }

    public StudentBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentBuilder setRollNo(int rollNO) {
        this.rollNO = rollNO;
        return this;
    }

    // variant step — each concrete builder implements this differently
    abstract StudentBuilder setSubjects();

    // final step — assembles and returns the Product
    public Student build() {
        return new Student(this);
    }
}
```

### Concrete Builders — Department Specific Implementations

```java
// CS Student — gets CN, OS, DBMS subjects
import java.util.ArrayList;
import java.util.List;

public class CSStudentBuilder extends StudentBuilder {
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

// MBA Student — gets MBA1, MBA2 subjects
import java.util.ArrayList;
import java.util.List;

public class MBAStudentBuilder extends StudentBuilder {
    @Override
    StudentBuilder setSubjects() {
        List<String> subjects = new ArrayList<>();
        subjects.add("MBA1");
        subjects.add("MBA2");
        this.subjects = subjects;
        return this;
    }
}
```

### Director — Controls the Order of Build Steps

```java
public class StudentDirector {
    StudentBuilder studentBuilder;

    // builder injected via constructor
    StudentDirector(StudentBuilder studentBuilder) {
        this.studentBuilder = studentBuilder;
    }

    // director decides the ORDER of steps — client doesn't need to know this
    public Student createStudent(int rollNo, String firstName, String lastName) {
        return studentBuilder
                .setRollNo(rollNo)       // step 1
                .setFirstName(firstName) // step 2
                .setLastName(lastName)   // step 3
                .setSubjects()           // step 4 — varies by builder
                .build();                // step 5 — assemble final object
    }
}
```

### Client — Chooses Builder, Asks Director to Build

```java
public class Main {
    public static void main(String[] args) {

        // create directors — inject the specific builder they will use
        StudentDirector csDirector  = new StudentDirector(new CSStudentBuilder());
        StudentDirector mbaDirector = new StudentDirector(new MBAStudentBuilder());

        // director builds the student — client doesn't know the internal steps
        Student csStudent  = csDirector.createStudent(5,   "vaibhav", "bagate");
        Student mbaStudent = mbaDirector.createStudent(123, "sumit",   "bagate");

        System.out.println(csStudent.toString());
        System.out.println(mbaStudent.toString());
    }
}
```

---

## Output

```
Student{rollNO=5,   firstName='vaibhav', lastName='bagate', subjects=[CN, OS, DBMS]}
Student{rollNO=123, firstName='sumit',   lastName='bagate', subjects=[MBA1, MBA2]}
```

---

## How it All Fits Together

```
Main (Client)
  │
  ├── creates ──► CSStudentBuilder
  │                     │
  ├── injects into ───► StudentDirector
  │                           │
  │                           │ controls order of steps:
  │                           │  .setRollNo()
  │                           │  .setFirstName()
  │                           │  .setLastName()
  │                           │  .setSubjects()   ← CS specific
  │                           │  .build()
  │                           │
  │                           └──► Student { CN, OS, DBMS }
  │
  └── same flow with MBAStudentBuilder
                              └──► Student { MBA1, MBA2 }
```

---

## Why `return this` in Builder Methods?

It enables **method chaining** — calling multiple methods in one line:

```java
// without return this — verbose
builder.setRollNo(5);
builder.setFirstName("vaibhav");
builder.setLastName("bagate");

// with return this — clean method chaining
builder.setRollNo(5)
       .setFirstName("vaibhav")
       .setLastName("bagate")
       .build();
```

---

## Why Does `Student` Take a `StudentBuilder` in Its Constructor?

Instead of exposing a messy constructor with many parameters:

```java
// ❌ BAD — telescoping constructor, hard to read
new Student(5, "vaibhav", "bagate", List.of("CN", "OS", "DBMS"));

// ✅ GOOD — builder carries all state, Student just reads from it
new Student(studentBuilder);  // clean, no parameter confusion
```

---

## Builder vs Other Creational Patterns

|                         | Builder                   | Factory                    | Abstract Factory            |
| ----------------------- | ------------------------- | -------------------------- | --------------------------- |
| **Focus**               | Step by step construction | Which class to instantiate | Families of related objects |
| **Complex object?**     | ✅ Yes                    | ❌ Simple                  | ❌ Simple                   |
| **Construction order?** | ✅ Controlled by Director | ❌ No                      | ❌ No                       |
| **Variants**            | Different builders        | Different subclasses       | Different factories         |

---

## SOLID Principles Covered

| Rule                                                                 | SOLID Principle                 |
| -------------------------------------------------------------------- | ------------------------------- |
| Each builder handles one type of student                             | Single Responsibility Principle |
| Add new department? Just add new builder, nothing else changes       | Open/Closed Principle           |
| Director depends on abstract `StudentBuilder`, not concrete builders | Dependency Inversion Principle  |

---

## When to Use Builder Pattern?

- Object has **many parameters**, especially optional ones
- Object needs to be created in a **specific order of steps**
- **Same construction process** should produce different representations
- You want to avoid **telescoping constructors** (constructors with many parameters)

---

## Key Takeaway

> **"Separate how an object is built from what it finally looks like."**

The `Director` knows the **order** of steps.  
The `Builder` knows the **content** of each step.  
The `Product` knows nothing about how it was built.  
The `Client` just picks a builder and asks the director to build.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)

---
