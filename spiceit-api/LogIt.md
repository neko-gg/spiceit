# LogIt
Logs methods entry, exit and error.  
Do you have any critical methods you need to monitor closely? Worry no more!

## How to use it
Just annotate your method with the `@LogIt` annotation. That's it.

## Examples
```java
@LogIt
private static String logItExampleA(String param) { 
    ...
}
```
```java
@LogIt(errorLogLevel = LogLevel.WARN)
private static void logItExampleB() {
    ...
}
```

## Attributes
It works out of the box, but you can still customize it.
- **order** *int*  
The processing order of this annotation.  
If a method has multiple SpiceIt annotations,
they are processed sequentially in this order (ascending).
- **entryPattern** *String*  
The log pattern for method entry.
- **entryLogLevel** *LogLevel*  
The log level for method entry.
- **errorPattern** *String*  
The log pattern for method exit.
- **errorLogLevel** *LogLevel*  
The log level for method exit.
- **exitPattern** *String*  
The log pattern for method error.
- **exitLogLevel** *LogLevel*  
The log level for method error.

Check out examples and how to customize [log patterns](LogPatterns.md).
