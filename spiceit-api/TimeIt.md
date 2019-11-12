# TimeIt
Logs methods execution time.  
Find out how long your methods take to run!

## How to use it
Just annotate your method with the `@TimeIt` annotation. That's it.

## Examples
```java
@TimeIt
private static String timeItExampleA(String param) { 
    ...
}
```
```java
@TimeIt(logLevel = LogLevel.WARN)
private static void timeItExampleB() {
    ...
}
```

## Attributes
It works out of the box, but you can still customize it.
- **order** *int*  
The processing order of this annotation.  
If a method has multiple SpiceIt annotations,
they are processed sequentially in this order (ascending).
- **logPattern** *String*  
The log pattern for method execution time.
- **logLevel** *LogLevel*  
The log level for method execution time.

Check out examples and how to customize [log patterns](LogPatterns.md).
