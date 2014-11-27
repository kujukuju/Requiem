package com.requiem.managers;

import com.trentwdavies.fontloader.StaticFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Trent on 7/24/2014.
 */
public class FontManager {
    public static StaticFont titleScreenMenuFont;

    //private static final String TITLE_SCREEN_TITLE_FONT_FILE_PATH = "assets/fonts/NeutraDisplay-BoldAlt.ttf";
    private static final String TITLE_SCREEN_MENU_FONT_FILE_PATH = "assets/fonts/NeutraDisplay-MediumAlt.ttf";
    //private static final String ABILITY_BAR_NUMBER_FONT_FILE_PATH = "assets/fonts/NeutraDisplay-BoldAlt.ttf";

    public static void init() {
        try {
            generateFonts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    public static void generateFonts() throws IOException, FontFormatException {
        int[] resolution = SettingsManager.getResolution();
        float scale = resolution[1] * 0.002f;
        System.out.println("Font Scale: " + scale);

        Font titleScreenMenuFontAWT = Font.createFont(Font.TRUETYPE_FONT, new File(TITLE_SCREEN_MENU_FONT_FILE_PATH));
        titleScreenMenuFontAWT = titleScreenMenuFontAWT.deriveFont(Font.PLAIN, 128);
        titleScreenMenuFont = new StaticFont(titleScreenMenuFontAWT, true);
    }

    public static void resize() {
        try {
            generateFonts();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }
}
