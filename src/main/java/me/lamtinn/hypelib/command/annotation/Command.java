package me.lamtinn.hypelib.command.annotation;

import me.lamtinn.hypelib.command.CommandTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    String name();

    String[] aliases() default {};

    CommandTarget target() default CommandTarget.BOTH;

    int minArgs() default -1;
}
