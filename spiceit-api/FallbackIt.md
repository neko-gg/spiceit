# FallbackIt
Invokes a fallback method when a method completes exceptionally.  
Do not give up yet!

## How to use it
Just annotate your method with the `@FallbackIt` annotation.
Specify the fallback method name in the `fallbackMethod` attribute.
That's it.

## Examples
```java
@FallbackIt(fallbackMethod = "fallbackMethodA")
private static String fallbackItExampleA(String param) { 
    ...
}

private static String fallbackMethodA(String param) { 
    ...
}
```
```java
@FallbackIt(fallbackMethod = "fallbackMethodB", fallbackOnNull = true)
private static String fallbackItExampleB(String param) { 
    ...
}

private static String fallbackMethodB(String param) { 
    ...
}
```

## Attributes
It only needs `fallbackMethod` to work out of the box, but you can still customize it.
- **order** *int*  
The processing order of this annotation.  
If a method has multiple SpiceIt annotations,
they are processed sequentially in this order (ascending).
- **fallbackMethod** *String*  
The name of the fallback method to invoke.  
Must have the same parameters (in type, number, and order) as the calling method.  
Must have the same return type.  
Must be callable from the calling method (for example, it must be `static` if calling method is `static`).
- **triggeringExceptions** *Class<? extends Throwable>[]*  
The exception types for which to invoke the fallback method.
- **ignoredExceptions** *Class<? extends Throwable>[]*  
The exception types for which NOT to invoke the fallback method.
- **fallbackOnNull** *boolean*  
Whether to invoke the fallback method when `null` is returned.

The fallback method is invoked when the calling method throws an exception
contained in `triggeringExceptions` **and** not contained in `ignoredExceptions`.
