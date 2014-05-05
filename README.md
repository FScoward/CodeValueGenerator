# Code value generator
Excel to Java code

## How to use

1. Prepare excel file.
1. Edit config file(application.conf)
1. Run.
```
sbt run
```

## create .jar file
```
sbt assembly
```

[sbt-assembly](https://github.com/sbt/sbt-assembly "sbt-assembly")

## java -jar

```
java -jar CodeValueGenerator-assembly-1.0.jar
```

or

```
java -jar -Dconfig.file=application.conf CodeValueGenerator-assembly-1.0.jar
```
