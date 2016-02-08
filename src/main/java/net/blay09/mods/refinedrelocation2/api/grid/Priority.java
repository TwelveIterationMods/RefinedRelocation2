package net.blay09.mods.refinedrelocation2.api.grid;

public enum Priority {
    LOWEST(-2, "--"),
    LOW(-1, "-"),
    NORMAL(0, "0"),
    HIGH(1, "+"),
    HIGHEST(2, "++");

    private static Priority[] values = values();

    private int priority;
    private String buttonString;

    Priority(int priority, String buttonString) {
        this.priority = priority;
        this.buttonString = buttonString;
    }

    public static Priority fromValue(int priority) {
        for(Priority value : values) {
            if(value.priority == priority) {
                return value;
            }
        }
        return NORMAL;
    }


    public String getButtonString() {
        return buttonString;
    }
}
