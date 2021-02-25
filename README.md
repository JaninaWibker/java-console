# Java Console

Logging in Java is annoying, you need to implement toString all over the place, can't inspect object, ...
Logging in the browser is however very pleasant.

This library is trying to fix some of this by providing an API which somewhat mirrors the console object in JavaScript.

Reflections allow inspection of objects so why not use it to build a *good* logging system?

## Installation

TODO

## Usage

### Console

Just import the Console package and you're good to go.

`log`, `info`, `warn`, `err` all pretty much act the same, the only difference is the prefix (`LOG`, `INFO`, `WARN`, `ERR`) and the color (if colors aren't disabled).

You can pass a variable amount of arguments to each method and all arguments will be printed out comma (,) seperated.

```java
Console.log("string message", "another string message");
// -> [LOG;<timestamp>] "string message", "another string message"

Console.log(some_object);
// -> [LOG;<timestamp>] Some_class {
//   -private_value: Type = <value>
//   +public_value: Type = <value>
//   #static_value: Type = <value>
//
//   -private_method(<args>): <return_type>
//   +public_method_with_generic<T>(<args>): <return_type>
// }
```

You can pass a ConsoleOptions object as the first argument to all of the methods to customize the output.

> If ConsoleOptions is the *only* argument it behaves as if it were any other object.
> Without this you couldn't print ConsoleOptions well.

Incase you don't want to print to stdout you can also use the `s_`-prefixed methods: `s_log`, `s_info`, `s_warn`, `s_err`.
These all return strings which would otherwise be printed to stdout.
The only difference between these strings is that they don't have a trailing newline.


### ConsoleOptions

ConsoleOptions objects control the output given by the `log`, `s_log`, ... methods.

The following properties exist:

| property                          | description | example | default |
| :-------------------------------- | :---------- | :------ | :------ |
| `short_names: boolean`            | omit package from type     | "String" instead of "java.lang.String" | true |
| `abbreviated_names: boolean`      | abbreviate package in type | "j.l.String" instead of "java.lang.String" | false |
| `methods: boolean`                | print methods | "+toString(): String" | true |
| `json: boolean`                   | output json (json doesn't print methods, types, ...) | { "key": value } | false |
| `skip_getter_setter: boolean`     | if getters & setters exist for a value ignore them and make the value public (**NOT YET IMPLEMENTED**) |  | false |
| `skip_enclosing_scope: boolean`   | ignore enclosing scope of inner classes entirely (if off: not descending) | "" instead of "this$0: \<outer class\> = \<not descending\>" | true |
| `colors: boolean`                 | colorful output |  | true |
| `timestamp: boolean`              | print timestamp | "\[LOG;2021-02-25T12:34:56\] ... | true |
| `indent: int`                     | number of spaces for indentation |  | 2 |
| `depth: int`                      | maximum depth for descending |  | 3 |
| `dateformat: SimpleDateFormat`    | dateformat for timestamp |  | ISO 8601 |

You can set these values through the following ways:

1. Just setting the values as they are all public
2. Using the appropriate methods (which can be chained but are **not immutable**):
  - `property()` to turn a boolean value on or
  - `no_property()` to turn a boolean value off
  - `property(value)` to set any kind of property to the supplied value
3. Using the constructor

### Annotations

If you want to customize the printing of a few select classes you can add annotations to values which change the behaviour of the console.

**`SkipDescend`** skips descending into the value that holds this annotation

**`SkipEntirely`** completely omits this value from all log output. This can also be used on methods

**`UseToString`** will use the `toString()` method of the value.

> A few types will always use toString automatically.
> These are (for now) only the autoboxed versions of primitives but this list might be expanded sometime to also include other classes like StringBuilder, date related classes or similar
