# Singleton Design Pattern in Java
> A complete interview-ready guide covering all 6 implementations, thread safety, edge cases, and expected interview questions with answers.

---

## What is the Singleton Pattern?

The Singleton Pattern is a **creational design pattern** that ensures a class has **only one instance** throughout the application lifecycle and provides a **global access point** to that instance.

```java
// Normal class — creates new object every time
MyClass obj1 = new MyClass();
MyClass obj2 = new MyClass();
// obj1 != obj2 — two different objects

// Singleton — always returns the SAME object
Singleton obj1 = Singleton.getInstance();
Singleton obj2 = Singleton.getInstance();
// obj1 == obj2 — same object, always
```

---

## The 3 Rules of Singleton

```
1. Private constructor       → nobody outside can do "new Singleton()"
2. Private static instance   → one instance, owned by the class itself
3. Public static getInstance → the only way to access that instance
```

---

## All 6 Implementations

---

### 1. Lazy Singleton ❌ Not Thread Safe

```java
class LazySingleton {
    private static LazySingleton instance;  // null initially

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();  // created only when first needed
        }
        return instance;
    }
}
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ✅ Yes — created only when needed |
| **Thread safe?** | ❌ No |
| **Performance** | ✅ Fast |
| **Use in production?** | ❌ No — only single threaded apps |

**Why not thread safe?**
```
Thread 1: checks instance == null → true
Thread 2: checks instance == null → true  (before Thread 1 creates it)
Thread 1: creates instance
Thread 2: creates instance (again!)
→ Two instances created ❌
```

---

### 2. Eager Singleton ✅ Thread Safe

```java
class EagerSingleton {
    // created immediately when class is loaded — not when getInstance() is called
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance() {
        return instance;
    }
}
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ❌ No — created at class load time |
| **Thread safe?** | ✅ Yes — JVM handles class loading |
| **Performance** | ✅ Fast |
| **Use in production?** | ⚠️ Only if instance is always needed |

**Drawback:** Instance created even if never used → wastes resources if initialization is expensive.

---

### 3. Static Block Singleton ✅ Thread Safe

```java
class StaticBlockSingleton {
    private static StaticBlockSingleton instance;

    private StaticBlockSingleton() {}

    // static block runs once when class is loaded
    static {
        try {
            instance = new StaticBlockSingleton();
        } catch (Exception e) {
            throw new RuntimeException("Exception in creating singleton instance");
        }
    }

    public static StaticBlockSingleton getInstance() {
        return instance;
    }
}
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ❌ No — created at class load time |
| **Thread safe?** | ✅ Yes |
| **Exception handling?** | ✅ Yes — can handle checked exceptions |
| **Use in production?** | ⚠️ When initialization may throw exceptions |

**Advantage over Eager:** Can handle exceptions during initialization.  
**Same drawback:** Not lazy — created even if never used.

---

### 4. Thread Safe Singleton ✅ Thread Safe but Slow

```java
class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    // synchronized — only one thread can enter at a time
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
}
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ✅ Yes |
| **Thread safe?** | ✅ Yes |
| **Performance** | ❌ Slow — synchronized on every call |
| **Use in production?** | ❌ No — performance bottleneck |

**Drawback:** `synchronized` locks the entire method on EVERY call — even after instance is already created. Unnecessary overhead.

---

### 5. Double Checked Locking ✅ Best for Multi-threaded Apps

```java
class DoubleCheckedSingleton {
    // volatile — prevents instruction reordering
    private static volatile DoubleCheckedSingleton instance;

    private DoubleCheckedSingleton() {}

    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {                          // Check 1 — no lock (fast path)
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {                  // Check 2 — with lock (safe path)
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ✅ Yes |
| **Thread safe?** | ✅ Yes |
| **Performance** | ✅ Fast — synchronized only during creation |
| **Use in production?** | ✅ Yes |

**Why two checks?**
```
Check 1 (outside sync) → avoids locking on every call after instance is created
Check 2 (inside sync)  → prevents two threads both passing Check 1 from creating two instances
```

**Why `volatile`?**
```
Without volatile, CPU can reorder instructions:
1. Allocate memory for instance
2. Assign reference to instance   ← another thread sees non-null but uninitialized object!
3. Call constructor

With volatile:
→ forces all writes to complete before reference is visible to other threads
→ prevents this partial initialization problem
```

---

### 6. Bill Pugh Singleton ✅ Best Overall (Recommended)

```java
class BillPughSingleton {
    private BillPughSingleton() {}

    // inner class is NOT loaded until getInstance() is called
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;  // inner class loaded here — thread safe by JVM
    }
}
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ✅ Yes — inner class loaded only when needed |
| **Thread safe?** | ✅ Yes — JVM class loading is thread safe |
| **Performance** | ✅ Fast — no synchronization needed |
| **Use in production?** | ✅ Yes — most recommended |

**Why is it thread safe without synchronized?**
```
JVM guarantees that a class is loaded and initialized only ONCE.
SingletonHelper is only loaded when getInstance() is first called.
JVM's class loading mechanism is inherently thread safe.
→ No synchronized, no volatile needed.
```

---

### 7. Enum Singleton ✅ Best for Serialization Safety

```java
public enum EnumSingleton {
    INSTANCE("localhost", 4000);

    private String host;
    private int port;

    EnumSingleton(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getPort()        { return port; }
    public String getHost()     { return host; }

    // ⚠️ WARNING — setters below are NOT thread safe
    public void setHost(String host) { this.host = host; }
    public void setPort(int port)    { this.port = port; }
}

// Usage
EnumSingleton singleton = EnumSingleton.INSTANCE;
System.out.println(singleton.getHost() + ":" + singleton.getPort());
```

| Property | Value |
|---|---|
| **Lazy loaded?** | ❌ No |
| **Thread safe?** | ✅ Yes — JVM guarantees enum instances are unique |
| **Serialization safe?** | ✅ Yes — only implementation that is |
| **Reflection safe?** | ✅ Yes — cannot break via reflection |
| **Use in production?** | ✅ Yes — when serialization matters |

---

## All Implementations at a Glance

| Implementation | Lazy | Thread Safe | Performance | Serialization Safe | Recommended |
|---|---|---|---|---|---|
| Lazy | ✅ | ❌ | ✅ | ❌ | ❌ Single thread only |
| Eager | ❌ | ✅ | ✅ | ❌ | ⚠️ If always needed |
| Static Block | ❌ | ✅ | ✅ | ❌ | ⚠️ If init throws exception |
| Thread Safe | ✅ | ✅ | ❌ | ❌ | ❌ Performance bottleneck |
| Double Checked | ✅ | ✅ | ✅ | ❌ | ✅ Multi-threaded apps |
| Bill Pugh | ✅ | ✅ | ✅ | ❌ | ✅ Best overall |
| Enum | ❌ | ✅ | ✅ | ✅ | ✅ When serialization matters |

---

## Ways Singleton Can Be Broken (And How to Fix)

### 1. Reflection Attack
```java
// ❌ Breaking singleton via reflection
Constructor<LazySingleton> constructor = LazySingleton.class.getDeclaredConstructor();
constructor.setAccessible(true);
LazySingleton instance2 = constructor.newInstance();  // second instance created!

// ✅ Fix — throw exception in constructor if instance exists
private LazySingleton() {
    if (instance != null) {
        throw new RuntimeException("Use getInstance() — reflection not allowed");
    }
}

// ✅ Best Fix — use Enum (reflection cannot instantiate enums)
```

### 2. Serialization Attack
```java
// ❌ Breaking singleton via serialization
ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("file.ser"));
out.writeObject(instance);
ObjectInputStream in = new ObjectInputStream(new FileInputStream("file.ser"));
LazySingleton instance2 = (LazySingleton) in.readObject();  // new instance created!

// ✅ Fix — implement readResolve()
protected Object readResolve() {
    return getInstance();  // return existing instance during deserialization
}

// ✅ Best Fix — use Enum (serialization safe by default)
```

### 3. Cloning Attack
```java
// ❌ Breaking singleton via clone
LazySingleton instance2 = (LazySingleton) instance.clone();  // new instance!

// ✅ Fix — override clone() and throw exception
@Override
protected Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException("Singleton cannot be cloned");
}
```

---

## Interview Questions and Answers

---

### Basic Level

**Q1. What is Singleton Pattern? Where is it used?**
```
A. A pattern that ensures only ONE instance of a class exists throughout the
   application and provides a global access point to it.

   Used in: Logger, Database connection pool, Configuration manager,
            Thread pool, Cache, Registry settings.
```

**Q2. What are the 3 key rules of Singleton?**
```
A. 1. Private constructor — prevent external instantiation
   2. Private static instance — class owns its only instance
   3. Public static getInstance() — the only access point
```

**Q3. What is the difference between Lazy and Eager Singleton?**
```
A. Lazy  → instance created only when first requested (getInstance() called)
           Pro: saves memory if instance may not be needed
           Con: not thread safe by default

   Eager → instance created when class is loaded by JVM
           Pro: thread safe (JVM handles it), simple
           Con: wastes resources if instance never used
```

---

### Intermediate Level

**Q4. Why is Lazy Singleton not thread safe?**
```
A. Two threads can both pass the null check simultaneously before either
   creates the instance, resulting in two separate instances being created.
   This violates the core guarantee of Singleton.
```

**Q5. What is Double Checked Locking? Why do we need two checks?**
```
A. Check 1 (outside synchronized): fast path — avoids locking on every call
   once instance is already created. Most calls after initialization take this path.

   Check 2 (inside synchronized): safety net — handles the case where two threads
   both passed Check 1 simultaneously. Only one proceeds to create the instance.

   Without Check 2: two threads pass Check 1, both enter synchronized block
   one at a time, both create an instance.

   Without Check 1: every call acquires a lock — severe performance penalty.
```

**Q6. Why is `volatile` needed in Double Checked Locking?**
```
A. Object creation has 3 steps:
   1. Allocate memory
   2. Assign reference
   3. Initialize object (call constructor)

   Without volatile, CPU can reorder to: 1 → 2 → 3
   Another thread sees non-null reference at step 2 but gets an
   uninitialized object at step 3 — extremely dangerous bug.

   volatile prevents instruction reordering and ensures all writes
   are visible across threads before the reference is exposed.
```

**Q7. How does Bill Pugh Singleton work? Why is it thread safe without synchronized?**
```
A. It uses a static inner class (SingletonHelper) that holds the instance.
   Inner classes are NOT loaded when the outer class loads.
   SingletonHelper is loaded only when getInstance() is first called.

   JVM guarantees class loading happens exactly once and is thread safe.
   So the instance is created exactly once, with no synchronized needed.
   This gives us lazy loading + thread safety + performance — all together.
```

**Q8. Why is Enum the best Singleton for serialization?**
```
A. Java guarantees that enum values are instantiated only once per JVM.
   During deserialization, Java does NOT create a new enum instance —
   it returns the existing one. This is built into the language spec.

   For all other implementations, deserialization creates a new instance
   unless you manually implement readResolve().
```

---

### Advanced Level

**Q9. How can Singleton be broken? How to prevent each?**

```
1. Reflection    → use Enum OR throw exception in constructor if instance != null
2. Serialization → use Enum OR implement readResolve() to return existing instance
3. Cloning       → override clone() and throw CloneNotSupportedException
4. Classloaders  → if two classloaders load the same class, two instances can exist
                   Fix: use a single classloader or Enum
```

**Q10. Is Singleton an Anti-Pattern?**
```
A. It is debated. Singleton is sometimes called an anti-pattern because:

   - It introduces global state — makes code harder to reason about
   - Tight coupling — code that uses getInstance() is hard to test
   - Difficult to mock in unit tests — cannot swap with a fake implementation
   - Violates Single Responsibility — class manages its own lifecycle

   Better alternative in modern code: use Dependency Injection frameworks
   like Spring, which manage singleton lifecycle for you (@Bean is singleton by default).
   This keeps your class testable and decoupled.
```

**Q11. What is the difference between Singleton and Static class?**

| | Singleton | Static Class |
|---|---|---|
| **Instantiation** | One object instance | No instance — just static methods |
| **Inheritance** | Can implement interfaces | Cannot implement interfaces |
| **Lazy loading** | ✅ Possible | ❌ Not possible |
| **Testable/Mockable** | ✅ Can be mocked | ❌ Hard to mock |
| **State** | Holds object state | Only static state |
| **When to use** | Need one shared stateful object | Utility functions with no state |

**Q12. Does Spring `@Bean` create a Singleton?**
```
A. Yes. Spring beans are Singleton by default — Spring manages one instance
   per ApplicationContext and injects the same instance everywhere.
   This is preferred over implementing Singleton manually because:
   → Your class stays a plain class (no getInstance(), no private constructor)
   → Easily testable and mockable
   → Spring handles lifecycle, thread safety, and serialization
```

**Q13. Which Singleton implementation should you use and when?**
```
A. Bill Pugh      → default choice for most cases
                    lazy + thread safe + fast + no boilerplate

   Enum           → when serialization or reflection safety is required
                    e.g. config objects, connection settings

   Double Checked → when you need lazy loading with explicit control
                    and are already familiar with volatile semantics

   Eager          → when the instance is always needed and initialization is cheap

   Never use      → Lazy (not thread safe) or ThreadSafe (performance issue)
                    in production multi-threaded code
```

---

## Real World Usage

| Usage | Why Singleton |
|---|---|
| **Logger** (Log4j, SLF4J) | One logging instance shared across entire app |
| **Database Connection Pool** | Expensive to create, shared across threads |
| **Configuration Manager** | One source of truth for app settings |
| **Cache** | Single shared cache across all requests |
| **Thread Pool** | One pool managing all worker threads |
| **Spring Beans** | Default scope is singleton per ApplicationContext |

---

## SOLID Principles and Singleton

```
Single Responsibility  → ⚠️ Violated — class manages both its business logic
                          AND its own instantiation lifecycle

Open/Closed            → ⚠️ Difficult to extend without modifying getInstance()

Dependency Inversion   → ❌ Violated when code calls Singleton.getInstance() directly
                          — tightly couples caller to concrete class
                          Fix: inject via constructor instead of calling getInstance()
```

This is why Singleton is considered an anti-pattern by some — use Dependency Injection where possible.

---

## Key Takeaway

> **"One instance. One access point. One source of truth."**

When you need exactly one shared object across your entire application —
that is when Singleton makes sense.  
But always prefer **Dependency Injection** over manual Singleton in production code —
it gives you the same one-instance guarantee without the testability and coupling problems.

| If you need | Use |
|---|---|
| Best overall Singleton | Bill Pugh |
| Serialization safe | Enum |
| Spring application | `@Bean` (default scope) |

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)