package sensor;

abstract class Sensor {

    protected String name;
    Object value = null;

    Sensor(String name) {
        this.name = name;
    }

    void turnOn() {}
    void turnOff() {}

    abstract void measurement();

    String getName() {
        return name;
    }
    Object getValue() {
        return value;
    }
}
