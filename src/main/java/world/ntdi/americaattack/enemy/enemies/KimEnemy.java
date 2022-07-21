package world.ntdi.americaattack.enemy.enemies;

import processing.core.PApplet;
import world.ntdi.americaattack.AmericaAttack;
import world.ntdi.americaattack.enemy.Enemy;

public class KimEnemy extends Enemy {
    public KimEnemy(AmericaAttack americaAttack, float x, float y) {
        super(x, y, 0, 0, 1F, 2F, 160, 160, americaAttack, "src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/kimmy_poo_");
    }
}
