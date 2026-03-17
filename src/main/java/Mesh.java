import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL33.*;

public class Mesh {

    private Shader shader;
    private Texture texture;

    public float[] vertices;
    public FloatBuffer verticesBuffer;
    public int VAO;
    public int VBO;
    public int EBO;

    public int drawMode;

    public int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public Mesh( float[] vertices, String name, int drawMode ) {
        this.vertices = vertices;
        this.drawMode = drawMode;
        shader = new Shader( name );
        texture = new Texture( name );
        verticesBuffer = MemoryUtil.memCallocFloat( vertices.length );
        verticesBuffer.put( 0, vertices );
        init();
    }

    public void init() {
        VAO = glGenVertexArrays();
        glBindVertexArray( VAO );

        VBO = glGenBuffers();
        glBindBuffer( GL_ARRAY_BUFFER, VBO );
        glBufferData( GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW );

        EBO = glGenBuffers();
        glBindBuffer( GL_ELEMENT_ARRAY_BUFFER, EBO );
        glBufferData( GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW );

        glVertexAttribPointer( 0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0 );
        glEnableVertexAttribArray( 0 );

        glVertexAttribPointer( 1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES );
        glEnableVertexAttribArray( 1 );

        glVertexAttribPointer( 2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES );
        glEnableVertexAttribArray( 2 );

        MemoryUtil.memFree( verticesBuffer );
    }

    public void draw() {
        glPolygonMode( GL_FRONT_AND_BACK, drawMode );

        glUseProgram( shader.shaderProgram );

        int vertexColorLocation = glGetUniformLocation( shader.shaderProgram, "u_Color" );
        float time = ( float )glfwGetTime();
        float colorValue = ( float )( ( Math.sin( time ) / 2.0f ) + 0.5f );
        glUniform3f( vertexColorLocation, colorValue, colorValue, colorValue );

        //glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, texture.id );

        glBindVertexArray( VAO );
        glDrawElements( GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0 );
    }
}
