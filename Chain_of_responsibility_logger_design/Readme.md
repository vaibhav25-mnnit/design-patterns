# Chain of Responsibility Design Pattern in Java

> A beginner-friendly guide to understanding the Chain of Responsibility Pattern with a real-world Logger example.

---

## What is the Chain of Responsibility Pattern?

The Chain of Responsibility is a **behavioral design pattern** where a request is passed along a **chain of handlers**. Each handler decides either to **process the request** or **pass it to the next handler** in the chain.

> Think of it like an escalation system — if one level can't or shouldn't handle it, it passes it to the next level.

---

## Real World Analogy

Think of a **customer support system**:

```
You raise a complaint
        │
        ▼
Level 1 Support  → can handle? → YES → resolves it
                              → NO  → passes to Level 2
        │
        ▼
Level 2 Support  → can handle? → YES → resolves it
                              → NO  → passes to Level 3
        │
        ▼
Level 3 Support  → handles all escalated issues
```

In this code, the same idea is applied to **logging**:

```
logMessage(INFO)
        │
        ▼
InfoLogger   → level matches INFO?  → YES → writes [INFO] log
                                   → NO  → passes down
        │
        ▼
DebugLogger  → level matches DEBUG? → YES → writes [DEBUG] log
                                   → NO  → passes down
        │
        ▼
ErrorLogger  → level matches ERROR? → YES → writes [ERROR] log
                                   → NO  → end of chain
```

---

## Project Structure

```
ChainOfResponsibility/
│
└── src/
    ├── Main.java          # Client — creates chain and sends log requests
    ├── LogLevel.java      # Enum — defines log levels (INFO, DEBUG, ERROR)
    ├── Logger.java        # Abstract Handler — defines chain structure
    ├── LoggerChain.java   # Chain Builder — assembles the chain
    ├── InfoLogger.java    # Concrete Handler — handles INFO logs
    ├── DebugLogger.java   # Concrete Handler — handles DEBUG logs
    └── ErrorLogger.java   # Concrete Handler — handles ERROR logs
```

---

## Key Components

| Component              | Role                                           | In This Example                            |
| ---------------------- | ---------------------------------------------- | ------------------------------------------ |
| **Handler (Abstract)** | Defines chain link structure and passing logic | `Logger`                                   |
| **Concrete Handlers**  | Each handles one specific request type         | `InfoLogger`, `DebugLogger`, `ErrorLogger` |
| **Chain Builder**      | Assembles handlers into a chain                | `LoggerChain`                              |
| **Request**            | What is being passed through the chain         | `LogLevel + message`                       |
| **Client**             | Sends request to first handler in chain        | `Main.java`                                |

---

## The Code

### LogLevel — Defines the Types of Requests

```java
public enum LogLevel {
    DEBUG,
    INFO,
    ERROR
}
```

### Abstract Handler — Defines Chain Link Structure

```java
public abstract class Logger {
    LogLevel level;            // what level THIS handler is responsible for
    protected Logger nexLogger; // reference to NEXT handler in chain — HAS-A

    public Logger(LogLevel level) {
        this.level = level;
    }

    // setter to link next handler in chain
    public void setNexLogger(Logger nexLogger) {
        this.nexLogger = nexLogger;
    }

    // core chain logic:
    // 1. if this handler's level matches — handle it
    // 2. always pass to next handler regardless
    public void logMessage(LogLevel level, String message) {
        if (this.level == level) {
            writeLog(message);         // handle it
        }
        if (this.nexLogger != null) {
            nexLogger.logMessage(level, message); // always pass forward
        }
    }

    // each concrete handler defines HOW to write the log
    protected abstract void writeLog(String message);
}
```

### Concrete Handlers — Each Handles One Log Level

```java
// handles INFO level logs
public class InfoLogger extends Logger {
    public InfoLogger(LogLevel level) {
        super(level);
    }

    @Override
    protected void writeLog(String message) {
        System.out.println("[INFO]  " + message);
    }
}

// handles DEBUG level logs
public class DebugLogger extends Logger {
    public DebugLogger(LogLevel level) {
        super(level);
    }

    @Override
    protected void writeLog(String message) {
        System.out.println("[DEBUG] " + message);
    }
}

// handles ERROR level logs
public class ErrorLogger extends Logger {
    public ErrorLogger(LogLevel level) {
        super(level);
    }

    @Override
    protected void writeLog(String message) {
        System.out.println("[ERROR] " + message);
    }
}
```

### Chain Builder — Assembles the Chain

```java
public class LoggerChain {

    // builds and links all handlers in order
    // INFO → DEBUG → ERROR
    public static Logger createLoggerChain() {
        Logger info  = new InfoLogger(LogLevel.INFO);
        Logger debug = new DebugLogger(LogLevel.DEBUG);
        Logger error = new ErrorLogger(LogLevel.ERROR);

        info.setNexLogger(debug);   // INFO  → DEBUG
        debug.setNexLogger(error);  // DEBUG → ERROR
                                    // ERROR → null (end of chain)
        return info;  // always return the FIRST handler in chain
    }
}
```

### Client — Sends Requests Into the Chain

```java
public class Main {
    public static void main(String[] args) {

        // get the first handler — client only talks to head of chain
        Logger logger = LoggerChain.createLoggerChain();

        // each message travels the entire chain
        logger.logMessage(LogLevel.INFO,  "This is a info message");
        logger.logMessage(LogLevel.DEBUG, "This is a debug message");
        logger.logMessage(LogLevel.ERROR, "This is a error message");
    }
}
```

---

## Output

```
[INFO]  This is a info message
[DEBUG] This is a debug message
[ERROR] This is a error message
```

---

## How the Chain Works Step by Step

### When `logMessage(INFO, "This is a info message")` is called:

```
InfoLogger  → level == INFO?  ✔ → writes [INFO] log
            → nextLogger exists? ✔ → passes to DebugLogger
DebugLogger → level == DEBUG? ✗ → skips
            → nextLogger exists? ✔ → passes to ErrorLogger
ErrorLogger → level == ERROR? ✗ → skips
            → nextLogger exists? ✗ → end of chain
```

### When `logMessage(ERROR, "This is a error message")` is called:

```
InfoLogger  → level == INFO?  ✗ → skips
            → nextLogger exists? ✔ → passes to DebugLogger
DebugLogger → level == DEBUG? ✗ → skips
            → nextLogger exists? ✔ → passes to ErrorLogger
ErrorLogger → level == ERROR? ✔ → writes [ERROR] log
            → nextLogger exists? ✗ → end of chain
```

---

## The Chain Visualized

```
  Client
    │
    ▼
┌─────────────┐    nextLogger    ┌─────────────┐    nextLogger    ┌─────────────┐
│ InfoLogger  │ ───────────────► │ DebugLogger │ ───────────────► │ ErrorLogger │──► null
│ level=INFO  │                  │ level=DEBUG │                  │ level=ERROR │
└─────────────┘                  └─────────────┘                  └─────────────┘
```

---

## Important Design Decision — Why Always Pass Forward?

In this implementation every handler **always passes the request forward** regardless of whether it handled it:

```java
public void logMessage(LogLevel level, String message) {
    if (this.level == level) {
        writeLog(message);        // handle if level matches
    }
    if (this.nexLogger != null) {
        nexLogger.logMessage(level, message); // ALWAYS pass forward
    }
}
```

This means every message travels the **entire chain**. This is intentional for logging — you might want multiple handlers to process the same message (e.g., write to console AND write to file).

If you want a handler to **stop** the chain after handling:

```java
public void logMessage(LogLevel level, String message) {
    if (this.level == level) {
        writeLog(message);
        return;  // stop here — don't pass forward
    }
    if (this.nexLogger != null) {
        nexLogger.logMessage(level, message);
    }
}
```

---

## Two Variants of Chain of Responsibility

| Variant                     | Behaviour                               | Use When                     |
| --------------------------- | --------------------------------------- | ---------------------------- |
| **Pass always** (this code) | Every handler sees every request        | Logging, event systems       |
| **Stop on handle**          | Chain stops once a handler processes it | Auth, validation, middleware |

---

## Chain of Responsibility vs Other Patterns

### vs Decorator Pattern

|                           | Chain of Responsibility    | Decorator                         |
| ------------------------- | -------------------------- | --------------------------------- |
| **Purpose**               | Pass request until handled | Add behaviour by wrapping         |
| **Handlers linked?**      | Yes, via nextHandler       | Yes, via wrapping                 |
| **All handlers execute?** | Optional                   | Always                            |
| **Example**               | Logger chain               | Adding features to a coffee order |

### vs Strategy Pattern

|              | Chain of Responsibility     | Strategy                      |
| ------------ | --------------------------- | ----------------------------- |
| **Handlers** | Multiple, linked in chain   | One selected at runtime       |
| **Decision** | Each handler decides itself | Client decides which strategy |
| **Flow**     | Sequential through chain    | Direct call to one strategy   |

---

## SOLID Principles Covered

| Rule                                                                      | SOLID Principle                 |
| ------------------------------------------------------------------------- | ------------------------------- |
| Each logger handles only its own level                                    | Single Responsibility Principle |
| Add new log level? Just add new Logger subclass, nothing else changes     | Open/Closed Principle           |
| `InfoLogger`, `DebugLogger`, `ErrorLogger` all substitutable for `Logger` | Liskov Substitution Principle   |
| Client depends on abstract `Logger`, not concrete loggers                 | Dependency Inversion Principle  |

---

## When to Use Chain of Responsibility?

- More than one object may handle a request and the handler is not known upfront
- You want to issue a request to one of several handlers **without specifying** which one explicitly
- You want to add or change handlers **dynamically** without touching client code
- Common use cases: **logging, middleware, auth filters, event handling, validation pipelines**

---

## Real World Usage

| Framework / Tool       | How Chain is Used                                 |
| ---------------------- | ------------------------------------------------- |
| `Java Servlet Filters` | Each filter processes request then passes to next |
| `Spring Security`      | Chain of security filters for auth, CORS, CSRF    |
| `Node.js Express`      | `app.use()` middleware chain                      |
| `Android View System`  | Touch events passed up the view hierarchy         |
| `slf4j / Log4j`        | Log level handlers chained together               |

---

## Key Takeaway

> **"Don't hardwire who handles what. Build a chain and let the request find its handler."**

The client sends a request to the **first link** in the chain.  
Each handler checks — **is this mine to handle?**  
If yes — it handles it.  
Either way — it **passes the request forward**.  
The chain ends when there are no more handlers.

---

Made with ❤️ by [@vaibhav25-mnnit](https://github.com/vaibhav25-mnnit)

---
