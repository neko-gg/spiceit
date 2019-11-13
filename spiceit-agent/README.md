# SpiceIt Agent

A Java Agent that _spices_ your project at runtime.

## Configuration
Just download SpiceIt Agent from Maven Central Repository.

## Usage
Run your application with the `-javaagent` option:  
```shell script
java -javaagent:spiceit-agent-0.1-jar-with-dependencies.jar -jar MyProject.jar
```

## Pros
- No compile time overhead
- Easy to de-_spice_, just remove the `-javaagent` option

## Cons
- Longer startup time (but not **that** longer)
- You have to keep the SpiceIt Agent `.jar` file at hand, because you will need it when launching your application
