import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {

    public static Scene scene;

    public static void run( Window win ) {
        init();

        double lastTime = glfwGetTime();
        int frameCount = 0;
        while ( !glfwWindowShouldClose( win.id ) ) {
            win.inputHandler();

            Render.render( scene );

            glfwSwapBuffers( win.id );
            glfwPollEvents();

            double currentTime = glfwGetTime();
            frameCount++;
            if ( currentTime - lastTime >= 1.0 ) {
                glfwSetWindowTitle( win.id, frameCount + " FPS" );
                frameCount = 0;
                lastTime = currentTime;
            }
        }
    }

    public static void init() {
        scene = new Scene();
        Mesh mesh = new Mesh( new float[] {
                0.5f, 0.5f, 0.0f,   0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
                0.5f, -0.5f, 0.0f,  1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
                -0.5f, 0.5f, 0.0f,  1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        }, "test" ,GL_FILL );
        scene.add( mesh );
    }
}
