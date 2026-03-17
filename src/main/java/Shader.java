import static org.lwjgl.opengl.GL33.*;

public class Shader {

    public int shaderProgram;
    private String path = "src/main/resources/shaders/";

    public Shader( String name ) {
        String filePath = path + name + "/" + name;
        String vertexShaderSource = Utils.readFile( filePath + ".vert" );
        String fragmentShaderSource = Utils.readFile( filePath + ".frag" );

        compile( vertexShaderSource, fragmentShaderSource );
    }

    public void compile( String vertexShaderSource, String fragmentShaderSource ) {
        int vertexShader = glCreateShader( GL_VERTEX_SHADER );
        glShaderSource( vertexShader, vertexShaderSource );
        glCompileShader( vertexShader );
        if ( glGetShaderi( vertexShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( vertexShader ) );
        }

        int fragmentShader = glCreateShader( GL_FRAGMENT_SHADER );
        glShaderSource( fragmentShader, fragmentShaderSource );
        glCompileShader( fragmentShader );
        if ( glGetShaderi( fragmentShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( fragmentShader ) );
        }

        shaderProgram = glCreateProgram();
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
