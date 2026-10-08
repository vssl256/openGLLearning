import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

    public int id;

    private final File vsFile;
    private final File fsFile;
    private long vsLastModified;
    private long fsLastModified;

    public Map<String, Integer> uniforms;

    public Shader( String name ) {
        String path = "src/main/resources/shaders/";
        String filePath = path + name + "/" + name;
        vsFile = new File( filePath + ".vert" );
        fsFile = new File( filePath + ".frag" );
        vsLastModified = vsFile.lastModified();
        fsLastModified = fsFile.lastModified();

        uniforms = new HashMap<>();

        compile();
    }

    public void setUniform1i( String name, int integer ) {
        Integer uLocation = uniforms.get( name );
        if ( uLocation == null ) {
            System.err.println( "Error during 1int uniform setting " + name );
            return;
        }
        glUniform1i( uLocation, integer );
    }

    public void setUniform1f( String name, float value ) {
        Integer uLocation = uniforms.get( name );
        if ( uLocation == null ) {
            System.err.println( "Error during 1int uniform setting " + name );
            return;
        }
        glUniform1f( uLocation, value );
    }

    public void setUniform3f( String name, Vector3f vec3 ) {
        Integer uLocation = uniforms.get( name );
        if ( uLocation == null ) {
            System.err.println( "Error during vec3 uniform setting " + name );
            return;
        }
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer buffer = stack.mallocFloat( 3 );
            glUniform3fv( uLocation, vec3.get( buffer ) );
        }
    }

    public void setUniformMatrix4f( String name, Matrix4f mat4 ) {
        Integer uLocation = uniforms.get( name );
        if ( uLocation == null ) {
            System.err.println( "Error during mat4 uniform setting " + name );
            return;
        }
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            FloatBuffer buffer = stack.mallocFloat( 16 );
            glUniformMatrix4fv( uLocation, false , mat4.get( buffer ) );
        }
    }

    public void createUniform( String name ) {
        int uLocation = glGetUniformLocation( id, name );
        if ( uLocation == -1 ) {
            System.err.println( "Error during uniform creation " + name );
            return;
        }
        uniforms.put( name, uLocation );
    }

    public void needToRecompile() {
        if ( vsLastModified != vsFile.lastModified() ||
                fsLastModified != fsFile.lastModified() ) {
            vsLastModified = vsFile.lastModified();
            fsLastModified = fsFile.lastModified();
            System.out.println( "Recompiling shaders..." );
            recompile();
        }
    }

    public void recompile() {
        glDeleteProgram( id );
        compile();
        glUniform1i( glGetUniformLocation( id, "texture1" ), 0 );
        glUniform1i( glGetUniformLocation( id, "texture2" ), 1 );
    }

    public void compile() {
        id = glCreateProgram();

        String vss = Utils.readFile( vsFile );
        String fss = Utils.readFile( fsFile );

        int vertexShader = glCreateShader( GL_VERTEX_SHADER );
        glShaderSource( vertexShader, vss );
        glCompileShader( vertexShader );
        if ( glGetShaderi( vertexShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( vertexShader ) );
        }

        int fragmentShader = glCreateShader( GL_FRAGMENT_SHADER );
        glShaderSource( fragmentShader, fss );
        glCompileShader( fragmentShader );
        if ( glGetShaderi( fragmentShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( fragmentShader ) );
        }

        glAttachShader( id, vertexShader );
        glAttachShader( id, fragmentShader );
        glLinkProgram( id );
        glDeleteShader( vertexShader );
        glDeleteShader( fragmentShader );
        if ( glGetProgrami( id, GL_LINK_STATUS ) == 0 ) {
            System.err.println( glGetProgramInfoLog( id ) );
        }
        glUseProgram( id );

        createUniform( "model" );
        createUniform( "view" );
        createUniform( "projection" );
        createUniform( "lightPos" );
        createUniform( "viewPos" );
    }
}
