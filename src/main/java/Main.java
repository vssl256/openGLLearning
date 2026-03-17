import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL33.*;

public class Main {

    private static int winHeight = 480;
    private static int winWidth = 480;
    private static String winTitle = "Primordial sin";

    private static float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    public static void main() {
        Window win = new Window( winWidth, winHeight, winTitle );

        Engine.run( win );

        glfwTerminate();
    }
}
