# Template Method Design Pattern in Java

---

## One Line
> **Define the skeleton of an algorithm in a base class**, and let subclasses fill in the specific steps — without changing the overall structure.

---

## Analogy
```
Tea and Coffee follow the same steps:

1. Boil water        → FIXED    (same for all)
2. Brew              → ABSTRACT (tea leaves vs coffee powder)
3. Pour in cup       → FIXED    (same for all)
4. Add condiments    → HOOK     (lemon vs milk+sugar vs nothing)

Skeleton is fixed. Specific steps vary.
```

---

## 3 Types of Methods — Core Concept

```java
private void boilWater()                      // FIXED    — private, no override allowed
protected abstract void brew()                // ABSTRACT — subclass MUST implement
protected void addCondiments() {}             // HOOK     — subclass MAY override, default empty
protected boolean customerWantsCondiments()   // HOOK     — controls flow, returns boolean
```

| Type | Keyword | Subclass | When to use |
|---|---|---|---|
| **Fixed** | `private` / `final` | Cannot override | Steps same for everyone |
| **Abstract** | `abstract` | Must override | Steps that always vary |
| **Hook** | `protected` with default | May override | Optional variation or flow control |

---

## Abstract vs Hook — Most Important Distinction

```java
// ABSTRACT — forces every subclass to implement, even if empty
// black coffee has nothing to add but is FORCED to write empty method ❌
protected abstract void addCondiments();

// HOOK — optional, default is empty
// black coffee just skips it cleanly ✅
protected void addCondiments() {}  // default — do nothing
```

> Use **abstract** when every subclass MUST have its own implementation.
> Use **hook** when some subclasses may want to skip the step entirely.

---

## The Code

```java
// ABSTRACT CLASS — skeleton defined here
abstract class HotBeverage {

    // TEMPLATE METHOD — final, order of steps cannot be changed by subclass
    public final void prepare() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {  // hook controls whether step runs
            addCondiments();
        }
    }

    // FIXED steps — private, nobody overrides
    private void boilWater() { System.out.println("Boiling water..."); }
    private void pourInCup() { System.out.println("Pouring into cup..."); }

    // ABSTRACT — subclass must define
    protected abstract void brew();

    // HOOK — subclass may override, default is empty
    protected void addCondiments() {}

    // HOOK — controls whether condiments step runs, default is true
    protected boolean customerWantsCondiments() { return true; }
}

// Tea — implements brew, adds lemon
class Tea extends HotBeverage {
    @Override
    protected void brew() { System.out.println("Steeping the tea leaves..."); }

    @Override
    protected void addCondiments() { System.out.println("Adding lemon..."); }
}

// Coffee — implements brew, optionally adds condiments
class Coffee extends HotBeverage {
    private final boolean blackCoffee;

    public Coffee(boolean blackCoffee) { this.blackCoffee = blackCoffee; }

    @Override
    protected void brew() { System.out.println("Dripping coffee through filter..."); }

    @Override
    protected void addCondiments() { System.out.println("Adding milk and sugar..."); }

    // hook override — black coffee skips condiments entirely
    @Override
    protected boolean customerWantsCondiments() { return !blackCoffee; }
}

// CLIENT
public class TemplateMethodDemo {
    public static void main(String[] args) {
        new Tea().prepare();
        new Coffee(false).prepare();  // with condiments
        new Coffee(true).prepare();   // black coffee — condiments skipped
    }
}
```

---

## Output
```
Boiling water...
Steeping the tea leaves...
Pouring into cup...
Adding lemon...

Boiling water...
Dripping coffee through filter...
Pouring into cup...
Adding milk and sugar...

Boiling water...
Dripping coffee through filter...
Pouring into cup...
                     ← condiments skipped via hook
```

---

## Interview Questions

**Q1. Why is `prepare()` marked `final`?**
```
To prevent subclasses from changing the algorithm's skeleton.
The ORDER of steps is the whole point of the pattern.
If subclass could override prepare(), the skeleton breaks.
```

**Q2. What is a Hook?**
```
A method in the abstract class with a default implementation (usually empty or true).
Subclass MAY override it — but is not forced to.
Used for:
→ Optional steps       (addCondiments — some beverages skip it)
→ Flow control         (customerWantsCondiments — returns boolean to skip a step)
```

**Q3. Abstract method vs Hook?**
```
abstract → no default, subclass MUST implement
hook     → has default, subclass MAY override
           use hook when a step is optional for some subclasses
```

**Q4. Template Method vs Strategy — most asked**

| | Template Method | Strategy |
|---|---|---|
| **Mechanism** | Inheritance (IS-A) | Composition (HAS-A) |
| **Varies via** | Overriding methods in subclass | Injecting different strategy object |
| **Swap at runtime?** | ❌ No — fixed at compile time | ✅ Yes |
| **Preferred?** | Small variations, fixed structure | Behaviour needs to be swappable |
| **Relation** | Base class calls subclass | Context delegates to strategy |

**Q5. What is the Hollywood Principle?**
```
"Don't call us, we'll call you."

Base class (HotBeverage) calls subclass methods (brew, addCondiments).
Subclass never calls base class directly.
Control always stays in the base class templateMethod.
```

**Q6. Where is Template Method used in real world Java?**
```
java.io.InputStream        → read() is abstract, read(byte[], int, int) is template
java.util.AbstractList     → get() is abstract, contains/indexOf/iterator use it
HttpServlet                → service() calls doGet()/doPost() — subclass fills in
Spring JdbcTemplate        → execute() is skeleton, callbacks fill the steps
JUnit TestCase             → setUp() → test() → tearDown() — classic template
```

---

## Responsibility Breakdown

```
Abstract class  → controls WHEN each step runs (order)
Subclass        → controls WHAT happens in each step (content)
Hook            → gives subclass optional influence over the flow
```

---

## When to Use
- Multiple classes share **same algorithm structure** with different step implementations
- You want to **eliminate code duplication** across subclasses
- You want strict **control over step order**

## When NOT to Use
- Steps need to be **swapped at runtime** — use Strategy instead
- Too many subclasses for each variation — prefer Strategy (composition over inheritance)

---

## One Line to Remember
> **Template Method controls the WHEN. Subclass controls the WHAT.** 🎯

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)