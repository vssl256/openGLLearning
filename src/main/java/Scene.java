import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Scene {

    private final Shader shaderProgram;
    List<Mesh> meshes;
    List<Cube> cubes;

    public Scene( Transformation transformation, Shader shaderProgram ) {
        this.shaderProgram = shaderProgram;
        meshes = new ArrayList<>();
        cubes = new ArrayList<>();
    }

    public void add( Mesh mesh ) {
        meshes.add( mesh );
    }
    public void add( Cube cube ) {
        cubes.add( cube );
    }

    float frame = 0.1f;
    public void draw() {
        shaderProgram.needToRecompile();

        glUseProgram( shaderProgram.id );
        glBindVertexArray( cubes.getFirst().VAO );

        //for ( Mesh mesh : meshes ) {
        //    mesh.draw();
        //}
        //lTest();
        for ( Cube cube : cubes ) {
            cube.draw();
        }
        glBindVertexArray( 0 );
        //glUseProgram( 0 );
    }

    Vector3f lPos = new Vector3f(0);
    float x = 0;
    private void lTest() {
        lPos.set( 0, Math.sin( x ) * Math.sin( x ) * 6, 0 );
        shaderProgram.setUniform3f( "lightPos", lPos );
        x+=0.01f;
    }
}
