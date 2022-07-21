package world.ntdi.americaattack;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import world.ntdi.americaattack.enemy.Enemy;
import world.ntdi.americaattack.enemy.enemies.ChinaEnemy;
import world.ntdi.americaattack.enemy.enemies.KimEnemy;
import world.ntdi.americaattack.enemy.enemies.RedEnemy;
import world.ntdi.americaattack.util.Direction;
import world.ntdi.americaattack.util.GameState;

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
    public PImage[] explosionAnim = new PImage[6]; int explosionFrame = 1;
    PImage gameOverlay;
    PImage button;

    // Scoring
    int score = 0;
    int highScore = 0;
    PFont scoreFont;

    // Game state
    GameState gameState;


    public static void main(String[] args) {
        PApplet.main("world.ntdi.americaattack.AmericaAttack");
    }

    public void settings() {
        size(771, 1056);
        enemies.add(new RedEnemy(this, random(0, width), random(0, width)));
    }

    public void setup() {
        bg = loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/Background.png");
        bg.resize(771, 1056);
        //Loading player animations
        for (int i = 1; i <= 6; i++) {
            playerAnim[i-1]=loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/American_eagle_" + i + ".png");
            playerAnim[i-1].resize(120, 0);

            explosionAnim[i - 1] = loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/sam_explosion_" + i + ".png");
            explosionAnim[i - 1].resize(60, 0);
        }
        gameState = GameState.RUNNING;
        gameOverlay = loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/GameOverImg.png");
        button = loadImage("src/main/java/world/ntdi/americaattack/images/GlobAttackAssets/WoodButton.png");
        gameOverlay.resize(600, 0);
        button.resize(480, 100);
    }

    public void draw() {
        if (frameCount % 5 == 0) {
            animationFrame++;
            animationFrame = animationFrame % 6;
        }
        drawBackground();

        switch (gameState) {
            case OVER:
                drawGameOver();
                break;

            case RUNNING:
                drawScore();
                noStroke();
                drawPlayer();
                movement();
                enemyMovement();
                increaseDifficulty();
                bulletMovement();
                break;
        }
    }

    public void drawGameOver() {
        imageMode(CENTER);
        image(gameOverlay, width / 2, height / 2);
        fill(122, 64, 51);
        textAlign(CENTER);
        text("Game Over ", width / 2, height / 2 - 100);
        text("Score: " + score, width / 2, height / 2 - 40);
        text("High Score: " + highScore, width / 2, height / 2 + 10);
        image(button, width / 2, height / 2 + 100);
        fill(255, 255, 255);
        text("Restart ", width / 2, height / 2 + 105);
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
        if (bulletsQueue.size() > 0) {
            List<Enemy> toKeep = new ArrayList<>();
            for (Enemy e : enemiesQueue) {
                if (e.doneBeingDead) {
                    enemies.remove(e);
                    toKeep.add(e);
                }
            }
            bullets.removeAll(bulletsQueue);
            enemiesQueue.clear();
            bulletsQueue.clear();
            enemiesQueue.addAll(toKeep);
        }
        for (Enemy enemy : enemies) {
            enemy.moveEnemy(playerX, playerY);
            enemy.drawEnemy();
            for (Bullet bullet : bullets) {
                if (abs(bullet.x - enemy.x) < enemy.sizeX && abs(bullet.y - enemy.y) < enemy.sizeY) {
                    enemiesQueue.add(enemy);
                    bulletsQueue.add(bullet);
                    enemy.isDead = true;
                    score += 1;
                    break;
                }
            }
            if (enemy.isDead) return;
            if (abs(playerX - enemy.x) < 30 && abs(playerY - enemy.y) < 30) {
                if (score > highScore) {
                    highScore = score;
                }
                gameState = GameState.OVER;
            }
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
            float x = random(0, width);
            float y = height * (side2 % 2);

            enemies.add(getRandomEnemy(x, y));
        } else { // left and right
            float x = width * (side2 % 2);
            float y = random(0, height);

            enemies.add(getRandomEnemy(x, y));
        }
    }

    public final List<Enemy> getEnemyList(float x, float y) {
        return new ArrayList<>(Arrays.asList(
                new RedEnemy(this, x, y),
                new RedEnemy(this, x, y),
                new RedEnemy(this, x, y),
                new ChinaEnemy(this, x, y),
                new ChinaEnemy(this, x, y),
                new KimEnemy(this, x, y)
        ));
    }

    public final Enemy getRandomEnemy(float x, float y) {
        return getEnemyList(x, y).get((int) random(0, getEnemyList(x, y).size()));
    }

    public void drawScore() {
        scoreFont = createFont("Leelawadee UI Bold", 26, true);
        textFont(scoreFont);
        fill(255, 255, 255);
        textAlign(CENTER);
        text("Score: " + score, width - 90, 40);
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
        switch (gameState) {
            case RUNNING:
                float dx = mouseX - playerX;
                float dy = mouseY - playerY;
                float angle = atan2(dy, dx);
                float vx = bulletSpeed * cos(angle);
                float vy = bulletSpeed * sin(angle);
                bullets.add(new Bullet(playerX, playerY, vx, vy));
                break;

            case OVER:
                if (mouseX > (width / 2 - 120) && mouseX < (width / 2 + 120) && mouseY > height / 2 + 100 - 25 && mouseY < (height / 2 + 100 + 25)) {
                    for (int i = 0; i < enemies.size(); i++) {
                        enemies.remove(i);
                        score = 0;
                        spawnRate = 100;
                    }
                    gameState = GameState.RUNNING;
                }
                break;
        }
    }

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
