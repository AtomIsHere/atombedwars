package com.github.atomishere.atombedwars.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameters {
    String description();
    String usage();
    String aliases() default "";
}
