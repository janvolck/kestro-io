package be.kestro.io.core.api;

public class InputEvent {

    private final String name;
    private final boolean isHigh;

    public InputEvent(String name, boolean isHigh){

        this.name = name;
        this.isHigh = isHigh;
    }

    public String name(){
        return name;
    }

    public boolean isHigh() {
        return isHigh;
    }

    public boolean isLow(){
        return !isHigh;
    }
}
