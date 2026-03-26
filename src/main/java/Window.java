import org.joml.Math;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

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
import static org.lwjgl.opengl.GL33.*;

public class Window {

    public long id;
    private int width, height;
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    private boolean resized = false;
    public boolean isResized() { return resized; }
    public void setResized( boolean isResized ) { this.resized = isResized; }

    private int windowedWidth, windowedHeight;
    private int x, y;

    public double lastMouseX;
    public double lastMouseY;
    public String title;

    public int vSync = 1;
    public GLFWVidMode mode;
    public long monitor;

    private boolean fullscreenMode = false;
    private boolean firstFrame = true;
    private boolean guiMode = false;
    private boolean debugMeshMode = false;

    public Window( int width, int height, String title ) {
        this.width = width;
        this.height = height;
        this.title = title;
        windowedWidth = width;
        windowedHeight = height;

        init();
    }
    private float yaw = -90f, pitch = 0f;
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

        glfwSwapInterval( vSync );

        GL.createCapabilities();

        monitor = glfwGetPrimaryMonitor();
        mode = glfwGetVideoMode( monitor );

        glfwSetInputMode( id, GLFW_CURSOR, GLFW_CURSOR_DISABLED );

        glfwSetFramebufferSizeCallback( id, ( _win, width, height ) -> {
            this.resized = true;
            glViewport( 0, 0, width, height );
            if ( !fullscreenMode ) {
                windowedWidth = width;
                windowedHeight = height;
            }
            this.width = width;
            this.height = height;
        } );

        getPos();
        glfwSetWindowPosCallback( id, ( _win, x, y ) -> {
            if ( !fullscreenMode ) {
                this.x = x;
                this.y = y;
            }
        } );

        glfwSetCursorPosCallback( id, ( _win, mouseX, mouseY ) -> {
            if ( guiMode ) return;
            if ( firstFrame ) {
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                firstFrame = false;
            }
            double xOffset = mouseX - lastMouseX;
            double yOffset = mouseY - lastMouseY;

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            yaw += xOffset;
            pitch += yOffset;
            //pitch = Math.clamp( -89f, 89f, pitch );

            Transformation.yaw += xOffset;
            Transformation.pitch += yOffset;
            Transformation.pitch = Math.clamp( -89f, 89f, Transformation.pitch );
        } );

        glfwSetKeyCallback( id, ( _win, key, _scancode, action, _mods ) -> {
            if ( action == GLFW_PRESS ) {
                switch ( key ) {
                    case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose( id, true );
                    case GLFW_KEY_V -> {
                        vSync = (vSync == 1) ? 0 : 1;
                        glfwSwapInterval( vSync );
                    }
                    case GLFW_KEY_F -> {
                        firstFrame = true;
                        if ( !fullscreenMode ) {
                            fullscreenMode = true;
                            glfwSetWindowMonitor( id, monitor, 0, 0, mode.width(), mode.height(), 5 );
                        } else {
                            fullscreenMode = false;
                            glfwSetWindowMonitor( id, NULL, x, y, windowedWidth, windowedHeight, 60 );
                        }
                    }
                    case GLFW_KEY_LEFT_ALT -> {
                        if ( !guiMode ) {
                            glfwSetInputMode( id, GLFW_CURSOR, GLFW_CURSOR_NORMAL );
                            guiMode = true;
                            firstFrame = true;
                        } else {
                            glfwSetInputMode( id, GLFW_CURSOR, GLFW_CURSOR_DISABLED );
                            guiMode = false;
                        }
                    }
                    case GLFW_KEY_P -> {
                        if ( !debugMeshMode ) {
                            glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
                            debugMeshMode = true;
                        } else {
                            glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
                            debugMeshMode = false;
                        }

                    }
                }
            }
        } );
    }

    private void getPos() {
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer posX = stack.mallocInt( 1 );
            IntBuffer posY = stack.mallocInt( 1 );
            glfwGetWindowPos( id, posX, posY );
            this.x = posX.get(0);
            this.y = posY.get(0);
        }
    }
}
