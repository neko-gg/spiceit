# Log patterns
Some SpiceIt features allow you to customize what they log.  
Whatever string you provide is what gets logged, and you can even use certain placeholders!

## Placeholders
* `${method.class.name}`  
The method declaring class name.  
Example: `java.lang.String`  
* `${method.class.simpleName}`  
The method declaring class simple name.  
Example: `String`  
* `${method.name}`  
The method name.  
Example: `replaceAll`  
* `${method.signature}`  
The method signature.  
Example: `replaceAll(java.lang.String, java.lang.String)`  
* `${method.longName}`  
The method long name.  
Example: `java.lang.String.replaceAll(java.lang.String,java.lang.String)`  
* `${method.return}`  
The method return value.  
Example: `the pencil is on the table`  
* `${method.exception.message}`  
The method exception message.  
Example: `String index out of range: 420`  
* `${method.args}`  
The method arguments.  
Example: `[nyan, cat]`  
* `${method.args[$index]}`, e.g.: `${method.args[1]}`  
The `$index`-th method argument, starting at 1.  
Example: `nyan`  
* `${method.time}`  
The method execution time in milliseconds.  
Example: `69`  

Please note that not all placeholders are available at all times, for example `${method.exception.message}` is only available in error patterns.
