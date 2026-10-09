import org.joml.Math;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static org.lwjgl.glfw.GLFW.*;

public class Character {
    private static final Vector3fc UP = new Vector3f( 0, 1, 0 );
    private static final float G = 10f;

    public Transformation camera;
    public Window win;

    public Vector3f position;
    public Vector3f velocity;
    public Vector3f newPosition;

    public float height = 1.7f;

    public float speed = 15f;
    public float accelStrength = 10f;
    public float jumpHeight = 180f;

    public Vector3f accel = new Vector3f();
    public Vector3f targetVelocity = new Vector3f();

    public boolean onFloor = false;

    /* TEMP */ public float floorHeight = 0f;

    public double mouseSensitivity = 0.25;

    public Character( Transformation camera, Window win ) {
        position = new Vector3f();
        velocity = new Vector3f();
        newPosition = new Vector3f();

        this.camera = camera;
        this.win = win;
    }

    public void physicsStep( double delta ) {
        handleCamera();
        handleMovement();
        accel.set(
                targetVelocity.x - velocity.x,
                targetVelocity.y - velocity.y,
                targetVelocity.z - velocity.z
        );
        velocity.fma( accelStrength * ( float ) delta, accel );
        position.fma( ( float ) delta, velocity );

        camera.position = position;
    }

    private Vector3f moveDelta = new Vector3f();
    public void handleMovement() {
        if ( !isOnFloor() ) {
            handleGravity();
            return;
        }
        targetVelocity.zero();
        moveDelta.zero();
        camera.rotation.identity()
                .rotateY( Math.toRadians( camera.yaw ) );

        camera.rotation.positiveZ( camera.dirZ );
        camera.rotation.positiveX( camera.dirX );

        if ( win.keys[ GLFW_KEY_SPACE ] ) {
            handleJump();
            return;
        }
        if ( win.keys[ GLFW_KEY_W ] ) {
            moveDelta.sub( camera.dirZ );
        }
        if ( win.keys[ GLFW_KEY_S ] ) {
            moveDelta.add( camera.dirZ );
        }
        if ( win.keys[ GLFW_KEY_A ] ) {
            moveDelta.sub( camera.dirX );
        }
        if ( win.keys[ GLFW_KEY_D ] ) {
            moveDelta.add( camera.dirX );
        }

        if ( moveDelta.lengthSquared() > 0.0f ) moveDelta.normalize();
        targetVelocity.add( moveDelta.mul( speed ) );
    }

    private void handleJump() {
        targetVelocity.add( 0, jumpHeight, 0 );
    }

    private float threshold = 0.05f;
    private void handleGravity() {
        if ( Math.abs( position.y - floorHeight ) <= threshold ) {
            position.y = floorHeight;
            targetVelocity.y = 0;
            velocity.y = 0;
            accel.y = 0;
            return;
        }
        targetVelocity.sub( 0, G, 0 );
    }

    private void handleCamera() {
        camera.yaw += ( float ) ( win.mouseOffset.x * mouseSensitivity );
        camera.pitch += ( float ) ( win.mouseOffset.y * mouseSensitivity );
        camera.pitch = Math.clamp( -89f, 89f, camera.pitch );
        win.mouseOffset.x = 0;
        win.mouseOffset.y = 0;
    }

    public boolean isOnFloor() {
        return position.y == floorHeight;
    }
}
