public class Main {

    private static int winHeight = 480;
    private static int winWidth = 480;
    private static String winTitle = "Test";

    public static void main() {
        Window win = new Window( winWidth, winHeight, winTitle );

        Engine.run( win );
    }
}
