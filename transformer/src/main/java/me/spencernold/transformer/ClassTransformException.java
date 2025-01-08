package me.spencernold.transformer;

public class ClassTransformException extends Exception {

    public ClassTransformException(String format, Object... args) {
        super(String.format(format, args));
    }

    public ClassTransformException(Throwable throwable, String format, Object... args) {
        super(String.format(format, args), throwable);
    }
}
