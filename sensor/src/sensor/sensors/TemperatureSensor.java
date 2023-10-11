package sensor;

public class TemperatureSensor extends Sensor {

    TemperatureSensor(String name) {
        super(name);
    }

    @Override
    void measurement() {
        value = 21.5; // stopni Celsjusza
    }
}
