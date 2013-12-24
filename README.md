BriefIO
=======

Convenient wrappers around IO.



Examples of succinct calls, which do not 
need typed exceptions


```java

@java.lang.SuppressWarnings(value = "unused")
private static void examples() {
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv"))
        java.lang.System.out.println(line);
}
```

If you want to add typed exception back (e.g., later in development),
just add ``.check()``:


```java

@java.lang.SuppressWarnings(value = "unused")
private static void examples2() throws java.io.IOException {
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").check())
        java.lang.System.out.println(line);
}
```

