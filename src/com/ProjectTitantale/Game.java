package com.ProjectTitantale;

import com.ProjectTitantale.Input.KeyInput;
import com.ProjectTitantale.Input.MouseInput;
import com.ProjectTitantale.entity.Entity;
import com.ProjectTitantale.gfx.Sprite;
import com.ProjectTitantale.gfx.SpriteSheet;
import com.ProjectTitantale.gfx.gui.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends Canvas implements Runnable{

    public static final int WIDTH = 270;
    public static final int HEIGHT = WIDTH/14*10;
    public static final int SCALE = 4;
    public static final String TITLE = "TitanTale";

    private Thread thread;
    private boolean running = false;

    private static BufferedImage[] levels;
    private static BufferedImage[] backgrounds;

    private static int playerX, playerY;
    private static int level =0;
    private static int background = 0;

    public static int coins=0;
    public static int lives=5 ; // number of lives
    public static int deathScreenTime = 0;

    public static boolean showDeathScreen = true;
    public static boolean gameOver = false;
    public static boolean playing = false;
    //public static boolean gameComplete = false;
    public static boolean gameFinished = false;

    public static Handler handler;
    public static SpriteSheet sheet;
    public static Camera cam;
    public static Launcher launcher;
    public static MouseInput mouse;

    public static Sprite grass;
    public static Sprite powerUp;
    public static Sprite usedPowerUp;
    public static Sprite dirtWall;
    public static Sprite iceFloor;
    public static Sprite iceWall;
    public static Sprite lavaFloor;
    public static Sprite lavaWall;

    public static Sprite mushroom;
    public static Sprite lifeMushroom;
    public static Sprite coin;
    public static Sprite star;
    public static Sprite fireball;
    public static Sprite blizzard;
    public static Sprite flower;
    public static Sprite flag;
    public static Sprite pipe;

    public static Sprite[] player;
    public static Sprite[] goomba;
    public static Sprite[] koopa;
    public static Sprite[] particle;
    public static Sprite[] firePlayer;
    public static Sprite[] towerBoss;

    public static Sound jump;
    public static Sound goombaStomp;
    public static Sound levelComplete;
    public static Sound loseALife;
    public static Sound themeSong;

    public Game(){
        Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
    }

    private void init(){
        handler = new Handler();
        sheet = new SpriteSheet("/spritesheet3.png");
        cam = new Camera();
        launcher = new Launcher();
        mouse = new MouseInput();

        addKeyListener(new KeyInput());
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        grass = new Sprite(sheet,1,1);
        powerUp = new Sprite(sheet, 7,1);
        usedPowerUp = new Sprite(sheet, 8,1);
        dirtWall = new Sprite(sheet, 2,1);
        iceFloor = new Sprite(sheet,3,1);
        iceWall = new Sprite(sheet,4,1);
        lavaFloor = new Sprite(sheet,6,1);
        lavaWall = new Sprite(sheet,5,1);

        mushroom = new Sprite(sheet, 2,2);
        lifeMushroom = new Sprite(sheet, 3,2);
        coin = new Sprite(sheet,1,2);
        star = new Sprite(sheet, 4, 2);
        fireball = new Sprite(sheet,1,3);
        blizzard = new Sprite(sheet,4,3);
        flower = new Sprite(sheet, 5,2);
        flag = new Sprite(sheet,1,4);
        pipe = new Sprite(sheet, 1,5);

        player = new Sprite[8]; // array size depends on total num of animation for player
        goomba = new Sprite[8];
        koopa = new Sprite[2];
        particle = new Sprite[6];
        firePlayer = new Sprite[8];
        towerBoss = new Sprite[6];

        levels = new BufferedImage[3]; //3 because right now only 3 levels
        backgrounds = new BufferedImage[3];

        for(int i=0; i< player.length; i++){
            player[i] = new Sprite(sheet,i+1, 11);
        }

        for(int i=0; i< goomba.length; i++){
            goomba[i] = new Sprite(sheet,i+1, 15);
        }

        for(int i=0; i< koopa.length; i++){
            koopa[i] = new Sprite(sheet,i+1, 14);
        }

        for(int i=0; i< particle.length; i++){
            particle[i] = new Sprite(sheet, i+1, 13);
        }

        for(int i=0; i< firePlayer.length; i++){
            firePlayer[i] = new Sprite(sheet, i+1, 12);
        }

        for(int i=0; i< towerBoss.length; i++){
            towerBoss[i] = new Sprite(sheet, i+1, 10);
        }


        try {
            levels[0] = ImageIO.read(getClass().getResource("/towerBossLevel.png")); // level design file
            levels[1] = ImageIO.read(getClass().getResource("/level1.png")); // level design file
            levels[2] = ImageIO.read(getClass().getResource("/towerBossLevel.png")); // level design file
            backgrounds[0] = ImageIO.read(getClass().getResource("/background.jpg"));
            backgrounds[1] = ImageIO.read(getClass().getResource("/background2.jpg"));
            backgrounds[2] = ImageIO.read(getClass().getResource("/background3.jpg"));
        } catch (IOException e){
            e.printStackTrace();
        }

        jump = new Sound("/smb jump.wav");
        goombaStomp = new Sound("/smb goombastomp.wav");
        levelComplete = new Sound("/smb levelcomplete.wav");
        loseALife = new Sound("/smb losealife.wav");
        themeSong = new Sound("/smb themesong.wav");
    }

    private synchronized void start(){
        if(running) return;
        running = true;
        thread = new Thread(this, "Thread");
        thread.start();
    }

    private synchronized void stop(){
        if(!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        init();
        requestFocus();
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0;
        double ns = 1000000000.0/60.0;
        int frames = 0;
        int ticks = 0;
        while(running){
            long now = System.nanoTime();
            delta+=(now-lastTime)/ns;
            lastTime = now;
            while(delta>=1){
                tick();
                ticks++;
                delta--;
            }
            render();
            frames++;
            if(System.currentTimeMillis()-timer>1000){
                timer+=1000;
                System.out.println(frames + " Frame Per Second " + ticks + " Updates Per Second");
                frames = 0;
                ticks = 0;
            }
        }
        stop();
    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs==null){
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        //background

        //display coins
        if(!showDeathScreen){
            g.drawImage(backgrounds[background],0,0,getWidth(),getHeight(), null);
        }
        //display player's lives
        if(showDeathScreen) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            if (!gameOver) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier", Font.BOLD, 50));
                g.drawImage(Game.player[0].getBufferredImage(), 500, 300, 100, 100, null);
                g.drawString("x" + lives, 590, 400);
            } else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier", Font.BOLD, 50));
                g.drawString("Game Over!!!", 400, 400);
            }
        }

        //display winning text
        if(gameFinished){
            handler.clearLevel();
            g.setColor(Color.BLACK);
            g.fillRect(0,0,getWidth(),getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier",Font.BOLD,30));
            g.drawString("Congratulations. You have finished the Game!", 200, 200);
            g.drawString("Total Coins Collected: "+ Game.coins, 410, 300);
            g.drawString("changed: "+ Game.coins, 510, 300);
        }


        if (playing) g.translate(cam.getX(), cam.getY());
        if(!showDeathScreen&&playing) handler.render(g);
        else if (!playing) launcher.render(g);
        g.dispose();
        bs.show();
    }

    public void tick(){
        if (playing) handler.tick();

        for( Entity e: handler.entity){
            if(e.getId()== Id.player){
                if (!e.goingDownPipe) cam.tick(e);
            }
        }

        if (showDeathScreen && !gameOver && playing) deathScreenTime++;
        if (deathScreenTime>=180) {
            if (!gameOver){
                showDeathScreen = false;
                deathScreenTime = 0;
                handler.clearLevel();
                handler.createLevel(levels[level]);
            } else if (gameOver){
                showDeathScreen = false;
                deathScreenTime = 0;
                playing = false;
                gameOver = false;
            }

            //themeSong.play();
        }
    }

    public static int getFrameWidth(){
        return WIDTH*SCALE;
    }

    public static int getFrameHeight(){
        return HEIGHT*SCALE;
    }

    public static void switchLevel(){
        Game.level++;
        Game.background++;

        handler.clearLevel();
        handler.createLevel(levels[level]);

        Game.themeSong.close();
        Game.levelComplete.play();

    }

    public static Rectangle getVisibleArea(){
        for (int i=0; i<handler.entity.size(); i++){
            Entity e = handler.entity.get(i);
            if(e.getId()==Id.player){
                if(!e.goingDownPipe){
                    playerX = e.getX();
                    playerY = e.getY();

                    return new Rectangle(playerX-(getFrameWidth()/2-5), playerY-(getFrameHeight()/2-5), getFrameWidth()+10, getFrameHeight()+10);

                }
            }
        }

        return new Rectangle(playerX-(getFrameWidth()/2-5), playerY-(getFrameHeight()/2-5), getFrameWidth()+10, getFrameHeight()+10);
    }

    public static void main(String[] args){
        Game game = new Game();
        JFrame frame = new JFrame(TITLE);
        frame.add(game);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.start();
    }
}
