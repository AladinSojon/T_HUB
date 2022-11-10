package net.therap.mealsystem.domain;

/**
 * @author aladin
 * @since 3/1/22
 */
public enum Day {

    SATURDAY("Saturday"),
    SUNDAY("Sunday"),
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday");

    private String label;

    Day(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
