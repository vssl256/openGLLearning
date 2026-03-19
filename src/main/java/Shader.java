import java.io.File;

import static org.lwjgl.opengl.GL33.*;

public class Shader {

    public int shaderProgram;

    private final File vsFile;
    private final File fsFile;
    private long vsLastModified;
    private long fsLastModified;

    public Shader( String name ) {
        String path = "src/main/resources/shaders/";
        String filePath = path + name + "/" + name;
        vsFile = new File( filePath + ".vert" );
        fsFile = new File( filePath + ".frag" );
        vsLastModified = vsFile.lastModified();
        fsLastModified = fsFile.lastModified();

        compile();
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
        glDeleteProgram( shaderProgram );
        compile();
        glUniform1i( glGetUniformLocation( shaderProgram, "texture1" ), 0 );
        glUniform1i( glGetUniformLocation( shaderProgram, "texture2" ), 1 );
    }

    public void compile() {
        shaderProgram = glCreateProgram();

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

        glAttachShader( shaderProgram, vertexShader );
        glAttachShader( shaderProgram, fragmentShader );
        glLinkProgram( shaderProgram );
        glDeleteShader( vertexShader );
        glDeleteShader( fragmentShader );
        if ( glGetProgrami( shaderProgram, GL_LINK_STATUS ) == 0 ) {
            System.err.println( glGetProgramInfoLog( shaderProgram ) );
        }
        glUseProgram( shaderProgram );
    }
}
