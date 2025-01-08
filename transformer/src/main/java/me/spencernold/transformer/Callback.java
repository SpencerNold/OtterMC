package me.spencernold.transformer;

public class Callback {

    private boolean cancel;

    public void setCanceled(boolean cancel) {
        this.cancel = cancel;
    }

    public boolean isNotCanceled() {
        return !cancel;
    }
}