package sensor;

public class Program implements Runnable {

    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }

    @Override
    public void run() {

        Sensor[] sensors = createSensors();

        for (Sensor sensor : sensors) {
            sensor.turnOn();
        }

        delayOneSecond();

        for (Sensor sensor : sensors) {
            sensor.measurement();
        }

        delayOneSecond();

        for (Sensor sensor : sensors) {
            String name = sensor.getName();
            Object value = sensor.getValue();
            System.out.println("Sensor: " + name + " = " + value);
        }

        for (Sensor sensor : sensors) {
            sensor.turnOff();
        }
    }

    private void delayOneSecond() {
        // @todo: Tu powinno być zagwarantowane opóźnienie trwające 1 sekundę.
        //        Na razie nie ma go, aby nie utrudniać zrozumienia działania
        //        całego programu.
    }

    private Sensor[] createSensors() {
        Sensor t = new TemperatureSensor("temperatura");
        Sensor p = new PressureSensor("ciśnienie");
        Sensor h = new HumiditySensor("wilgotność");
        return new Sensor[] {t, p, h};
    }
}
