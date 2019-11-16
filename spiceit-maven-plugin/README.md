# SpiceIt Maven Plugin

If you're using Maven, this is the easiest way to integrate SpiceIt in your project.

## Configuration
Add SpiceIt Maven Plugin to your `pom.xml` plugins section:
```xml
<plugin>
    <groupId>gg.neko.spiceit</groupId>
    <artifactId>spiceit-maven-plugin</artifactId>
    <version>0.2</version>
    <executions>
        <execution>
            <goals>
                <goal>spiceit</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Goals
There's just one goal: `spiceit`.  
It's bound by default to the `process-classes` lifecycle phase (i.e.: it runs automatically when you `mvn install`).  
If you want to bind it to a different phase (e.g.: to also _spice_ your test classes), just say it:
```xml
<plugin>
    <groupId>gg.neko.spiceit</groupId>
    <artifactId>spiceit-maven-plugin</artifactId>
    <version>0.2</version>
    <executions>
        <execution>
            <phase>process-test-classes</phase>
            <goals>
                <goal>spiceit</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Pros
- Easy to use
- No dealing with devops: launch your application as before
- No _spicing_ overhead at startup

## Cons
- Longer build time (but not **that** longer)
- If you want to _de-spice_ your application, you have to recompile it (consider putting this plugin in a profile to somewhat alleviate this issue)
