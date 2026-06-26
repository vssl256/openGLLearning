import java.util.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Engine {

    public static final float MOVEMENT_SPEED = 30f;

    private Window win;
    private Scene scene;
    private DebugUI debugUI;
    private Shader shaderProgram;
    private Render render;
    private Transformation transformation;

    private Character chara;

    public static double dt;

    public Engine() {
        init();
    }

    public void init() {;
        win = new Window( 640, 640, 180,"testWindow" );
        transformation = new Transformation( 70f, 1, 0.01f, 1000.0f );
        shaderProgram = new Shader( "test" );
        scene = new Scene( transformation, shaderProgram );
        render = new Render( scene, shaderProgram, win, transformation );

        chara = new Character( transformation, win );
        debugUI = new DebugUI( win, chara );
        //Mesh mesh = new Mesh( new float[] {
        //        0.5f,   0.5f,   -1.00f,      0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
        //        0.5f,   -0.5f,  -1.00f,      1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
        //        -0.5f,  -0.5f,  -1.00f,      1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
        //        -0.5f,  0.5f,   -1.00f,      1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        //}, "bricks", GL_FILL, shaderProgram );
        //Mesh mesh2 = new Mesh( new float[] {
        //        0.5f,   0.5f,   -1.00f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
        //        0.5f,   -0.5f,  -1.00f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
        //        0.5f,   -0.5f,  0f,     1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
        //        0.5f,   0.5f,   0f,     1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        //}, "bricks", GL_FILL, shaderProgram );
        //Mesh mesh3 = new Mesh( new float[] {
        //        -0.5f,   0.5f,   0f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
        //        -0.5f,   -0.5f,  0f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
        //        -0.5f,   -0.5f,  -1.00f,     1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
        //        -0.5f,   0.5f,   -1.00f,     1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        //}, "bricks", GL_FILL, shaderProgram );
        //Mesh mesh4 = new Mesh( new float[] {
        //        0.5f,   0.5f,  0f,     0.0f, 1.0f, 1.0f,   1.0f, 1.0f,
        //        0.5f,   0.5f,   -1.00f,     1.0f, 1.0f, 0.0f,   1.0f, 0.0f,
        //        -0.5f,   0.5f,  -1.00f,     1.0f, 0.0f, 1.0f,   0.0f, 0.0f,
        //        -0.5f,   0.5f,   0f,     1.0f, 1.0f, 1.0f,   0.0f, 1.0f
        //}, "bricks", GL_FILL, shaderProgram );
        Mesh axisX = new Mesh( new float[] {
                 250.0f,   0f,  0f,     1.0f, 0.0f, 0.0f,   1.0f, 1.0f,
                 250.0f,   0f,  0f,     1.0f, 0.0f, 0.0f,   1.0f, 0.0f,
                -250.0f,   0f,  0f,     1.0f, 0.0f, 0.0f,   0.0f, 0.0f,
                -250.0f,   0f,  0f,     1.0f, 0.0f, 0.0f,   0.0f, 1.0f
        }, "grass", GL_LINE, shaderProgram );
        Mesh axisY = new Mesh( new float[] {
                0f,   250f,  0f,     0.0f, 1.0f, 0.0f,   1.0f, 1.0f,
                0f,   250f,  0f,     0.0f, 1.0f, 0.0f,   1.0f, 0.0f,
                0f,   -250f,  0f,     0.0f, 1.0f, 0.0f,   0.0f, 0.0f,
                0f,   -250f,  0f,     0.0f, 1.0f, 0.0f,   0.0f, 1.0f
        }, "grass", GL_LINE, shaderProgram );
        Mesh axisZ = new Mesh( new float[] {
                0f,   0f,  250f,     0.0f, 0.0f, 1.0f,   1.0f, 1.0f,
                0f,   0f,  250f,     0.0f, 0.0f, 1.0f,   1.0f, 0.0f,
                0f,   0f,  -250f,     0.0f, 0.0f, 1.0f,   0.0f, 0.0f,
                0f,   0f,  -250f,     0.0f, 0.0f, 1.0f,   0.0f, 1.0f
        }, "grass", GL_LINE, shaderProgram );
        //scene.add( axisX );
        //scene.add( axisY );
        //scene.add( axisZ );
        //scene.add( mesh2 );
        //scene.add( mesh3 );
        //scene.add( mesh );
        //scene.add( mesh4 );
        //Cube cube = new Cube( 0, -0.5f, -1, 1, "bricks", shaderProgram, scene );
        Random random = new Random();
        float max = 25f;
        float min = -max;
        float minSize = 0.5f;
        float maxSize = 3f;
        int count = 2000;
        glUseProgram( shaderProgram.id );
        shaderProgram.createUniform( "min" );
        shaderProgram.createUniform( "max" );
        shaderProgram.setUniform1f( "min", min );
        shaderProgram.setUniform1f( "max", max );
        Texture brick = new Texture( "bruckHD" );
        Texture grass = new Texture( "grass" );
        Texture icon = new Texture( "icon" );
        for ( int i = 0; i < count; i+=0 ) {
            float size = minSize + random.nextFloat( maxSize - minSize );
            float x = min + random.nextFloat( max - min ) - size;
            float y = min + random.nextFloat( max - min ) - size;
            float z = min + random.nextFloat( max - min ) - size;
            float rx = random.nextFloat( 360 );
            float ry = random.nextFloat( 360 );
            float rz = random.nextFloat( 360 );
            float sx = random.nextFloat( 0.5f );
            float sy = random.nextFloat( 0.5f );
            float sz = random.nextFloat( 0.5f );
            float d = org.joml.Math.sqrt( ( x - 0 ) * ( x - 0 ) + ( y - 0 ) * ( y - 0 ) + ( z - 0 ) * ( z - 0 ) );
            if ( d + size/2 < max && d + size/2 > 16 ) {
                new Cube( x, y, z, size, brick, shaderProgram, scene, transformation, rx, ry, rz, sx, sy, sz );
                i++;
            }
        }
        new Cube( 0f, -21.7f, 0f, 40f, grass, shaderProgram, scene, transformation, 0, 0, 0, 0, 0, 0 );
        new Cube( 3f, -1.7f/2f, 3f, 2f, icon, shaderProgram, scene, transformation, 0, 0, 0, 0, 0, 0 );
        System.out.println( scene.cubes.size() + " / " + count );
    }

    public void run() {
        double lastTime = glfwGetTime();
        int frameCount = 0;
        double firstFrame;
        double deltaTime = 0;

        while ( !glfwWindowShouldClose( win.id ) ) {
            firstFrame = glfwGetTime();

            //debugUI.speed = transformation.moveToward( win, deltaTime );
            //input();
            //if ( fly ) transformation.flight( win, deltaTime );
            //else transformation.gravityTest( win, deltaTime );

            chara.physicsStep( deltaTime );

            render.render();

            memTest();

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
            dt = deltaTime;
            //debugUI.deltaTime = Transformation.pitch;
        }
        glfwFreeCallbacks( win.id );
        glfwDestroyWindow( win.id );
        glfwTerminate();
    }

    private boolean fly = false;
    private void input() {
        if ( win.keys[ GLFW_KEY_C ] ) {
            fly = !fly;
            win.keys[ GLFW_KEY_C ] = false;
        }
    }

    final long MB = 1024 * 1024;
    private void memTest() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();

        long usedMemory = totalMemory - freeMemory;

        debugUI.usedMemory = usedMemory / MB;
        debugUI.maxMemory = maxMemory / MB;
    }
}
