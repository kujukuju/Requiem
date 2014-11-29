package com.trentwdavies.fontloader;

import com.requiem.utilities.GraphicsUtils;
import com.trentwdavies.textureloader.Texture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Trent on 11/25/2014.
 */
public class StaticFont {
    private boolean antiAlised;
    private Font awtFont;
    private FontMetrics awtFontMetrics;
    private Texture charactersTexture;
    private int charWidth;
    private int imageWidth;
    private int imageHeight;

    private int columnCount;

    private static int[] characterIndexList;
    private static char[] characterList = {' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'};

    public StaticFont(Font awtFont) {
        this(awtFont, false);
    }

    public StaticFont(Font awtFont, boolean antiAliased) {
        this.antiAlised = antiAliased;
        this.awtFont = awtFont;

        Image tempBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        awtFontMetrics = tempBufferedImage.getGraphics().getFontMetrics(awtFont);

        generateCharacterWidth();
        generateCharacterTexture();
    }

    public void drawString(String string, int x, int y) {
        for (int i = 0; i < string.length(); i++) {
            char curChar = string.charAt(i);
            //ByteBuffer curByteBuffer = getByteBuffer(curChar);
        }
    }

    public int getStaticFontHeight() {
        return awtFontMetrics.getMaxAscent() + awtFontMetrics.getMaxDescent();
    }

    public void generateCharacterWidth() {
        int[] charWidths = awtFontMetrics.getWidths();
        charWidth = 0;
        for (int i : charWidths) {
            charWidth = Math.max(charWidth, i);
        }
    }

    public int getMaxAscent() {
        return awtFontMetrics.getMaxAscent();
    }

    public int getMaxDescent() {
        return awtFontMetrics.getMaxDescent();
    }

    private double[] getTextureBoundary(char curChar) {
        int[] rowColumn = getRowColumn(curChar);

        double lowerX = rowColumn[0] * charWidth;
        double lowerY = imageHeight - rowColumn[1] * getStaticFontHeight();
        double upperX = (rowColumn[0] + 1) * charWidth;
        double upperY = imageHeight - (rowColumn[1] + 1) * getStaticFontHeight();

        return new double[]{lowerX / imageWidth, lowerY / imageHeight, upperX / imageWidth, upperY / imageHeight};
    }

    private int[] getRowColumn(char curChar) {
        int index = characterIndexList[curChar];
        int column = index % columnCount;
        int row = index / columnCount;

        return new int[]{row, column};
    }

    private void generateCharacterTexture() {
        int height = getStaticFontHeight();
        imageWidth = GraphicsUtils.nextPowerOfTwo(charWidth * 8);
        columnCount = imageWidth / charWidth;
        imageHeight = GraphicsUtils.nextPowerOfTwo(characterList.length / columnCount * height);

        System.out.println(imageWidth + ", " + imageHeight);
        BufferedImage characterImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) characterImage.getGraphics();
        g.setFont(awtFont);
        if (antiAlised) {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        for (int i = 0; i < characterList.length; i++) {
            char curChar = characterList[i];
            int[] rowColumn = getRowColumn(curChar);
            int drawX = rowColumn[1] * charWidth;
            int drawY = rowColumn[0] * height + height - getMaxDescent();
            g.drawString("" + curChar, drawX, drawY);
        }

        charactersTexture = new Texture(characterImage, true);
    }

    static {
        generateCharacterIndexList();
    }

    private static void generateCharacterIndexList() {
        try {
            int maxChar = -1;
            for (int i = 0; i < characterList.length; i++) {
                maxChar = Math.max(maxChar, characterList[i]);
            }
            characterIndexList = new int[maxChar + 1];

            for (int i = 0; i < characterIndexList.length; i++) {
                characterIndexList[i] = -1;
            }
            for (int i = 0; i < characterList.length; i++) {
                char curChar = characterList[i];
                characterIndexList[curChar] = i;
            }
        } catch (Exception e) {
            System.err.println("Error in the StaticFont static init call");
            e.printStackTrace();
        }
    }
}
