package com.gregstudios.entities;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.gregstudios.main.Game;
import com.gregstudios.world.Camera;
import com.gregstudios.world.Node;
import com.gregstudios.world.Vector2i;
import com.gregstudios.world.World;

public class Entity {
	
	public static BufferedImage PLAYER = Game.spritesheet.getSprite(96, 0, 32, 32);
	public static BufferedImage MINICOIN_SPRITE = Game.spritesheet.getSprite(0, 32, 32, 32);
	public static BufferedImage MEGACOIN_SPRITE = Game.spritesheet.getSprite(32, 32, 32, 32);
	public static BufferedImage ENEMY1 = Game.spritesheet.getSprite(64, 64, 32, 32);
	public static BufferedImage ENEMY2 = Game.spritesheet.getSprite(64, 128, 32, 32);
	public static BufferedImage ENEMY3 = Game.spritesheet.getSprite(128, 192, 32, 32);
	public static BufferedImage ENEMY4 = Game.spritesheet.getSprite(128, 256, 32, 32);
	public static BufferedImage ENEMY_DAMAGE = Game.spritesheet.getSprite(0, 64, 32, 32);

	public static BufferedImage[] enemyDamaged;

	public static BufferedImage[] rightEnemy1;
	public static BufferedImage[] leftEnemy1;
	public static BufferedImage[] upEnemy1;
	public static BufferedImage[] downEnemy1;

	public static BufferedImage[] rightEnemy2;
	public static BufferedImage[] leftEnemy2;
	public static BufferedImage[] upEnemy2;
	public static BufferedImage[] downEnemy2;

	public static BufferedImage[] rightEnemy3;
	public static BufferedImage[] leftEnemy3;
	public static BufferedImage[] upEnemy3;
	public static BufferedImage[] downEnemy3;

	public static BufferedImage[] rightEnemy4;
	public static BufferedImage[] leftEnemy4;
	public static BufferedImage[] upEnemy4;
	public static BufferedImage[] downEnemy4;

	
    public static boolean ghostMode;
	public static int ghostFrames = 0;


	protected double x;
	protected double y;
	protected int width;
	protected int height;
	protected double speed;
	
	public int depth;

	protected List<Node> path;
	
	public boolean debug = false;
	
	private BufferedImage sprite;
	
	public static Random rand = new Random();

	private int maskx, masky, mwidth, mheight;
	
	public Entity(double x,double y,int width,int height,double speed,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public void setMask(int maskx, int masky, int mwidth, int mheight){
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}

	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		
		@Override
		public int compare(Entity n0,Entity n1) {
			if(n1.depth < n0.depth)
				return +1;
			if(n1.depth > n0.depth)
				return -1;
			return 0;
		}
		
	};
	
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*World.TILE_SIZE - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*World.TILE_SIZE - Game.HEIGHT);
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick(){
	}
	
	public double calculateDistance(int x1,int y1,int x2,int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
	
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;
				if(x < target.x * 32) {
					x++;
					Enemy.lastDir = 1;
					Enemy2.lastDir = 1;
					Enemy3.lastDir = 1;
					Enemy4.lastDir = 1;
				}else if(x > target.x * 32) {
					x--;
					Enemy.lastDir = -1;
					Enemy2.lastDir = -1;
					Enemy3.lastDir = -1;
					Enemy4.lastDir = -1;
				}else if(y < target.y * 32) {
					y++;
					Enemy.lastDir = -2;
					Enemy2.lastDir = -2;
					Enemy3.lastDir = -2;
					Enemy4.lastDir = -2;
				}else if(y > target.y * 32) {
					y--;
					Enemy.lastDir = 2;
					Enemy2.lastDir = 2;
					Enemy3.lastDir = 2;
					Enemy4.lastDir = 2;
				}
				
				if(x == target.x * 32 && y == target.y * 32) {
					path.remove(path.size() - 1);
				}
				
			}
		}
	}
	
	public static boolean isColliding(Entity e1,Entity e2){
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx ,e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		
		return e1Mask.intersects(e2Mask);
	}

	public boolean isCollidingWithPlayer(){
		Rectangle enemyCurrent = new Rectangle(getX() + maskx, getY() + masky, mwidth, mheight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 32, 32);
		
		return enemyCurrent.intersects(player);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x,this.getY() - Camera.y,null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx - Camera.x,this.getY() + masky - Camera.y,mwidth,mheight);
	}
	
}
