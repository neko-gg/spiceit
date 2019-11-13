# SpiceIt Injector

The core of SpiceIt, where the juicy stuff happens.  
If you just want to use SpiceIt, you're probably in the wrong place.

## Behind the scenes
SpiceIt works by modifying the bytecode: the `.class` files compiled from your `.java` files.  
It does so with the help of [Javassist](https://www.javassist.org/), a powerful bytecode engineering library.
