BriefIO
-------

Convenient wrappers around common IO operations.



Examples of succinct calls, which do not 
need typed exceptions:


```java

private static void examples() {
    for (java.lang.String line : briefj.BriefIO.readLines("src/test/resources/test.csv"))
        java.lang.System.out.println(line);
    for (java.lang.String line : briefj.BriefIO.readLinesFromResource("/test.csv"))
        java.lang.System.out.println(line);
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv"))
        java.lang.System.out.println(line);
}
```<sub>Test</sub>

If you want to add typed exception back (e.g., later in development),
just add ``.check()``:


```java

private static void examples2() throws java.io.IOException {
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").check())
        java.lang.System.out.println(line);
}
```

Charset in IO operations are optional.
To change the default charset, set the following field:


```java

public static java.nio.charset.Charset defaultCharset = com.google.common.base.Charsets.UTF_8;
```

