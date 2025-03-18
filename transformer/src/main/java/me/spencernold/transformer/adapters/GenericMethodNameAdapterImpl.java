package me.spencernold.transformer.adapters;

public class GenericMethodNameAdapterImpl extends MethodNameAdapter {

    public GenericMethodNameAdapterImpl(String classNameRaw) {
        super(classNameRaw);
    }

    @Override
    public String adapt(String s) {
        return s;
    }
}
