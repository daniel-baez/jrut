# JRut

A reasonable Rut implementation... in Java

# What's a Rut anyway?

It's Chile's SSN.

# Why make a library out of this?

- It's format implicitly allows for a tiny validation.
- An open source should, in time, provide a reasonable set of tests.
- Low compatibility (Java 5).
- No dependencies.

# How to use it?

The usual:

```
maven depedency
```

or

```
gradle depedency
```

and then...

```java
import cl.daplay.jrut.JRut;

// Handles multiple formats
final JRut a = new JRut("5252856-9");
final JRut b = new JRut("5.252.856-9");
final JRut c = new JRut("52528569");

// Throws IllegalArgumentException on invalid arguments
try {
    final JRut c = new JRut("5252856K");
} catch (IllegalArgumentException ex) {
    // ...
}

// Pretty print
final JRut a = new JRut("52528569");
// prints "5.252.856-9"
System.out.println(a);

```

# Building

Just run

`./gradlew build`
