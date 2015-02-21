package com.requiem.managers;

import com.trentwdavies.shaderloader.Shader;

import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * Created by Trent on 2/16/2015.
 */
public class ShaderManager {
    public static Shader perFragLightingFlatShader;
    public static final String HELLO_WORLD_SHADER_VERT_PATH = "assets/shaders/per-frag-lighting-flat.vert";
    public static final String HELLO_WORLD_SHADER_FRAG_PATH = "assets/shaders/per-frag-lighting-flat.frag";

    public static void loadShaders() {
        perFragLightingFlatShader = new Shader(HELLO_WORLD_SHADER_VERT_PATH, HELLO_WORLD_SHADER_FRAG_PATH);
    }

    public static void useNoShader() {
        glUseProgram(0);
    }
}
