# JRut

A reasonable Rut implementation... in Java.

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

// throws IllegalArgumentException on invalid arguments
final JRut rut = new JRut("6363525-5");
```

# Building

Just run

`./gradlew build`
