package com.gregstudios.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.gregstudios.entities.Entity;
import com.gregstudios.entities.Player;
import com.gregstudios.graficos.Spritesheet;
import com.gregstudios.graficos.UI;
import com.gregstudios.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{

    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int WIDTH = 960;
    public static final int HEIGHT =640;
    public static final int SCALE = 2;

    private BufferedImage image;
    
    public static List<Entity> entities;
    public static Spritesheet spritesheet;
    public static World world;
    public static Player player;
        
    public UI ui;

    //Adicionando nova fonte personalizada.
    public static InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
    public static InputStream stream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
    public static InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
    public static Font fontScorePixel;
    public static Font fontRestartGamePixel;  
    public static Font fontGameOver;

    public static int coins_atual = 0;
    public static int coins_contagem = 0;
    public static int megaCoins = 0;


    //Criações relacionadas ao level e ao MENU.
    public static String gameState = "NORMAL";
    private boolean showMessageRestart = true;
    private int framesRestartMessage = 0;
    public static boolean restartGame = false;

    public static int CUR_LEVEL = 1;
    private int MAX_LEVEL = 2;
    
    public Game(){
        //Config. para iniciar o jogo.
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));       //Tela Full Screen
        //setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        //Inicializando objetos.
        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0, 0, 32, 32, 2, spritesheet.getSprite(64, 0, 32, 32));
        entities = new ArrayList<Entity>();
        world = new World("/level1.png");
        ui = new UI();

        try {
            fontScorePixel = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(52f);
            fontRestartGamePixel = Font.createFont(Font.TRUETYPE_FONT, stream1).deriveFont(70f);
            fontGameOver = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(200f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        entities.add(player);
    }

    public void initFrame(){
        frame = new JFrame("Pac-man");
        frame.add(this);
        frame.setUndecorated(true);     //Maximiza a janela e tira as bordas.
        frame.setResizable(false);
        frame.pack();
        //Ícone da janela.
        Image imagem = null;
        try {
            imagem = ImageIO.read(getClass().getResource("/icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(imagem);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start(){
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop(){
        isRunning = false;
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public void tick(){
        if (gameState == "NORMAL") {
            restartGame = false;
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.tick();
            }
            //                                                  Método para ganhar o jogo ou fazer outra ação!
            if (coins_contagem == coins_atual) {
                //System.out.println("Ganhamos o Jogo!");
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVEL) {
                    CUR_LEVEL = 1;
                }
                String newWorld = "level"+CUR_LEVEL+".png";
                World.restartGame(newWorld);
            }
        }else if (gameState == "GAME_OVER") {
            framesRestartMessage++;
            if (framesRestartMessage == 20) {
                framesRestartMessage = 0;
                if (showMessageRestart) {
                    showMessageRestart = false;
                }else {
                    showMessageRestart = true;
                }
            }
            if (restartGame) {
                restartGame = false;
                gameState = "NORMAL";
                CUR_LEVEL = 1;
                String newWorld = "level"+CUR_LEVEL+".png";
                World.restartGame(newWorld);
            }
        }
    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Renderização do jogo.
        world.render(g);
        Collections.sort(entities, Entity.nodeSorter);
        for (int i = 0; i < entities.size(); i++) {
    		Entity e = entities.get(i);
    		e.render(g);
    	}
        /***/
        g.dispose();
        g = bs.getDrawGraphics();
        //g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
        ui.render(g);
        //                              Sistema de Game Over! 
        if (gameState == "GAME_OVER") {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
            g.setFont(fontGameOver);
            g.setColor(Color.BLACK);
            g.drawString("Game Over!", Game.WIDTH*SCALE / 2 - 305, Game.HEIGHT / 2 + 105);
            g.setColor(Color.WHITE);
            g.drawString("Game Over!", Game.WIDTH*SCALE / 2 - 310, Game.HEIGHT / 2 + 100);
            g.setFont(fontRestartGamePixel);
            g.setColor(Color.BLACK);
            if (showMessageRestart) {
            g.drawString("Press >Enter< to restart!", Game.WIDTH*SCALE / 2 - 252, Game.HEIGHT / 2 + 175);
            g.setColor(Color.WHITE);
            g.drawString("Press >Enter< to restart!", Game.WIDTH*SCALE / 2 - 255, Game.HEIGHT / 2 + 170);
            }
        }
        bs.show();
    }

    public void run() {
    	//Sound.music.loop();
        requestFocus();
        long lastTime = System.nanoTime();        // Essa variável irá pegar o tempo atual do nosso computador em nano segundos para precisão
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();        // Variável que pegará o tempo atual só que de uma forma mais leve
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                tick();
                render();
                frames++;
                delta--;
            }
            if(System.currentTimeMillis() - timer >= 1000){
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }
        stop();
    }

	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
		} if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
		}
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            restartGame = true;
            Player.lifePlayer = 3;
        }
	}


	public void keyReleased(KeyEvent e) {
        
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;	
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
        
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

    public void mouseDragged(MouseEvent e) {
        
    }

    public void mouseMoved(MouseEvent e) {

    }
}
