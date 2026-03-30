import org.joml.Vector2d;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Engine {

    public static final float MOVEMENT_SPEED = 1f;

    private Window win;
    private Scene scene;
    private DebugUI debugUI;
    private Shader shaderProgram;
    private Render render;
    private Transformation transformation;

    public Engine() {
        init();
    }

    public void init() {;
        win = new Window( 640, 640, "testWindow" );
        scene = new Scene();
        shaderProgram = new Shader( "test" );
        transformation = new Transformation( 70f, 1, 0.01f, 1000.0f );
        render = new Render( scene, shaderProgram, win, transformation );
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
        double deltaTime = 0;

        while ( !glfwWindowShouldClose( win.id ) ) {
            firstFrame = glfwGetTime();

            inputHandler( deltaTime );

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
            debugUI.deltaTime = Transformation.pitch;
        }
        glfwFreeCallbacks( win.id );
        glfwDestroyWindow( win.id );
        glfwTerminate();
    }

    private boolean w, a, s, d;
    private Vector3f movementVector = new Vector3f();
    private void inputHandler( double dt ) {
        w = isKeyPressed( 'w' );
        a = isKeyPressed( 'a' );
        s = isKeyPressed( 's' );
        d = isKeyPressed( 'd' );


        if ( w ) movementVector.set( 0.0f, 0.0f, -1.0f );
        if ( a ) movementVector.set( -1.0f, 0.0f, 0.0f );
        if ( s ) movementVector.set( 0.0f, 0.0f, 1.0f );
        if ( d ) movementVector.set( 1.0f, 0.0f, 0.0f );

        if ( w && a ) movementVector.set( -1.0f, 0.0f, -1.0f );
        if ( a && s ) movementVector.set( -1.0f, 0.0f, 1.0f );
        if ( s && d ) movementVector.set( 1.0f, 0.0f, 1.0f );
        if ( w && d ) movementVector.set( 1.0f, 0.0f, -1.0f );

        transformation.moveToward( movementVector, dt );
    }

    private boolean isKeyPressed( char key ) {
        key = Character.toUpperCase( key );
        return glfwGetKey( win.id, ( int ) key ) == GLFW_PRESS;
    }

    private void inputHandlerDepr( double dt ) {
        if ( glfwGetKey( win.id, GLFW_KEY_A ) == GLFW_PRESS ) {
            transformation.moveToward( Directions.LEFTWARD, dt );
        } else if ( glfwGetKey( win.id, GLFW_KEY_D ) == GLFW_PRESS ) {
            transformation.moveToward( Directions.RIGHTWARD, dt );
        }
        if ( glfwGetKey( win.id, GLFW_KEY_W ) == GLFW_PRESS ) {
            transformation.moveToward( Directions.FORWARD, dt );
        } else if ( glfwGetKey( win.id, GLFW_KEY_S ) == GLFW_PRESS ) {
            transformation.moveToward( Directions.BACKWARD, dt );
        }
    }
}
