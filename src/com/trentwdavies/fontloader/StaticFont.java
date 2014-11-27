package com.trentwdavies.fontloader;

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
    private ByteBuffer[] characterByteBuffers;
    private int[] characterWidths;

    private static int[] characterIndexList = {};
    private static char[] characterList = {' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'};

    public StaticFont(Font awtFont) {
        this(awtFont, false);
    }

    public StaticFont(Font awtFont, boolean antiAliased) {
        this.antiAlised = antiAliased;
        this.awtFont = awtFont;

        Image tempBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        awtFontMetrics = tempBufferedImage.getGraphics().getFontMetrics(awtFont);

        generateCharacterWidths();
        generateCharacterByteBuffers();
    }

    public void drawString(String string, int x, int y) {
        for (int i = 0; i < string.length(); i++) {
            char curChar = string.charAt(i);
            ByteBuffer curByteBuffer = getByteBuffer(curChar);
        }
    }

    public int getStaticFontHeight() {
        return awtFontMetrics.getMaxAscent() + awtFontMetrics.getMaxDescent();
    }

    public int getMaxAscent() {
        return awtFontMetrics.getMaxAscent();
    }

    public int getMaxDescent() {
        return awtFontMetrics.getMaxDescent();
    }

    private ByteBuffer getByteBuffer(char curChar) {
        return characterByteBuffers[characterIndexList[curChar]];
    }

    private void generateCharacterByteBuffers() {
        characterByteBuffers = new ByteBuffer[characterList.length];

        int height = getStaticFontHeight();
        for (int i = 0; i < characterList.length; i++) {
            char curCharacter = characterList[i];
            int width = awtFontMetrics.charWidth(characterList[i]);

            BufferedImage characterImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = (Graphics2D) characterImage.getGraphics();
            if (antiAlised) {
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            g.setFont(awtFont);
            g.drawString("" + curCharacter, 0, characterImage.getHeight() - getMaxDescent());

            DataBufferByte dataBufferByte = (DataBufferByte) characterImage.getRaster().getDataBuffer();
            byte[] pixelData = dataBufferByte.getData();
            characterByteBuffers[i] = ByteBuffer.allocate(pixelData.length);
            characterByteBuffers[i].wrap(pixelData);
            characterByteBuffers[i].flip();
        }
    }

    private void generateCharacterWidths() {
        characterWidths = new int[characterList.length];

        int[] widths = awtFontMetrics.getWidths();
        for (int i = 0; i < characterList.length; i++) {
            characterWidths[i] = widths[characterList[i]];
        }
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
