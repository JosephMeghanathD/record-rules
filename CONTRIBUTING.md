# Contributing to RecordRules

First off, thank you for considering contributing to RecordRules! It’s people like you who make open-source tools better for everyone.

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).

## How Can I Contribute?

### 🛠️ Improvements and Suggestions
Whether you have a bug fix, a new validation rule idea, or a performance improvement, all contributions are welcome. 

1.  **Fork** the repository to your own GitHub account.
2.  **Clone** your fork to your local machine.
3.  Create a new **branch** for your changes (e.g., `git checkout -b feat/add-date-range-rule`).
4.  Commit your changes with clear, descriptive commit messages.
5.  **Push** your branch to your fork.
6.  Open a **Pull Request (PR)** against our `master` branch.

### 🧪 Reporting Bugs
If you find a bug, please open an issue on GitHub. Include:
*   A clear description of the problem.
*   Steps to reproduce the issue.
*   The expected vs. actual behavior.
*   Your Java version (Note: This project requires **Java 21+**).

## Development Setup

To work on this project locally, you will need:
*   **Java 21** or higher.
*   **Maven** 3.x.

### Running Tests
Before submitting a PR, please ensure all tests pass:
```bash
mvn test
```
If you add a new feature or validation rule, please add corresponding test cases in `src/test/java/com/joseph/rule/child/`.

## Coding Standards
*   Follow standard Java naming conventions.
*   Keep the API fluent and readable (e.g., `Rule.on(...).required().matches(...)`).
*   Ensure Javadoc is updated for any new public methods.
*   Maintain the "Zero Dependency" philosophy of the project.

## Get in Touch

I am always open to discussing new ideas, architectural changes, or just networking with fellow developers. If you have questions about the codebase or want to collaborate more closely:

*   **LinkedIn:** [Joseph Meghanath](https://www.linkedin.com/in/joseph-meghanath-9880ba149/)
*   **Email:** josephdanthikolla@gamil.com (As listed in the Code of Conduct)

---
*Happy Coding!* 🚀
