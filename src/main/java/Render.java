import static org.lwjgl.opengl.GL33.*;

public class Render {

    private Scene scene;
    private Transformation transformation;
    private Shader shaderProgram;
    private Window win;

    public Render( Scene scene, Shader shaderProgram, Window win ) {
        this.scene = scene;
        this.shaderProgram = shaderProgram;
        this.win = win;

        init();
    }

    private void init() {
        transformation = new Transformation( 70f, 1, 0.01f, 1000.0f );
    }

    private void setMVPUniform() {
        shaderProgram.setUniformMatrix4f( "mvpMatrix", transformation.getMVPMatrix() );
    }

    public void render() {
        glClearColor( 0.2f, 0f, 0.2f, 1f );
        glClear( GL_COLOR_BUFFER_BIT );

        scene.draw();
        if ( win.isResized() ) {
            transformation.updateAspect( ( float ) win.getWidth() / win.getHeight() );
            win.setResized( false );
        }
        transformation.updateViewMatrix();
        setMVPUniform();
    }
}
