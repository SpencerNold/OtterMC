package me.spencernold.transformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transformer {
    String className();

    boolean initialize() default false;
    Class<? extends Adapter<String, String>> adapter() default GenericStringAdapter.class;
}
