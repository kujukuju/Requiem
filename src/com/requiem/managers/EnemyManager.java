package com.requiem.managers;

import com.requiem.abstractentities.entities.enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trent on 2/2/2015.
 */
public class EnemyManager {
    public static List<Enemy> enemyList = new ArrayList<Enemy>();

    public static void update() {
        for (Enemy enemy : enemyList) {
            enemy.update();
        }
    }

    public static void renderEnemies() {
        for (Enemy enemy : enemyList) {
            enemy.render();
        }
    }
}
