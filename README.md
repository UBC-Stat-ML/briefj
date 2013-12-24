BriefIO
-------

Convenient wrappers around common IO operations.



Examples of succinct calls, which do not 
need typed exceptions, and also maintain memory efficiency (i.e. they are
not dumped into a large list, so file that do not fit in memory can still
be iterated over):


```java

private static void examples() {
    for (java.lang.String line : briefj.BriefIO.readLines("src/test/resources/test.csv"))
        java.lang.System.out.println(line);
    for (java.lang.String line : briefj.BriefIO.readLinesFromResource("/test.csv"))
        java.lang.System.out.println(line);
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv"))
        java.lang.System.out.println(line);
}
```

If you want to add typed exception back (e.g., later in development),
just add ``.check()``:


```java

private static void examplesTyped() throws java.io.IOException {
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").check())
        java.lang.System.out.println(line);
}
```

Charset in IO operations are optional.
To change the default charset, set the following field:


```java

public static java.nio.charset.Charset defaultCharset = com.google.common.base.Charsets.UTF_8;
```
<sub>From:[briefj.DefaultCharset](src/main/java//briefj/DefaultCharset.java)</sub>

Returning a ``FluentIterable`` (from the guava project), it is easy to limit, filter, etc
(see guava project for more):



```java

private static void examplesFluent() {
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").skip(1).limit(10))
        java.lang.System.out.println(line);
}
```

