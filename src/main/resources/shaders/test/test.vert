#version 330 core

layout ( location = 0 ) in vec3 aPos;
layout ( location = 1 ) in vec3 aColor;
layout ( location = 2 ) in vec2 aTexCoord;
layout ( location = 3 ) in vec3 aNormal;

out vec3 ourColor;
out vec3 colr;
out vec2 TexCoord;
out vec3 normal;
out vec3 fragPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    float noise = fract(sin(dot(TexCoord, vec2(12.9898, 78.233))) * 43758.5453);
    fragPos = vec3( model * vec4( aPos, 1.0f ) );
    gl_Position = projection * view * model * vec4(aPos, 1.0f);

    ourColor = aPos;
    colr = aColor;
    TexCoord = aTexCoord;
    normal = mat3( transpose( inverse( model ) ) ) * aNormal;
}