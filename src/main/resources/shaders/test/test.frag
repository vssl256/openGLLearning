#version 330 core

out vec4 FragColor;

in vec3 ourColor;
in vec2 TexCoord;

uniform vec3 u_Color;
uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;

void main()
{
    vec4 c0 = texture( texture0, TexCoord );
    vec4 c1 = texture( texture1, TexCoord );
    vec4 c2 = texture( texture2, TexCoord );
    FragColor = c2;
}