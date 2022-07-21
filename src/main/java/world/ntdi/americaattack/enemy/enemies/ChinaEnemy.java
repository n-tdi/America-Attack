package world.ntdi.americaattack.enemy.enemies;

import processing.core.PApplet;
import world.ntdi.americaattack.AmericaAttack;
import world.ntdi.americaattack.enemy.Enemy;

public class ChinaEnemy extends Enemy {
    public ChinaEnemy(AmericaAttack americaAttack, float x, float y) {
        super(x, y, 0, 0, 2F, 3F, 120, 120, americaAttack, "src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/maozedong_enemy_");
    }
}
