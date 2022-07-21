package world.ntdi.americaattack.enemy;

import processing.core.PApplet;
import processing.core.PImage;
import world.ntdi.americaattack.AmericaAttack;

public abstract class Enemy {
    public float x, y, vx, vy, enemySpeed, maxEnemySpeed, sizeX, sizeY;
    public AmericaAttack americaAttack;
    public String filePath;
    public PImage[] enemyImages = new PImage[6];
    public int animationFrame = 1;
    public int explosionFrame = 1;
    public boolean isDead = false;
    public boolean doneBeingDead = false;

    public Enemy(float x, float y, float vx, float vy, float enemySpeed, float maxEnemySpeed, int sizeX, int sizeY, AmericaAttack americaAttack, String filePath) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.enemySpeed = enemySpeed;
        this.maxEnemySpeed = maxEnemySpeed;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.filePath = filePath;
        this.americaAttack = americaAttack;

        for (int i = 1; i <= 6; i++) {
            enemyImages[i-1] = americaAttack.loadImage(filePath + i + ".png");
            enemyImages[i-1].resize(sizeX, sizeY);
        }
    }

    public void drawEnemy() {
        if (!isDead) {
            if (americaAttack.frameCount % 5 == 0) {
                animationFrame++;
                animationFrame = animationFrame % 6;
            }
            americaAttack.imageMode(americaAttack.CENTER);
            americaAttack.image(enemyImages[animationFrame], x, y);
        } else {
            if (americaAttack.frameCount % 5 == 0) {
                explosionFrame++;
                if (explosionFrame == 5) {
                    doneBeingDead = true;
                }
            }
            if (!doneBeingDead) {
                americaAttack.imageMode(americaAttack.CENTER);
                americaAttack.image(americaAttack.explosionAnim[explosionFrame], x, y);
            }

        }

    }

    public void moveEnemy(float playerX, float playerY) {
        if (isDead) {
            return;
        }
        float angle = PApplet.atan2(playerY - y, playerX - x);
        vx = PApplet.cos(angle);
        vy = PApplet.sin(angle);
        x += vx * enemySpeed;
        y += vy * enemySpeed;
    };
}
