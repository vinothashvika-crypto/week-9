# Week 9 – Quiz and Concept Question Answers

Topic: abstraction, abstract classes and interfaces.

## Part B – Quiz

| Q | Answer | Why |
|---|--------|-----|
| 1 | **C** | Abstraction hides *how* an object works and shows only *what* it does. |
| 2 | **C** | An abstract class can never be instantiated directly, even if it has no abstract methods. |
| 3 | **B** | A class that leaves an inherited abstract method unimplemented must itself be declared `abstract`, or it won't compile. |
| 4 | **C** – `private` | A private method can't be seen by subclasses, so it could never be overridden. |
| 5 | **C** | Interface fields are implicitly `public static final` (constants). |
| 6 | **C** | A class can implement any number of interfaces, even while it extends a class. |
| 7 | **D** – `public` | Interface methods are implicitly public, and an implementation can't reduce visibility. |
| 8 | **C** | Interfaces carry no instance state, so multiple inheritance of interfaces avoids diamond-problem conflicts over inherited data. |
| 9 | **D** | Two conflicting default methods cause a compile-time error until the class overrides the method, optionally choosing one with `InterfaceName.super.method()`. |
| 10 | **B** | An abstract class's constructor runs, through `super(...)`, when a subclass object is created. |

## Part C – Concept Questions

**1. Abstraction in my own words, with an example.**
Abstraction means exposing only the operations a user needs and hiding how they work. Example: a food-delivery app. The user sees *search restaurant, add to cart, pay, track order*. Hidden from them are restaurant availability checks, delivery-partner assignment, route calculation, payment-gateway calls and retry logic. The user can order food without knowing any of that.

**2. Abstraction vs. encapsulation.**
Abstraction is about design: deciding *what* to show, through abstract methods and interfaces. Encapsulation is about protection: *keeping data safe* with private fields and controlled getters and methods. They work together in one class. A `BankAccount` exposes `deposit()`, `withdraw()` and `getBalance()` (abstraction: you don't see how they work) while keeping `balance` private and validating every change (encapsulation: nobody can set it to an invalid value).

**3. Why an abstract class can have a constructor.**
An abstract class often has fields that all subclasses share, such as `name` or `units`. Its constructor initializes them once and can validate them, so each subclass doesn't repeat that code. The constructor never runs on its own. It runs when a *subclass* object is created, either through an explicit `super(...)` call or the implicit one at the start of the subclass constructor, before the subclass's own constructor body.

**4. Why an abstract method can't be `private`, `static` or `final`.**
- **private**: subclasses can't see a private method, so they could never provide the required implementation.
- **static**: static methods belong to the class and are not overridden (they are hidden, not dynamically dispatched). An abstract static method could never get a polymorphic implementation.
- **final**: `final` forbids overriding, while `abstract` *requires* overriding. Using both is a contradiction.

**5. The diamond problem.**
If classes B and C both extend A and override the same method, and D extends both B and C, it's ambiguous which version (and which copy of A's fields) D inherits. Java avoids this by allowing only one superclass, so there is a single chain of state and implementation. Interfaces have no instance fields, so implementing many of them can't create duplicated state. If two interfaces supply the same `default` method, the compiler forces the class to override it and resolve the conflict explicitly.
