import java.util.ArrayList;
import java.util.List;

public class Scene {

    List<Mesh> meshes;

    public Scene() {
        meshes = new ArrayList<>();
    }

    public void add( Mesh mesh ) {
        meshes.add( mesh );
    }

    public void draw() {
        for ( Mesh mesh : meshes ) {
            mesh.draw();
        }
    }
}
