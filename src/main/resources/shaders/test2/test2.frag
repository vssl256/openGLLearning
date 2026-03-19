#version 330 core

out vec4 fragColor;
in vec3 ourColor;
uniform vec3 u_Color;

void main()
{
    fragColor = vec4(ourColor, 1.0) * vec4( u_Color, 1.0 );
}