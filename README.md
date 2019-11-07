# SpiceIt
A collection of nice, easy to use features to make your code _spicier_.

[![Build Status](https://img.shields.io/travis/neko-gg/spiceit/develop)](https://travis-ci.org/neko-gg/spiceit)
[![Coveralls github branch](https://img.shields.io/coveralls/github/neko-gg/spiceit/develop)](https://coveralls.io/github/neko-gg/spiceit)
[![LGTM Grade](https://img.shields.io/lgtm/grade/java/github/neko-gg/spiceit?label=code%20quality)](https://lgtm.com/projects/g/neko-gg/spiceit/context:java)
[![GitHub](https://img.shields.io/github/license/neko-gg/spiceit)](LICENSE.txt)
[![nyan](https://img.shields.io/badge/nyancat-approved-ff69b4.svg?style=flat)](http://www.nyan.cat/)

With SpiceIt, this class:
![example class](resources/example-class.gif?raw=true)

produces this output:
![example output](resources/example-output.png?raw=true)

## Getting started
SpiceIt features are exposed by the [API](spiceit-api) module: go on and import it as a Maven dependency:
```xml
<dependency>
    <groupId>gg.neko.spiceit</groupId>
    <artifactId>spiceit-api</artifactId>
    <version>0.1</version>
</dependency>
```

## Making it work
SpiceIt needs to inject its magic into your bytecode.  
It can do so with either its [Maven plugin](spiceit-maven-plugin) or its [Java agent](spiceit-agent).
- **Maven plugin**  
add this plugin to your `pom.xml` plugins section: 
    ```xml
    <plugin>
        <groupId>gg.neko.spiceit</groupId>
        <artifactId>spiceit-maven-plugin</artifactId>
        <version>0.1</version>
        <executions>
            <execution>
                <goals>
                    <goal>spiceit</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    ```
- **Java agent**  
download the agent from Maven Central Repository and launch your application with this option:
    ```shell script
    -javaagent:spiceit-agent-1.0-jar-with-dependencies.jar
    ```
Check out the example projects making use of [Maven plugin](spiceit-example-parent/spiceit-example-maven-plugin) and [Java agent](spiceit-example-parent/spiceit-example-agent).
