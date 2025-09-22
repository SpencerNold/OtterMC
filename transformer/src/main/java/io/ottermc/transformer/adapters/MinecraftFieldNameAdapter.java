package io.ottermc.transformer.adapters;

import io.ottermc.transformer.Mapping;
import me.spencernold.transformer.adapters.FieldNameAdapter;

public class MinecraftFieldNameAdapter extends FieldNameAdapter {

    private final Mapping.Class clazz;

    public MinecraftFieldNameAdapter(String classNameRaw) {
        super(classNameRaw);
        this.clazz = Mapping.get(classNameRaw);
    }

    @Override
    public String adapt(String fieldName) {
        Mapping.Field field = clazz.getField(fieldName);
        if (field == null)
            return fieldName;
        return field.getName1();
    }
}
