package helloworld;

public class HelloWorld {

    private String text;

    public HelloWorld(String text) {
        this.text = text;
    }

    public void shortInvitation() {
        System.out.println(text);
    }

    public void longInvitation() {
        for (int i = 0; i < 3; i++) {
            System.out.print(text + " ♡ ");
        }
        System.out.println();
    }
}
