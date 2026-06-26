#version 330 core

out vec4 FragColor;

in vec3 ourColor;
in vec2 TexCoord;
in vec3 colr;
in vec3 normal;
in vec3 fragPos;

uniform vec3 u_Color;
uniform sampler2D bricks;
float min = 25.0f;
float max = -25.0f;
float ambientLight = 0.0;
uniform vec3 lightPos;
vec3 lightColor = vec3( 1.0f, 1.0f, 1.0f );
float lightingStrength = 2;
vec3 objectColor = vec3( 1.0f, 0.0f, 1.0f );

uniform vec3 viewPos;
float specularStrength = 0.5f;

void main()
{
    float noise = fract(sin(dot(TexCoord, vec2(12.9898, 78.233))) * 43758.5453);
    vec4 c0 = texture( bricks, TexCoord );
    //vec4 c1 = texture( texture1, TexCoord );
    //vec4 c2 = texture( bricks, TexCoord + u_Color.x * 0 );
    vec3 color = fragPos;
    vec2 blocks = floor(TexCoord * 16.0) / 16.0;
    float r = ( color.x * color.x + min ) / ( max - min );
    float g = ( color.y * color.y + min ) / ( max - min );
    float b = ( color.z * color.z + min ) / ( max - min );

    vec3 norm = normalize( normal );
    vec3 lightDir = normalize( lightPos - fragPos );
    float diff = max( dot( norm, lightDir ), 0.0f );
    lightColor = min( vec3( abs(r - r/2), abs(g - g/2), abs(b - b/2) ), 1.2f );
    vec3 diffuse = diff * lightColor;
    vec3 coloredTexture = vec3( c0.r, c0.g, c0.b);

    vec3 viewDir = normalize( viewPos - fragPos );
    vec3 reflectDir = reflect( -lightDir, norm );
    float spec = pow( max( dot( viewDir, reflectDir ), 0.0 ), 32 );
    vec3 specular = specularStrength * spec * lightColor;

    vec3 ligting = ( ambientLight + diffuse + specular ) * lightingStrength;

    vec3 result = ligting * coloredTexture.rgb;
    result = result;

    FragColor = vec4( result, 0.0f );
    //if ( colr.x == 0 && colr.y == 0 && colr.z == 0 ) {
    //    FragColor = vec4( result, c0.a );
    //} else {
    //    FragColor = vec4( colr.x, colr.y, colr.z, 1.0 );
    //}
}