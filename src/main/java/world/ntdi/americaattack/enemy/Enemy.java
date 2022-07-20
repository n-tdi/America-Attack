package world.ntdi.americaattack.enemy;

import processing.core.PImage;
import world.ntdi.americaattack.AmericaAttack;

public abstract class Enemy {
    public float x, y, vx, vy, enemySpeed, maxEnemySpeed, sizeX, sizeY;
    public String filePath;
    public PImage[] enemyImages = new PImage[6];

    public Enemy(float x, float y, float vx, float vy, float enemySpeed, float maxEnemySpeed, float sizeX, float sizeY, AmericaAttack americaAttack, String filePath) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.enemySpeed = enemySpeed;
        this.maxEnemySpeed = maxEnemySpeed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

        for (int i = 1; i <= 6; i++) {
            enemyImages[i-1] = americaAttack.loadImage(filePath + i + ".png");
            enemyImages[i-1].resize(120, 0);
        }
    }

    public abstract void drawEnemy();

    public abstract void moveEnemy(float playerX, float playerY);
}
