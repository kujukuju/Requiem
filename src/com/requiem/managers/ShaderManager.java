package com.requiem.managers;

import com.trentwdavies.shaderloader.Shader;

import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Created by Trent on 2/16/2015.
 */
public class ShaderManager {
    public static Shader helloWorldShader;
    public static final String HELLO_WORLD_SHADER_VERT_PATH = "assets/shaders/hello-world.vert";
    public static final String HELLO_WORLD_SHADER_FRAG_PATH = "assets/shaders/hello-world.frag";

    public static void loadShaders() {
        helloWorldShader = new Shader(HELLO_WORLD_SHADER_VERT_PATH, HELLO_WORLD_SHADER_FRAG_PATH);
    }

    public static void useNoShader() {
        glUseProgram(0);
    }
}
