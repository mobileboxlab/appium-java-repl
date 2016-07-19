package com.mobilebox.repl.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface CommandRef {

	public String desc() default "";

	public String[] params() default "";

	public String ret() default "";
}
