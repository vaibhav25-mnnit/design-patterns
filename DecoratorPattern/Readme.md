# ☕ Coffee Machine Simulation – Decorator Design Pattern

## 📌 What Is This Project?

This project simulates a simple coffee machine.

You can:
- Choose a base coffee
- Add milk
- Add sugar
- See the final cost

The main purpose of this project is to understand the **Decorator Design Pattern** in a very simple and practical way.

---

# 🧠 Problem We Are Solving

Imagine a coffee shop menu:

## Base Coffees:
- Black Coffee – ₹25
- Indian Filter Coffee – ₹30
- Espresso – ₹50

## Add-ons:
- Milk – ₹10
- Sugar – ₹5

Now think about this:

If we create separate classes for every possible combination, we would need:

- BlackCoffeeWithMilk
- BlackCoffeeWithSugar
- BlackCoffeeWithMilkAndSugar
- EspressoWithMilk
- EspressoWithSugar
- EspressoWithMilkAndSugar
- And many more...

This quickly becomes messy and hard to maintain ❌

This problem is called **Class Explosion**.

---

# 💡 Solution: Decorator Design Pattern

Instead of creating many classes, we:

1. Create base coffee classes.
2. Create add-on classes that wrap around coffee.
3. Dynamically add features at runtime.

This approach is called the **Decorator Pattern**.

---

# 🧠 When Should You Use the Decorator Pattern?

Use the Decorator Pattern when:

## 1️⃣ You Need Many Combinations of Features

If your system has:
- A base object
- Multiple optional add-ons
- Different combinations of those add-ons

Example in this project:
- 3 base coffees
- 2 add-ons
- Total combinations = Many

Without decorator → You would need separate classes for each combination.

Decorator allows flexible combinations without creating new classes every time.

---

## 2️⃣ You Want to Add Features Dynamically (At Runtime)

Sometimes you don't know in advance what features the user will choose.

Example:
- Customer selects coffee
- Then chooses milk
- Then decides to add sugar

You cannot predefine every possible combination.

Decorator allows:
- Adding behavior dynamically
- Layer by layer

---

## 3️⃣ You Want to Follow Open/Closed Principle

Open/Closed Principle says:

> Software entities should be open for extension but closed for modification.

With decorator:
- You can create new add-ons (like Chocolate)
- Without changing existing coffee classes

That means your old code stays untouched and safe.

---

# 🚫 When NOT to Use Decorator Pattern

Do NOT use it when:

❌ You only have 1–2 combinations  
❌ There is no need for dynamic feature addition  
❌ The object is simple and unlikely to grow

Using decorator unnecessarily can:
- Increase complexity
- Make debugging harder
- Create too many small classes

---

# ⚖ Advantages of Decorator Pattern

✅ Avoids class explosion  
✅ Flexible and scalable  
✅ Supports dynamic behavior addition  
✅ Follows SOLID principles  
✅ Promotes composition over inheritance

---

# ⚠ Disadvantages of Decorator Pattern

❌ Can create many small classes  
❌ Harder to understand for beginners  
❌ Debugging nested decorators can be tricky

---
# 🏗 How It Works (Simple Explanation)

Think of it like wrapping a gift 🎁

- Base coffee = gift box
- Milk = wrapping paper
- Sugar = ribbon

Each add-on wraps around the coffee and increases the cost.

We do not modify the original coffee.
We decorate it.

---

# 🧩 Step-by-Step Understanding

## 1️⃣ Coffee Interface

All coffee types must implement:
- `getDescription()`
- `getCost()`

This ensures every coffee behaves in a consistent way.

---

## 2️⃣ Base Coffee Classes

Each base coffee:
- Has its own price
- Returns its name

Example:
- Black Coffee → ₹25
- Indian Filter Coffee → ₹30
- Espresso → ₹50

---

## 3️⃣ CoffeeDecorator (The Wrapper)

This class:
- Holds a reference to a Coffee object
- Implements the Coffee interface
- Allows add-ons to wrap around coffee objects

---

## 4️⃣ Add-ons (Milk & Sugar)

### Milk:
- Adds ₹10
- Appends ", Milk" to the description

### Sugar:
- Adds ₹5
- Appends ", Sugar" to the description

Each add-on wraps the previous coffee object.

---

# 🧪 Example Usage

```java
Coffee coffee = new BlackCoffee();
coffee = new Milk(coffee);
coffee = new Sugar(coffee);

System.out.println(coffee.getDescription());
System.out.println(coffee.getCost());
Black Coffee, Milk, Sugar
40