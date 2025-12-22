# 🚀 RecordRules v1.0.1 — Initial Release

RecordRules provides a clean, readable way to enforce business logic and data integrity within Java Records. By leveraging the compact constructor pattern, it allows developers to validate fields and collect all violations into a single, structured exception.

### ✨ Key Features

*   **Fluent API:** Chainable validation rules that read like plain English (e.g., `.required().email().matches(...)`).
*   **Record-First Design:** Optimized for Java Records’ compact constructors to ensure objects are valid upon instantiation.
*   **Comprehensive Error Collection:** Unlike standard `assert` statements, RecordRules evaluates all provided rules and returns a complete map of all field violations.
*   **Zero Dependencies:** A lightweight footprint for your project.
*   **Built-in Rules:**
    *   **String Validation:** Required (non-null), Not Blank, Email format, and Regex pattern matching.
    *   **Number Validation:** Minimum and Maximum range checking (supports `Integer` and `Long`).

### 📦 Installation (Maven)

Add the following to your `pom.xml`:

```xml
<dependency>
    <groupId>com.joseph</groupId>
    <artifactId>record-rules</artifactId>
    <version>1.0.1</version>
</dependency>
```

### 🛠 Usage Example

Implement validation directly in your Record's compact constructor:

```java
public record User(String email, int age, String username) {
    public User {
        RecordRules.check(
            Rule.on(email, "email").required().email(),
            Rule.on(age, "age").min(18).max(120),
            Rule.on(username, "username").required().notBlank()
        );
    }
}
```

### ⚠️ Error Handling

When validation fails, a `RecordValidationException` is thrown. It contains a structured map of all errors:

```java
try {
    new User("invalid-email", 15, "");
} catch (RecordValidationException e) {
    // Access error map: {email=[must be a valid email], age=[must be at least 18], username=[must not be blank]}
    Map<String, List<String>> errors = e.getErrors();
}
```

### ⚙️ Technical Requirements
*   **Java Version:** Java 21 or higher (Required for Record features and modern compiler support).
*   **Build System:** Maven.

---
*Developed by Joseph Meghanath.*
