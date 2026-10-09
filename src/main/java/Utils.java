import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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
