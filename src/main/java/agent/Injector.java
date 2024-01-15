package agent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Injector {
	agent.Target target();
	String name();
	
	/*
	int opcode() default 0; // NOP Opcode
	String className() default "";
	String componentName() default "";
	int ordinal() default -1;
	*/
}
