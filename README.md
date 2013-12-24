BriefIO
-------

Convenient wrappers around common IO operations.



Examples of succinct calls, which do not 
need typed exceptions, and also maintain memory efficiency (i.e. they are
not dumped into a large list, so file that do not fit in memory can still
be iterated over):


```java

@org.junit.Test
public void examples() {
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

@org.junit.Test
public void examplesTyped() throws java.io.IOException {
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

@org.junit.Test
public void examplesFluent() {
    for (java.lang.String line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").skip(1).limit(10))
        java.lang.System.out.println(line);
}
```

Convenient access to CSV files:


```java

@org.junit.Test
public void examplesCSV() {
    for (java.util.List<java.lang.String>  line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").splitCSV().limit(10))
        java.lang.System.out.println(line);
}
```

Which can also be indexed by the name of the columns of the first row via a map:


```java

@org.junit.Test
public void examplesCSVMap() {
    for (java.util.Map<java.lang.String, java.lang.String>  line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").indexCSV().limit(10))
        java.lang.System.out.println(line);
}
```

Different CSV options can be used (see au.com.bytecode.opencsv for details):


```java

@org.junit.Test
public void examplesCSVCustom() {
    for (java.util.Map<java.lang.String, java.lang.String>  line : briefj.BriefIO.readLinesFromURL("http://stat.ubc.ca/~bouchard/pub/geyser.csv").indexCSV(new au.com.bytecode.opencsv.CSVParser(';')).limit(10))
        java.lang.System.out.println(line);
}
```

