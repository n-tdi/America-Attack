package world.ntdi.americaattack.enemy.enemies;

import processing.core.PApplet;
import world.ntdi.americaattack.AmericaAttack;
import world.ntdi.americaattack.enemy.Enemy;

public class ChinaEnemy extends Enemy {
    AmericaAttack americaAttack;

    public ChinaEnemy(AmericaAttack americaAttack, float x, float y) {
        super(x, y, 0, 0, 1F, 2F, 40, 40);
        this.americaAttack = americaAttack;
    }

    @Override
    public void drawEnemy() {
        americaAttack.rectMode(americaAttack.CENTER);
        americaAttack.fill(10, 10, 10);
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
