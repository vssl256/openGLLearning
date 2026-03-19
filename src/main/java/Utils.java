import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    public static String readFile( File file ) {

        String out;
        try ( FileReader reader = new FileReader( file ) ) {
            out = reader.readAllAsString();
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        return out;
    }
}
