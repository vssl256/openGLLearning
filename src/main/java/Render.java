import static org.lwjgl.opengl.GL33.*;

public class Render {

    public static void render( Scene scene ) {
        glClearColor( 0.2f, 0f, 0.2f, 1f );
        glClear( GL_COLOR_BUFFER_BIT );

        scene.draw();
    }
}
