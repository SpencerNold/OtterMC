package me.spencernold.transformer;

import me.spencernold.transformer.adapters.ClassNameAdapter;
import me.spencernold.transformer.adapters.GenericClassNameAdapterImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transformer {
    String className();

    boolean initialize() default false;
    Class<? extends ClassNameAdapter> adapter() default GenericClassNameAdapterImpl.class;
}
