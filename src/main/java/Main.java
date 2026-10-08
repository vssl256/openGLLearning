public class Main {

    public static void main() {
        long start = System.nanoTime();
        Engine engine = new Engine();
        long end = System.nanoTime();
        System.out.println( ( end - start ) / 1000000L + "ms" );
        engine.run();
    }
}
