package helloworld;

public class Program implements Runnable {

    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }

    @Override
    public void run() {

        String text = "Hello World";
        HelloWorld helloWorld = new HelloWorld(text);

        helloWorld.shortInvitation();
        helloWorld.longInvitation();
    }
}
