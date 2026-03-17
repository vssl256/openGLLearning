import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static String readFile( String path ) {

        String out;
        try ( FileReader reader = new FileReader( path ) ) {
            out = reader.readAllAsString();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        return out;
    }
}
