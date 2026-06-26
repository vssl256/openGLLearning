import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Transformation {

    private static float movementSpeed = Engine.MOVEMENT_SPEED;
    public static void updateSpeed( float newSpeed ) {
        movementSpeed = newSpeed;
    }

    public Vector3f position;
    public float yaw, pitch;
    public static float fov;
    private float zNear, zFar;

    public Matrix4f model;
    public Matrix4f projection;
    public Matrix4f view;
    private final Matrix4f modelViewMatrix;

    public Matrix4f rotation;
    public Vector3f directionVector;
    public Vector3f moveDelta;

    private float ground = -1.7f;
    private float height = 1.7f;

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
        moveDelta = new Vector3f();
    }

    public Vector3f dirX = new Vector3f();
    public Vector3f dirZ = new Vector3f();

    public float moveToward( Window win, double dt ) {
        moveDelta.zero();
        rotation.identity()
                .rotateY( Math.toRadians( yaw ) );

        rotation.positiveZ( dirZ );
        rotation.positiveX( dirX );

        if ( win.keys[ GLFW_KEY_W ] ) {
            moveDelta.sub( dirZ );
        }
        if ( win.keys[ GLFW_KEY_S ] ) {
            moveDelta.add( dirZ );
        }
        if ( win.keys[ GLFW_KEY_D ] ) {
            moveDelta.add( dirX );
        }
        if ( win.keys[ GLFW_KEY_A ] ) {
            moveDelta.sub( dirX );
        }

        if ( moveDelta.lengthSquared() > 0 ) moveDelta.normalize();
        position.add( moveDelta.mul( movementSpeed * ( float ) dt ) );
        return moveDelta.lengthSquared();
    }

    private float speed;
    public void flight( Window win, double dt ) {
        inJump = false;
        fallSpeed = 0;
        speed = movementSpeed * ( float ) dt;
        if ( win.keys[ GLFW_KEY_SPACE ] ) position.set( position.x, position.y + speed, position.z );
        if ( win.keys[ GLFW_KEY_LEFT_SHIFT ] ) position.set( position.x, position.y - speed, position.z );
    }

    private float fallSpeed = 0;
    private float g = 0.981f/2f;
    public void gravityTest( Window win, double dt ) {
        if ( inJump ) {
            jumping( dt );
            return;
        }
        if ( position.y <= ground + height ) {
            position.y = height + ground;
            fallSpeed = 0;
            if ( win.keys[ GLFW_KEY_SPACE ] ) {
                inJump = true;
            }
        } else if ( position.y >= ground + height ) {
            fallSpeed += g * g * ( float ) dt;
            position.y -= fallSpeed * ( float ) dt * 100;
        }
    }

    private boolean inJump = false;
    public static float jumpHeight = 2f;
    private float fadeOut;
    private void jumping( double dt ) {
        fadeOut = ( float ) dt * ( 16f * g - ( float ) dt );
        position.lerp( new Vector3f( position.x, jumpHeight, position.z ), fadeOut );
        if ( Math.abs( position.y - jumpHeight ) < jumpHeight / 30f ) {
            inJump = false;
        }
    }

    public Matrix4f getModel() {
        return model;
    }

    public Matrix4f getView() {
        return view;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public void updateModel( Vector3f translation, float scale, Vector3f rotation ) {
        model.identity()
                .translate( translation )
                .scale( scale )
                .rotateXYZ( rotation );
    }

    public void updateAspect( float aspect ) {
        projection.setPerspective( Math.toRadians( fov ), aspect, zNear, zFar );
    }

    public void updateViewMatrix( float aspect ) {
        projection.setPerspective( Math.toRadians( fov ), aspect, zNear, zFar );
        view.identity()
                .rotateX( Math.toRadians( pitch ) )
                .rotateY( Math.toRadians( yaw ) )
                .translate( -position.x, -position.y, -position.z );
    }


}
