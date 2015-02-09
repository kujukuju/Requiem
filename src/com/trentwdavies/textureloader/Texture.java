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

import com.requiem.interfaces.Asset;
import org.lwjgl.opengl.GL30;

public class Texture implements Asset {
	private int mId;
	private ByteBuffer mImageData;
	private int mWidth;
	private int mHeight;
	
	public Texture(InputStream in, boolean mipmap) throws IOException {
		this(ImageIO.read(in), mipmap);
	}

	public Texture(BufferedImage image, boolean mipmap) {
		this(((DataBufferByte) image.getRaster().getDataBuffer()).getData(), image.getWidth(), image.getHeight(), mipmap);
	}

	public Texture(byte[] image, int width, int height, boolean mipmap) {
		for (int i = 0; i < image.length; i += 4) {
			byte alpha = image[i];
			byte red = image[i + 1];
			byte green = image[i + 2];
			byte blue = image[i + 3];
			image[i] = red;
			image[i + 1] = green;
			image[i + 2] = blue;
			image[i + 3] = alpha;
		}
		ByteBuffer pb = ByteBuffer.allocateDirect(image.length).order(ByteOrder.nativeOrder());
		pb.put(image).flip();
		setupTexture(pb, width, height, mipmap);
	}

	public Texture(ByteBuffer image, int width, int height, boolean mipmap) {
		setupTexture(image, width, height, mipmap);
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, mId);
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	private void setupTexture(ByteBuffer image, int width, int height, boolean mipmap) {
		mId = glGenTextures();
		mImageData = image;
		mWidth = width;
		mHeight = height;

		glBindTexture(GL_TEXTURE_2D, mId);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);

		glTexImage2D(GL_TEXTURE_2D, 0, 4, mWidth, mHeight, 0, GL_BGRA, GL_UNSIGNED_BYTE, mImageData);

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
