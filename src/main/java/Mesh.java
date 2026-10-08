import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL33.*;

public class Mesh {

    private Shader shaderProgram;
    private List<Texture> textures;

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

    public Mesh( float[] vertices, String name, int drawMode, Shader shaderProgram ) {
        this.vertices = vertices;
        this.drawMode = drawMode;
        this.shaderProgram = shaderProgram;
        initTextures( name );

        verticesBuffer = MemoryUtil.memCallocFloat( vertices.length );
        verticesBuffer.put( 0, vertices );
        init();
    }

    private void initTextures( String name ) {
        textures = new ArrayList<>();
        textures.add( new Texture( name ) );
        textures.add( new Texture( name ) );
        textures.add( new Texture( name ) );
    }

    private void setUniforms() {
        for ( int i = 0; i < textures.size(); i++ ) {
            shaderProgram.createUniform( "texture" + i );
            shaderProgram.setUniform1i( "texture" + i, i );
        }
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
        int[] values = new int[2];
        glGetIntegerv( GL_POLYGON_MODE, values );
        int currentMode = values[0];

        glPolygonMode( GL_FRONT_AND_BACK, drawMode );

        //shaderProgram.needToRecompile();

        //glUseProgram( shaderProgram.id );

        //shaderProgram.createUniform( "u_Color" );
        //shaderProgram.setUniform3f( "u_Color", new Vector3f( 2.0f, 2.0f, 2.0f ) );
        //setUniforms();
        //int vertexColorLocation = glGetUniformLocation( shaderProgram.id, "u_Color" );
        //float time = ( float )glfwGetTime();
        //float colorValue = ( Math.sin( time ) / 2.0f ) + 0.5f;
        //wglUniform3f( vertexColorLocation, colorValue, colorValue, colorValue );

        //for ( int i = 0; i < textures.size(); i++ ) {
        //    glActiveTexture( GL_TEXTURE0 + i );
        //    glBindTexture( GL_TEXTURE_2D, textures.get( i ).id );
        //}

        glBindVertexArray( VAO );
        glDrawElements( GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0 );

        glPolygonMode( GL_FRONT_AND_BACK, currentMode );
    }
}
