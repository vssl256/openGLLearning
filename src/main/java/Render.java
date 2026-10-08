import static org.lwjgl.opengl.GL33.*;

public class Render {

    private Scene scene;
    private Transformation transformation;
    private Shader shaderProgram;
    private Window win;

    private final float[] color = { 0, 0, 0 };

    public Render( Scene scene, Shader shaderProgram, Window win, Transformation transformation ) {
        this.scene = scene;
        this.shaderProgram = shaderProgram;
        this.win = win;
        this.transformation = transformation;
        glEnable( GL_DEPTH_TEST );

    }

    private void updateMVPMatrix() {
        //shaderProgram.setUniformMatrix4f( "mvpMatrix", transformation.getMVPMatrix() );
        shaderProgram.setUniformMatrix4f( "model", transformation.getModel() );
        shaderProgram.setUniformMatrix4f( "view", transformation.getView() );
        shaderProgram.setUniformMatrix4f( "projection", transformation.getProjection() );
        shaderProgram.setUniform3f( "viewPos", transformation.position );
    }

    public void updateModel() {
        shaderProgram.setUniformMatrix4f( "model", transformation.getModel() );
    }

    public void render() {
        glClearColor( color[0], color[1], color[2], 1f );
        glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

        scene.draw();
        if ( win.isResized() ) {
            transformation.updateAspect( ( float ) win.getWidth() / win.getHeight() );
            win.setResized( false );
        }
        transformation.updateViewMatrix( ( float ) win.getWidth() / win.getHeight() );
        updateMVPMatrix();
    }
}
