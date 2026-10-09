import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public class Cube {

    public float x, y, z;
    public float rx, ry, rz;
    public float sx, sy, sz;
    private float size;
    public Shader shaderProgram;
    private Scene scene;
    private Transformation transformation;

    public Texture texture;

    public FloatBuffer verticesBuffer;
    public int VAO;
    public int VBO;
    public int EBO;

    public int[] indices = {
            0, 1, 2, 2, 3, 0,
            4, 5, 6, 6, 7, 4,
            8, 9, 10, 10, 11, 8,
            12, 13, 14, 14, 15, 12,
            16, 17, 18, 18, 19, 16,
            20, 21, 22, 22, 23, 20
    };

    public Cube( float x, float y, float z, float size,
                 Texture texture, Shader shaderProgram, Scene scene, Transformation transformation,
                 float rx, float ry, float rz,
                 float sx, float sy, float sz ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.texture = texture;
        this.shaderProgram = shaderProgram;
        this.scene = scene;
        this.transformation = transformation;

        this.rx = rx;
        this.ry = ry;
        this.rz = rz;

        this.sx = sx;
        this.sy = sy;
        this.sz = sz;

        create();
    }

    private void create() {
        float[] vertices = {
                // Передняя грань
                -0.5f,    -0.5f,    0.5f,           0.0f, 0.0f,     0.0f, 0.0f, 1.0f,
                0.5f,     -0.5f,    0.5f,           1.0f, 0.0f,     0.0f, 0.0f, 1.0f,
                0.5f,     0.5f,     0.5f,           1.0f, 1.0f,     0.0f, 0.0f, 1.0f,
                -0.5f,    0.5f,     0.5f,           0.0f, 1.0f,     0.0f, 0.0f, 1.0f,

                // Задняя грань
                0.5f,     -0.5f,    -0.5f,          0.0f, 0.0f,     0.0f, 0.0f, -1.0f,
                -0.5f,    -0.5f,    -0.5f,          1.0f, 0.0f,     0.0f, 0.0f, -1.0f,
                -0.5f,    0.5f,     -0.5f,          1.0f, 1.0f,     0.0f, 0.0f, -1.0f,
                0.5f,     0.5f,     -0.5f,          0.0f, 1.0f,     0.0f, 0.0f, -1.0f,

                // Верхняя грань
                -0.5f,    0.5f,     0.5f,           0.0f, 0.0f,     0.0f, 1.0f, 0.0f,
                0.5f,     0.5f,     0.5f,           1.0f, 0.0f,     0.0f, 1.0f, 0.0f,
                0.5f,     0.5f,     -0.5f,          1.0f, 1.0f,     0.0f, 1.0f, 0.0f,
                -0.5f,    0.5f,     -0.5f,          0.0f, 1.0f,     0.0f, 1.0f, 0.0f,

                // Нижняя грань
                -0.5f,    -0.5f,    -0.5f,          0.0f, 0.0f,     0.0f, -1.0f, 0.0f,
                0.5f,     -0.5f,    -0.5f,          1.0f, 0.0f,     0.0f, -1.0f, 0.0f,
                0.5f,     -0.5f,    0.5f,           1.0f, 1.0f,     0.0f, -1.0f, 0.0f,
                -0.5f,    -0.5f,    0.5f,           0.0f, 1.0f,     0.0f, -1.0f, 0.0f,

                // Левая грань
                -0.5f,    -0.5f,    -0.5f,          0.0f, 0.0f,     -1.0f, 0.0f, 0.0f,
                -0.5f,    -0.5f,    0.5f,           1.0f, 0.0f,     -1.0f, 0.0f, 0.0f,
                -0.5f,    0.5f,     0.5f,           1.0f, 1.0f,     -1.0f, 0.0f, 0.0f,
                -0.5f,    0.5f,     -0.5f,          0.0f, 1.0f,     -1.0f, 0.0f, 0.0f,

                // Правая грань
                0.5f,     -0.5f,    0.5f,           0.0f, 0.0f,     1.0f, 0.0f, 0.0f,
                0.5f,     -0.5f,    -0.5f,          1.0f, 0.0f,     1.0f, 0.0f, 0.0f,
                0.5f,     0.5f,     -0.5f,          1.0f, 1.0f,     1.0f, 0.0f, 0.0f,
                0.5f,     0.5f,     0.5f,           0.0f, 1.0f,     1.0f, 0.0f, 0.0f
        };

        verticesBuffer = MemoryUtil.memCallocFloat(vertices.length);
        verticesBuffer.put(0, vertices);

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

        // (X, Y, Z, U, V, NX, NY, NZ)
        int stride = 8 * Float.BYTES;

        glVertexAttribPointer( 0, 3, GL_FLOAT, false, stride, 0 );
        glEnableVertexAttribArray( 0 );

        glVertexAttribPointer( 2, 2, GL_FLOAT, false, stride, 3 * Float.BYTES );
        glEnableVertexAttribArray( 2 );

        glVertexAttribPointer( 3, 3, GL_FLOAT, false, stride, 5 * Float.BYTES );
        glEnableVertexAttribArray( 3 );

        MemoryUtil.memFree( verticesBuffer );
        scene.add( this );
    }

    Vector3f rotation;
    Vector3f translation;
    double dt;
    public void draw() {
        dt = Engine.dt * 100;
        rx += sx * dt;
        ry += sy * dt;
        rz += sz * dt;
        glActiveTexture( GL_TEXTURE0 );
        glBindTexture( GL_TEXTURE_2D, texture.id );

        translation = new Vector3f( x, y, z );
        rotation = new Vector3f( Math.toRadians( rx ), Math.toRadians( ry ), Math.toRadians( rz ) );
        transformation.updateModel( translation, size, rotation );
        shaderProgram.setUniformMatrix4f( "model", transformation.model );
        glDrawElements( GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0 );
    }
}
