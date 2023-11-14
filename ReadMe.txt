Tasks:

Develop a console application for Clever-Bank.

Stack:
- Java 17
-Gradle
- PostgreSQL
- JDBC
- Lombok
- * Servlets
- *Docker
!!! Spring and Hibernate are not used intentionally

Requirements:
1. Implement operations of replenishment and withdrawal of funds from the account
2. Implement the possibility of transferring funds to another Clever-Bank client and
to a client of another bank. When transferring funds to another bank, use one
transaction and ensure security. Taking into account working in a multi-threaded environment
(avoid deadlock)
3. Regularly, according to the schedule (every half a minute), check whether it is necessary to accrue
percent (1% - the value is substituted from the configuration file) on the balance
invoices at the end of the month (not completed)
Checking and calculating interest must be implemented asynchronously
4. Store the values in the configuration file - .yml
5. After each operation, it is necessary to generate a check (Appendix I)
Save checks in the check folder, in the project root
6. Apply design patterns in application development
7. Follow the principles of OOP (object-oriented programming), SOLID
(SOLID principles: unity of responsibility, openness/closedness, substitution
Liskov, interface separation and dependency inversion), KISS (simplicity and
intuitiveness), DRY (don't repeat yourself) and YAGNI (don't add unnecessary features)

!!!!SQL Dump is countersCopy.txt