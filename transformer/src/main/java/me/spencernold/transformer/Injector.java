package me.spencernold.transformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Injector {
    me.spencernold.transformer.Target target();
    String name();

    int opcode() default 0; // NOP
    int ordinal() default 0;

    Class<? extends Adapter<String, String>> adapter() default GenericStringAdapter.class;
}
