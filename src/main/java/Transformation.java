import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    private static final float MOVEMENT_SPEED = Engine.MOVEMENT_SPEED;

    public Vector3f position;
    public static float yaw, pitch;
    private float fov;
    private float zNear, zFar;

    public Matrix4f model;
    public Matrix4f projection;
    public Matrix4f view;
    private final Matrix4f modelViewMatrix;

    private Matrix4f rotation;
    private Vector3f directionVector;

    public Transformation( float fov, float aspect, float zNear, float zFar ) {
        this.fov = fov;
        this.zNear = zNear;
        this.zFar = zFar;

        position = new Vector3f( 0, 0, 0 );
        model = new Matrix4f();
        view = new Matrix4f();
        projection = new Matrix4f().setPerspective( Math.toRadians( fov ), aspect, zNear, zFar );
        modelViewMatrix = new Matrix4f();

        rotation = new Matrix4f();
        directionVector = new Vector3f();
    }
    public void moveToward( Directions direction, double dt ) {

        rotation.identity()
                .rotateY( Math.toRadians( yaw ) );

        switch ( direction ) {
            case FORWARD -> rotation.positiveZ( directionVector ).negate();
            case BACKWARD -> rotation.positiveZ( directionVector );
            case RIGHTWARD -> rotation.positiveX( directionVector );
            case LEFTWARD -> rotation.positiveX( directionVector ).negate();
        }

        directionVector.normalize();
        position.add( directionVector.mul( MOVEMENT_SPEED * ( float ) dt ) );
    }

    public Matrix4f getMVPMatrix() {
        return modelViewMatrix.set( projection )
                .mul( view )
                .mul( model );
    }

    public void updateAspect( float aspect ) {
        projection.setPerspective( Math.toRadians( fov ), aspect, zNear, zFar );
    }

    public void updateViewMatrix() {
        view.identity()
                .rotateX( Math.toRadians( pitch ) )
                .rotateY( Math.toRadians( yaw ) )
                .translate( -position.x, -position.y, -position.z );
    }

}
