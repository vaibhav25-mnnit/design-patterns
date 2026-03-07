# Factory Design Pattern in Java
> A beginner-friendly guide to understanding the Factory Pattern with a real-world Shape creation example.

---

## What is the Factory Pattern?

The Factory Pattern is a **creational design pattern** that provides a single place to create objects **without exposing the creation logic** to the client. The client asks the factory *"give me this type of object"* and the factory decides which class to instantiate.

> Instead of the client doing `new Circle()` or `new Square()`, it just says `ShapeFactory.getShape(CIRCLE)` and gets back a `Shape`. It never needs to know which concrete class was created.

---

## Real World Analogy

Think of a **vehicle showroom**:

```
You walk in and say: "Give me a Sedan"
        │
        ▼
Showroom (Factory) checks your request
        │
        ├──► SEDAN    → hands you a Honda City
        ├──► SUV      → hands you a Fortuner
        └──► HATCHBACK→ hands you a Swift

You always receive a "Vehicle" — you never build it yourself.
```

Same idea here — you ask for a `CIRCLE`, factory gives you a `Circle`. You only ever hold a `Shape`.

---

## Project Structure

```
FactoryPattern/
│
└── src/
    ├── Main.java          # Client — asks factory for shapes
    ├── Shape.java         # Product Interface — common type for all shapes
    ├── ShapeType.java     # Enum — type-safe shape request
    ├── Circle.java        # Concrete Product
    ├── Square.java        # Concrete Product
    ├── Triangle.java      # Concrete Product
    └── ShapeFactory.java  # Factory — decides which shape to create
```

---

## Key Components

| Component | Role | In This Example |
|---|---|---|
| **Product Interface** | Common type all products implement | `Shape` |
| **Concrete Products** | Actual objects being created | `Circle`, `Square`, `Triangle` |
| **Factory** | Decides which object to instantiate | `ShapeFactory` |
| **Enum** | Type-safe way to request a product | `ShapeType` |
| **Client** | Asks factory, never uses `new` directly | `Main.java` |

---

## The Code

### Product Interface — Common Type for All Shapes
```java
public interface Shape {
    public void sayMyName();
}
```

### Concrete Products — Actual Shapes Being Created
```java
public class Circle implements Shape {
    @Override
    public void sayMyName() {
        System.out.println("I am circle without any angles :(");
    }
}

public class Square implements Shape {
    @Override
    public void sayMyName() {
        System.out.println("I am square with 4 angles");
    }
}

public class Triangle implements Shape {
    @Override
    public void sayMyName() {
        System.out.println("I am Triangle with 3 angles");
    }
}
```

### Enum — Type-Safe Way to Request a Shape
```java
public enum ShapeType {
    CIRCLE,
    TRIANGLE,
    SQUARE
}
```

### Factory — The Only Place Where `new` is Called
```java
public class ShapeFactory {
    public static Shape getShape(ShapeType shapeType) {
        switch (shapeType) {
            case CIRCLE:   return new Circle();    // client never sees this
            case SQUARE:   return new Square();    // client never sees this
            case TRIANGLE: return new Triangle();  // client never sees this
        }
        return null;
    }
}
```

### Client — Asks Factory, Never Uses `new` Directly
```java
public class Main {
    public static void main(String[] args) {

        // client only knows Shape interface and ShapeType enum
        // has zero knowledge of Circle, Square, Triangle classes
        Shape circle   = ShapeFactory.getShape(ShapeType.CIRCLE);
        Shape square   = ShapeFactory.getShape(ShapeType.SQUARE);
        Shape triangle = ShapeFactory.getShape(ShapeType.TRIANGLE);

        circle.sayMyName();
        square.sayMyName();
        triangle.sayMyName();
    }
}
```

---

## Output

```
I am circle without any angles :(
I am square with 4 angles
I am Triangle with 3 angles
```

---

## How the Factory Works Internally

```
Client calls: ShapeFactory.getShape(ShapeType.CIRCLE)
                              │
                              ▼
                    switch(shapeType)
                              │
               ┌──────────────┼──────────────┐
               ▼              ▼              ▼
           CIRCLE           SQUARE        TRIANGLE
        new Circle()      new Square()  new Triangle()
               │
               ▼
        returns as Shape  ◄── client receives interface, not concrete class
```

---

## Without Factory vs With Factory

```java
// ❌ WITHOUT Factory
// Client is tightly coupled to every concrete class
// Adding a new shape means changing client code
Shape circle   = new Circle();
Shape square   = new Square();
Shape triangle = new Triangle();

// ✅ WITH Factory
// Client is decoupled — only knows Shape and ShapeType
// Adding a new shape means only changing ShapeFactory
Shape circle   = ShapeFactory.getShape(ShapeType.CIRCLE);
Shape square   = ShapeFactory.getShape(ShapeType.SQUARE);
Shape triangle = ShapeFactory.getShape(ShapeType.TRIANGLE);
```

---

## Adding a New Shape — Only One Place Changes

Say you want to add a `Pentagon`:

```java
// Step 1 — add to enum
public enum ShapeType { CIRCLE, TRIANGLE, SQUARE, PENTAGON }

// Step 2 — create the class
public class Pentagon implements Shape {
    @Override
    public void sayMyName() {
        System.out.println("I am Pentagon with 5 angles");
    }
}

// Step 3 — add one case in factory
case PENTAGON: return new Pentagon();

// Client code? — ZERO changes needed ✅
Shape pentagon = ShapeFactory.getShape(ShapeType.PENTAGON);
```

---

## Why Use Enum Instead of String?

```java
// ❌ BAD — using String, typo-prone, no compile time check
ShapeFactory.getShape("circle");   // works
ShapeFactory.getShape("Circle");   // fails silently
ShapeFactory.getShape("cirlce");   // typo, fails silently

// ✅ GOOD — using Enum, compile time safety
ShapeFactory.getShape(ShapeType.CIRCLE);   // always correct
ShapeFactory.getShape(ShapeType.CIRLCE);   // compile error — caught immediately
```

---

## Factory vs Other Creational Patterns

| | Factory | Abstract Factory | Builder |
|---|---|---|---|
| **Creates** | One product | Family of related products | One complex product step by step |
| **Focus** | Which class to instantiate | Which family to use | How to construct |
| **Complexity** | Simple | Medium | High |
| **Example** | `ShapeFactory` | `UIFactory (WindowsUI / MacUI)` | `StudentBuilder` |

---

## SOLID Principles Covered

| Rule | SOLID Principle |
|---|---|
| Factory is the only place responsible for object creation | Single Responsibility Principle |
| Add new shape by adding new class + one line in factory, client unchanged | Open/Closed Principle |
| Client depends on `Shape` interface, not `Circle` or `Square` | Dependency Inversion Principle |
| All shapes substitutable wherever `Shape` is expected | Liskov Substitution Principle |

---

## When to Use Factory Pattern?

- You don't know ahead of time **which class** you need to instantiate
- You want to **centralize** object creation in one place
- You want the client to be **decoupled** from concrete classes
- Common use cases: **parsers, loggers, DB connections, UI components, shape renderers**

---

## Real World Usage

| Framework / Tool | How Factory is Used |
|---|---|
| `java.util.Calendar.getInstance()` | Returns right Calendar based on locale |
| `java.sql.DriverManager.getConnection()` | Returns right DB driver |
| `Spring BeanFactory` | Creates and manages beans by type |
| `NumberFormat.getInstance()` | Returns locale-specific number formatter |

---

## Key Takeaway

> **"Don't let the client worry about which class to instantiate. That is the factory's job."**

The client knows **what** it wants (`ShapeType.CIRCLE`).  
The factory knows **how** to create it (`new Circle()`).  
The client always receives the **interface** (`Shape`), never the concrete class.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)