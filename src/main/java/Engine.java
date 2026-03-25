import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Engine {

    private Window win;
    private Scene scene;
    private DebugUI debugUI;
    private Shader shaderProgram;
    private Render render;

    public Engine() {
        init();
    }

    public void init() {;
        win = new Window( 640, 640, "testWindow" );
        scene = new Scene();
        shaderProgram = new Shader( "test" );
        render = new Render( scene, shaderProgram, win );
        debugUI = new DebugUI( win );
        Mesh mesh = new Mesh( new float[] {
                0.5f,   0.5f,   -1.05f,      0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
                0.5f,   -0.5f,  -1.05f,      1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
                -0.5f,  -0.5f,  -1.05f,      1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
                -0.5f,  0.5f,   -1.05f,      1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        }, "bricks", GL_FILL, shaderProgram );
        Mesh mesh2 = new Mesh( new float[] {
                0.5f,   0.5f,   -1.05f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
                0.5f,   -0.5f,  -1.05f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
                0.5f,   -0.5f,  -.55f,     1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
                0.5f,   0.5f,   -.55f,     1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        }, "bricks", GL_FILL, shaderProgram );
        Mesh mesh3 = new Mesh( new float[] {
                -0.5f,   0.5f,   -.55f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
                -0.5f,   -0.5f,  -.55f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
                -0.5f,   -0.5f,  -1.05f,     1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
                -0.5f,   0.5f,   -1.05f,     1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        }, "bricks", GL_FILL, shaderProgram );
        Mesh grass = new Mesh( new float[] {
                 2.0f,   -0.5f,  2.0f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
                 2.0f,   -0.5f,  -2.0f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
                -2.0f,   -0.5f,  -2.0f,     1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
                -2.0f,   -0.5f,  2.0f,     1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        }, "grass", GL_FILL, shaderProgram );
        scene.add( grass );
        scene.add( mesh2 );
        scene.add( mesh3 );
        scene.add( mesh );
    }

    public void run() {
        double lastTime = glfwGetTime();
        int frameCount = 0;
        double firstFrame;
        double deltaTime;
        while ( !glfwWindowShouldClose( win.id ) ) {
            firstFrame = glfwGetTime();
            win.inputHandler();

            render.render();

            debugUI.run();

            glfwSwapBuffers( win.id );
            glfwPollEvents();

            double currentTime = glfwGetTime();
            frameCount++;
            if ( currentTime - lastTime >= 1.0 ) {
                glfwSetWindowTitle( win.id, frameCount + " FPS" );
                debugUI.currentFPS = frameCount;
                frameCount = 0;
                lastTime = currentTime;
            }
            deltaTime = glfwGetTime() - firstFrame;
            debugUI.deltaTime = deltaTime;
        }
        glfwTerminate();
    }
}
