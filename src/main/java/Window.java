import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.util.freetype.FreeType.*;
import static org.lwjgl.stb.STBTruetype.STBTT_MAC_EID_ARABIC;

public class Window {

    public long id;
    public int width;
    public int height;
    public String title;

    public Window( int width, int height, String title ) {
        this.width = width;
        this.height = height;
        this.title = title;

        init();
    }

    private void init() {
        glfwInit();

        glfwDefaultWindowHints();
        glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 3 );
        glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 3 );
        glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE );

        id = glfwCreateWindow( width, height, title, NULL, NULL );
        if ( id == NULL ) {
            System.err.println( "Failed to create GLFW window" );
            glfwTerminate();
        }

        glfwMakeContextCurrent( id );
        glfwSwapInterval( 0 );

        GL.createCapabilities();

        glfwSetFramebufferSizeCallback( id, ( _, width, height ) -> {
            glViewport( 0, 0, width, height );
        } );
    }

    public void inputHandler() {
        if ( glfwGetKey( id, GLFW_KEY_ESCAPE ) == GLFW_PRESS ) {
            glfwSetWindowShouldClose( id, true );
        }
    }
}
