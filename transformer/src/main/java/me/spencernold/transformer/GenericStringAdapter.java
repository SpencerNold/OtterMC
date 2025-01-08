package me.spencernold.transformer;

class GenericStringAdapter implements Adapter<String, String> {
    public String adapt(String string) {
        return string;
    }
}
