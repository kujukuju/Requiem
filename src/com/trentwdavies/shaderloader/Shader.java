package com.trentwdavies.shaderloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;

/**
 * Created by Trent on 2/16/2015.
 */
public class Shader {
    public int shaderProgram;

    public Shader(String vertexPath, String fragmentPath) {
        try {
            setUpShader(vertexPath, fragmentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpShader(String vertexPath, String fragmentPath) throws IOException {
        shaderProgram = glCreateProgram();
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        StringBuilder vertexShaderSource = new StringBuilder();
        StringBuilder fragmentShaderSource = new StringBuilder();
        BufferedReader vertexReader = new BufferedReader(new FileReader(vertexPath));
        BufferedReader fragmentReader = new BufferedReader(new FileReader(fragmentPath));

        String vertexLine = null;
        while ((vertexLine = vertexReader.readLine()) != null) {
            vertexShaderSource.append(vertexLine).append('\n');
        }
        vertexReader.close();

        String fragmentLine = null;
        while ((fragmentLine = fragmentReader.readLine()) != null) {
            fragmentShaderSource.append(fragmentLine).append('\n');
        }
        fragmentReader.close();

        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Vertex shader was not compiled.");
            System.err.println(glGetShaderInfoLog(vertexShader, 1024));
            throw new RuntimeException();
        }
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader was not compiled.");
            System.err.println(glGetShaderInfoLog(fragmentShader, 1024));
            throw new RuntimeException();
        }

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Shader program was not linked.");
            System.err.println(glGetProgramInfoLog(shaderProgram, 1024));
            throw new RuntimeException();
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(shaderProgram);
    }
}
