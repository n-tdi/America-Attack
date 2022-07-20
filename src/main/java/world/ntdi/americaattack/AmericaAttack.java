package world.ntdi.americaattack;

import processing.core.PApplet;
import processing.core.PImage;
import world.ntdi.americaattack.enemy.Enemy;
import world.ntdi.americaattack.enemy.enemies.ChinaEnemy;
import world.ntdi.americaattack.enemy.enemies.RedEnemy;
import world.ntdi.americaattack.util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmericaAttack extends PApplet {

    private float playerX = 256;
    private float playerY = 352;
    private int speed = 6;
    private List<Direction> directions = new ArrayList<>(); // Active directions
    private List<world.ntdi.americaattack.enemy.Enemy> enemies = new ArrayList<>(); // Active enemies
    private List<Bullet> bullets = new ArrayList<>(); // Active bullets
    private List<Enemy> enemiesQueue = new ArrayList<>(); // Queue of enemies to be removed from the game
    private List<Bullet> bulletsQueue = new ArrayList<>(); // Queue of bullets to be removed from the game
    private float spawnRate = 100; // Spawn rate frames
    private float bulletSpeed = 5; // Bullet speed

    // Images
    PImage bg;
    PImage[] playerAnim =  new PImage[6]; int animationFrame = 1;


    public static void main(String[] args) {
        PApplet.main("world.ntdi.americaattack.AmericaAttack");
    }

    public void settings() {
        size(771, 1056);
        enemies.add(new world.ntdi.americaattack.enemy.enemies.RedEnemy(this, random(0, width), random(0, width)));
    }

    public void setup() {
        bg = loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/Background.png");
        bg.resize(771, 1056);
        //Loading player animations
        for (int i = 1; i <= 6; i++) {
            playerAnim[i-1]=loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/American_eagle_" + i + ".png");
            playerAnim[i-1].resize(120, 0);
        }
    }

    public void draw() {
        if (frameCount % 5 == 0) {
            animationFrame++;
            animationFrame = animationFrame % 6;
        }
        drawBackground();
        noStroke();
        drawPlayer();
        movement();
        enemyMovement();
        increaseDifficulty();
        bulletMovement();

    }

    public void drawBackground() {
        background(250);
        imageMode(CORNER);
        image(bg, 0, 0);
    }

    public void drawPlayer() {
        imageMode(CENTER);
        image(playerAnim[animationFrame], playerX, playerY);
        playerX = constrain(playerX, 140, width - 140);
        playerY = constrain(playerY, 140, height - 140);
    }

    public void enemyMovement() {
        for (Enemy enemy : enemies) {
            enemy.moveEnemy(playerX, playerY);
            enemy.drawEnemy();
            for (Bullet bullet : bullets) {
                if (abs(bullet.x - enemy.x) < enemy.sizeX && abs(bullet.y - enemy.y) < enemy.sizeY) {
                    enemiesQueue.add(enemy);
                    bulletsQueue.add(bullet);
                    break;
                }
            }
            if (abs(playerX - enemy.x) < 30 && abs(playerY - enemy.y) < 30) {
                println("game over");
            }
        }
        if (bulletsQueue.size() > 0) {
            enemies.removeAll(enemiesQueue);
            bullets.removeAll(bulletsQueue);
            enemiesQueue.clear();
            bulletsQueue.clear();
        }
    }

    public void bulletMovement() {
        for (Bullet bullet : bullets) {
            bullet.move();
            bullet.drawBullet();
            if (bullet.x < 0 || bullet.x > width || bullet.y < 0 || bullet.y > height) {
                bulletsQueue.add(bullet);
            }
        }
    }

    public void increaseDifficulty() {
        if (frameCount % spawnRate == 0) {
            generateEnemy();
            for (Enemy enemy : enemies) {
                if (enemy.enemySpeed < enemy.maxEnemySpeed) {
                    enemy.enemySpeed += 0.1F;
                }
            }
            if (spawnRate > 10) {
                spawnRate -= 10;
            }
        }
    }

    public void generateEnemy() {
        int side = (int) random(0, 2);
        int side2 = (int) random(0, 2);
        if (side % 2 == 0) { // top and bottom
            //enemies.add(new RedEnemy(random(0, width), height * (side2 % 2)));
            List<Enemy> enemyList = new ArrayList<>(Arrays.asList(
                    new RedEnemy(this, random(0, width), height * (side2 % 2)),
                    new RedEnemy(this, random(0, width), height * (side2 % 2)),
                    new RedEnemy(this, random(0, width), height * (side2 % 2)),
                    new ChinaEnemy(this, random(0, width), height * (side2 % 2))));
            Enemy randomEnemy = enemyList.get((int) random(0, enemyList.size()));
            enemies.add(randomEnemy);
        } else { // sides
            //enemies.add(new RedEnemy(width * (side2 % 2), random(0, height)));
            List<Enemy> enemyList = new ArrayList<>(Arrays.asList(
                    new RedEnemy(this, width * (side2 % 2), random(0, height)),
                    new RedEnemy(this, width * (side2 % 2), random(0, height)),
                    new RedEnemy(this, width * (side2 % 2), random(0, height)),
                    new ChinaEnemy(this, width * (side2 % 2), random(0, height))));
            Enemy randomEnemy = enemyList.get((int) random(0, enemyList.size()));
            enemies.add(randomEnemy);
        }
    }

    // Called when a key is pressed
    public void keyPressed() {
        if (key == 'w') {
            if (!directions.contains(Direction.UP))
                directions.add(Direction.UP);
        }
        if (key == 'a') {
            if (!directions.contains(Direction.RIGHT))
                directions.add(Direction.RIGHT);
        }
        if (key == 's') {
            if (!directions.contains(Direction.DOWN))
                directions.add(Direction.DOWN);
        }
        if (key == 'd') {
            if (!directions.contains(Direction.LEFT))
                directions.add(Direction.LEFT);
        }
    }

    // Called when a key is released
    public void keyReleased() {
        if (key == 'w') {
            directions.remove(Direction.UP);
        }
        if (key == 'a') {
            directions.remove(Direction.RIGHT);
        }
        if (key == 's') {
            directions.remove(Direction.DOWN);
        }
        if (key == 'd') {
            directions.remove(Direction.LEFT);
        }
    }

    // Called in draw, checks if it should move the player.
    public void movement() {
        for (Direction direction : directions) {
            if (direction == Direction.UP) {
                playerY -= speed;
            }
            if (direction == Direction.LEFT) {
                playerX += speed;
            }
            if (direction == Direction.DOWN) {
                playerY += speed;
            }
            if (direction == Direction.RIGHT) {
                playerX -= speed;
            }
        }
    }

    public void mousePressed() {
        float dx = mouseX - playerX;
        float dy = mouseY - playerY;
        float angle = atan2(dy, dx);
        float vx = bulletSpeed * cos(angle);
        float vy = bulletSpeed * sin(angle);
        bullets.add(new Bullet(playerX, playerY, vx, vy));
    }

//    public abstract class Enemy {
//        protected float x, y, vx, vy, enemySpeed, maxEnemySpeed;
//
//        public Enemy(float x, float y, float vx, float vy, float enemySpeed, float maxEnemySpeed) {
//            this.x = x;
//            this.y = y;
//            this.vx = vx;
//            this.vy = vy;
//            this.enemySpeed = enemySpeed;
//            this.maxEnemySpeed = maxEnemySpeed;
//        }
//
//        public abstract void drawEnemy();
//
//        public abstract void moveEnemy(float playerX, float playerY);
//    }
//
//    public class RedEnemy extends Enemy {
//        public RedEnemy(float x, float y) {
//            super(x, y, 0, 0, 2F, 3F);
//        }
//
//        @Override
//        public void drawEnemy() {
//            rectMode(CENTER);
//            fill(30, 120, 199);
//            rect(x, y, 25, 25);
//        }
//
//        @Override
//        public void moveEnemy(float playerX, float playerY) {
//            float angle = atan2(playerY - y, playerX - x);
//            vx = cos(angle);
//            vy = sin(angle);
//            x += vx * enemySpeed;
//            y += vy * enemySpeed;
//        }
//    }
//
//    public class BlackEnemy extends Enemy {
//        public BlackEnemy(float x, float y) {
//            super(x, y, 0, 0, 4F, 5F);
//        }
//
//        @Override
//        public void drawEnemy() {
//            rectMode(CENTER);
//            fill(10, 10, 10);
//            rect(x, y, 40, 40);
//        }
//
//        @Override
//        public void moveEnemy(float playerX, float playerY) {
//            float angle = atan2(playerY - y, playerX - x);
//            vx = cos(angle);
//            vy = sin(angle);
//            x += vx * enemySpeed;
//            y += vy * enemySpeed;
//        }
//    }
//
    public class Bullet {
        float x, y, vx, vy;

        public Bullet(float x, float y, float vx, float vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }

        public void drawBullet() {
            fill(94, 94, 94);
            ellipse(x, y, 20, 20);
        }

        public void move() {
            x += vx;
            y += vy;
        }
    }

}
