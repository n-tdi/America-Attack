package world.ntdi.americaattack.enemy.enemies;

import processing.core.PApplet;
import world.ntdi.americaattack.AmericaAttack;
import world.ntdi.americaattack.enemy.Enemy;

public class RedEnemy extends Enemy {
    AmericaAttack americaAttack;

    public RedEnemy(AmericaAttack americaAttack, float x, float y) {
        super(x, y, 0, 0, 2F, 3F, 25, 25);
        this.americaAttack = americaAttack;

        for (int i = 1; i <= 6; i++) {
            enemyImages[i-1] = americaAttack.loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/russia_enemy_" + i + ".png");
            enemyImages[i-1].resize(120, 0);
        }
    }

    @Override
    public void drawEnemy() {
        americaAttack.rectMode(americaAttack.CENTER);
        americaAttack.fill(255, 102, 102);
        americaAttack.rect(x, y, sizeX, sizeY);
    }

    @Override
    public void moveEnemy(float playerX, float playerY) {
        float angle = PApplet.atan2(playerY - y, playerX - x);
        vx = PApplet.cos(angle);
        vy = PApplet.sin(angle);
        x += vx * enemySpeed;
        y += vy * enemySpeed;
    }
}