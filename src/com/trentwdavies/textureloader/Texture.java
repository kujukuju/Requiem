package com.trentwdavies.textureloader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_8_8_8_8_REV;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL30;

public class Texture {
	private int mId;
	private ByteBuffer mImageData;
	private int mSize;
	
	public Texture(InputStream in, boolean mipmap) throws IOException {
		this(ImageIO.read(in), mipmap);
	}

	public Texture(BufferedImage image, boolean mipmap) {
		this(((DataBufferByte) image.getRaster().getDataBuffer()).getData(), image.getWidth(), mipmap);
	}

	public Texture(byte[] image, int size, boolean mipmap) {
		ByteBuffer pb = ByteBuffer.allocateDirect(image.length).order(ByteOrder.nativeOrder());
		pb.put(image).flip();
		setupTexture(pb, size, mipmap);
	}

	public Texture(ByteBuffer image, int size, boolean mipmap) {
		setupTexture(image, size, mipmap);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, mId);
	}

	private void setupTexture(ByteBuffer image, int size, boolean mipmap) {
		mId = glGenTextures();
		mImageData = image;
		mSize = size;

		glBindTexture(GL_TEXTURE_2D, mId);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);

		glTexImage2D(GL_TEXTURE_2D, 0, 4, mSize, mSize, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, mImageData);

		if(mipmap) {
			GL30.glGenerateMipmap(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		} else {
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		}
	}
}
