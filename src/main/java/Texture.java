import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL33.*;

public class Texture {

    public int id;
    public int width, height, channels;

    public Texture( String name ) {
        stbi_set_flip_vertically_on_load( true );
        String path = "src/main/resources/textures/";
        path += name + ".jpg";
        try ( MemoryStack stack = MemoryStack.stackPush() ) {
            IntBuffer bWidth = stack.mallocInt( 1 );
            IntBuffer bHeight = stack.mallocInt( 1 );
            IntBuffer bChannels = stack.mallocInt( 1 );

            ByteBuffer image = stbi_load( path, bWidth, bHeight, bChannels, 0 );
            width = bWidth.get( 0 );
            height = bHeight.get( 0 );
            channels = bChannels.get( 0 );
            if ( image != null ) {
                genTexture( image );
                stbi_image_free( image );
            } else {
                System.err.println( "Couldn't load image file " + name );
            }
        }
    }

    private void genTexture( ByteBuffer image ) {
        id = glGenTextures();
        glBindTexture( GL_TEXTURE_2D, id );

        glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST );

        glTexImage2D( GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image );
        glGenerateMipmap( GL_TEXTURE_2D );
    }
}
