import org.lwjgl.system.MemoryStack;

import static org.lwjgl.stb.STBImage.*;

void main() {
    int width;
    int height;
    int channels;
    ByteBuffer image;
    try ( MemoryStack stack = MemoryStack.stackPush() ) {
        IntBuffer pWidth = stack.mallocInt( 1 );
        IntBuffer pHeight = stack.mallocInt( 1 );
        IntBuffer pChannels = stack.mallocInt( 1 );
        image = stbi_load( "test.jpg", pWidth, pHeight, pChannels, 0 );
        if ( image == null ) {
            throw new RuntimeException( "Failed to load texture: " + stbi_failure_reason() );
        }
        width = pWidth.get( 0 );
        height = pHeight.get( 0 );
        channels = pChannels.get( 0 );
    }
    System.out.println( channels );
    stbi_image_free( image );

}
