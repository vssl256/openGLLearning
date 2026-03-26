import org.joml.Math;
import org.lwjgl.system.MemoryStack;

import org.joml.*;

import java.lang.Runtime;

import static org.lwjgl.glfw.GLFW.*;

void main() {
    Runtime runtime = Runtime.getRuntime();
// Используемая память = Всего выделено - Свободно в выделенном
    int penis = 0;
    while ( true ) {
        Matrix4f ass = new Matrix4f()
                .rotateX( 90f )
                .rotateY( 90f );
    long usedMemory = runtime.totalMemory() - runtime.freeMemory();
    System.out.println("Используемая память: " + usedMemory / (1024 * 1024) + " МБ");
    }
}


