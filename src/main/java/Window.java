import org.joml.Math;
import org.joml.Vector2d;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
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
    public int refreshRate;
    private boolean resized = false;
    public boolean isResized() { return resized; }
    public void setResized( boolean isResized ) { this.resized = isResized; }

    private int windowedWidth, windowedHeight;
    private int x, y;

    public String title;

    public int vSync = 1;
    public GLFWVidMode mode;
    public long monitor;

    private boolean firstFrame = true;
    private boolean guiMode = false;
    private boolean debugMeshMode = false;

    private Texture icon;

    public boolean[] keys = new boolean[ GLFW_KEY_LAST ];

    private Vector2d lastMousePos = new Vector2d();
    public Vector2d mouseOffset = new Vector2d();

    public Window( int width, int height, int refreshRate, String title) {
        this.width = width;
        this.height = height;
        this.refreshRate = refreshRate;
        this.title = title;
        windowedWidth = width;
        windowedHeight = height;

        icon = new Texture( "icon", true );

        init();
    }

    private void init() {
        glfwInit();

        PointerBuffer test = glfwGetMonitors();
        while ( test != null && test.hasRemaining() ) {
            monitor = test.get();
            System.out.println( glfwGetMonitorName( monitor ) );
        }
        mode = glfwGetVideoMode( monitor );

        glfwDefaultWindowHints();
        glfwWindowHint( GLFW_CONTEXT_VERSION_MAJOR, 3 );
        glfwWindowHint( GLFW_CONTEXT_VERSION_MINOR, 3 );
        glfwWindowHint( GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE );
        glfwWindowHint( GLFW_REFRESH_RATE, refreshRate );

        id = glfwCreateWindow( width, height, title, NULL, NULL );
        if ( id == NULL ) {
            System.err.println( "Failed to create GLFW window" );
            glfwTerminate();
        }


        glfwMakeContextCurrent( id );
        glfwSetWindowIcon( id, icon.getImageBuffer() );

        glfwSwapInterval( vSync );

        GL.createCapabilities();

        glfwSetInputMode( id, GLFW_CURSOR, GLFW_CURSOR_DISABLED );
        glfwSetInputMode( id, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE );

        glfwSetFramebufferSizeCallback( id, ( _win, width, height ) -> {
            this.resized = true;
            glViewport( 0, 0, width, height );
            if ( !isFullscreen() ) {
                windowedWidth = width;
                windowedHeight = height;
            }
            this.width = width;
            this.height = height;
        } );

        getPos();
        glfwSetWindowPosCallback( id, ( _win, x, y ) -> {
            if ( !isFullscreen() ) {
                this.x = x;
                this.y = y;
            }
        } );

        glfwSetCursorPosCallback( id, ( _win, mouseX, mouseY ) -> {
            if ( guiMode ) return;
            if ( firstFrame ) {
                lastMousePos.x = mouseX;
                lastMousePos.y = mouseY;
                firstFrame = false;
            }
            mouseOffset.x += mouseX - lastMousePos.x;
            mouseOffset.y += mouseY - lastMousePos.y;

            lastMousePos.x = mouseX;
            lastMousePos.y = mouseY;
        } );

        glfwSetScrollCallback( id, ( _win, xOffset, yOffset ) -> {
            Transformation.fov -= ( float ) yOffset;
            if ( Transformation.fov < 1.0f ) Transformation.fov = 1.0f;
            if ( Transformation.fov > 90.0f ) Transformation.fov = 90.0f;
        } );

        glfwSetKeyCallback( id, ( _win, key, _scancode, action, _mods ) -> {
            if ( key >= 0 && key < GLFW_KEY_LAST ) {
                keys[ key ] = ( action != GLFW_RELEASE );
            }
            if ( action == GLFW_PRESS ) {
                switch ( key ) {
                    case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose( id, true );
                    case GLFW_KEY_V -> toggleVsync();
                    case GLFW_KEY_F -> toggleFullscreen();
                    case GLFW_KEY_LEFT_ALT -> toggleCursor();
                    case GLFW_KEY_P -> toggleDebugMode();
                    case GLFW_KEY_R -> toggleAlwaysOnTop();
                }
            }
        } );
    }

    private void toggleDebugMode() {
        if ( !debugMeshMode ) {
            glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
            debugMeshMode = true;
        } else {
            glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
            debugMeshMode = false;
        }
    }

    private void toggleCursor() {
        if ( !guiMode ) {
            glfwSetInputMode( id, GLFW_CURSOR, GLFW_CURSOR_NORMAL );
            guiMode = true;
            firstFrame = true;
        } else {
            glfwSetInputMode( id, GLFW_CURSOR, GLFW_CURSOR_DISABLED );
            glfwSetInputMode( id, GLFW_RAW_MOUSE_MOTION, GLFW_TRUE );
            guiMode = false;
        }
    }

    private void toggleBorderlessFullscreen() {

    }

    private void toggleFullscreen() {
        firstFrame = true;
        if ( !isFullscreen() ) {
            glfwSetWindowMonitor( id, monitor, 0, 0, mode.width(), mode.height(), mode.refreshRate() );
            glfwShowWindow( id );
            glfwFocusWindow( id );
        } else {
            glfwSetWindowMonitor( id, NULL, x, y, windowedWidth, windowedHeight, mode.refreshRate() );
        }
        glfwFocusWindow( id );
    }

    private boolean isFullscreen() {
        if ( glfwGetWindowMonitor( id ) != 0 ) return true;
        if ( mode != null ) {
            boolean isSizeMatches = ( width == mode.width() ) && ( height == mode.height() );
            boolean isBorderless = glfwGetWindowAttrib( id, GLFW_DECORATED ) == GLFW_FALSE;
            if ( isSizeMatches && isBorderless ) return true;
        }


        return false;
    }

    public void toggleVsync() {
        vSync = (vSync == 1) ? 0 : 1;
        glfwSwapInterval( vSync );
    }

    public void toggleAlwaysOnTop() {
        int currentState = glfwGetWindowAttrib( id, GLFW_FLOATING );
        glfwSetWindowAttrib( id, GLFW_FLOATING, currentState ^ GLFW_TRUE );

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
