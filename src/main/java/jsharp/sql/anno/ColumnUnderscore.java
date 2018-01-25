package jsharp.sql.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * support for convert db column  hello_world to java property helloWorld
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ColumnUnderscore {
}
