package com.gregstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.gregstudios.main.Game;
import com.gregstudios.world.AStar;
import com.gregstudios.world.Camera;
import com.gregstudios.world.Vector2i;
import com.gregstudios.world.World;

public class Enemy extends Entity {
	
	public static int lastDir = 1;

	private int frames = 0, maxFrames = 7, index = 0, maxIndex = 1;

	public Enemy(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

		rightEnemy1 = new BufferedImage[2];
		leftEnemy1 = new BufferedImage[2];
		upEnemy1 = new BufferedImage[2];
		downEnemy1 = new BufferedImage[2];

		enemyDamaged = new BufferedImage[2];

		for (int i = 0; i < 2; i++) {
			rightEnemy1[i] = Game.spritesheet.getSprite(64 + (i*32), 64, 32, 32);
		}
		for (int i = 0; i < 2; i++) {
			leftEnemy1[i] = Game.spritesheet.getSprite(128 + (i*32), 64, 32, 32);
		}
		for (int i = 0; i < 2; i++) {
			downEnemy1[i] = Game.spritesheet.getSprite(128 + (i*32), 96, 32, 32);
		}
		for (int i = 0; i < 2; i++) {
			upEnemy1[i] = Game.spritesheet.getSprite(64 + (i*32), 96, 32, 32);
		}
		
		for (int i = 0; i < 2; i++) {
			enemyDamaged[i] = Game.spritesheet.getSprite(0 + i * 32, 64, 32, 32);
		}
		
	}
	
	public void tick() {
		depth = 0;
		if (ghostMode == false) {
			if (isCollidingWithPlayer() == false) {	
				if(path == null || path.size() == 0){
					Vector2i start = new Vector2i((int)(x / 32), (int)(y / 32));
					Vector2i end = new Vector2i((int)(Game.player.x / 32), (int)(Game.player.y / 32));
					path = AStar.findPath(Game.world, start, end);
				}
				if(new Random().nextInt(100) < 90){
					followPath(path);
				}
				if(x % 32 == 0 && y % 32 == 0){
					if (new Random().nextInt(100) < 10) {
						Vector2i start = new Vector2i((int)(x / 32), (int)(y / 32));
						Vector2i end = new Vector2i((int)(Game.player.x / 32), (int)(Game.player.y / 32));
						path = AStar.findPath(Game.world, start, end);
					}
				}
			}else{
				//Estamos colidindo com o player.
				Player.lifePlayer--;
				if (Player.lifePlayer == 2) {
					Game.restartGame = false;
					Game.gameState = "NORMAL";
					String newWorld = "level"+Game.CUR_LEVEL+".png";
					World.restartGame(newWorld);
				}else if (Player.lifePlayer == 1) {
					Game.restartGame = false;
					Game.gameState = "NORMAL";
					String newWorld = "level"+Game.CUR_LEVEL+".png";
					World.restartGame(newWorld);
				}
			}
		}
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			
			}
		}

		if (Game.megaCoins != 0) {
			ghostMode = true;
			if (ghostMode == true) {
				ghostFrames++;
				if (ghostFrames == 60 * 20) {
					ghostMode = false;
					ghostFrames = 0;
					Game.megaCoins = 0;

				}
			}else{
				ghostMode = false;
				Game.megaCoins = 0;
			}
		}
	}
	
	public void render(Graphics g) {
		if(lastDir == 1) {
			g.drawImage(rightEnemy1[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(lastDir == -1) {
			g.drawImage(leftEnemy1[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		if(lastDir == 2) {
			g.drawImage(upEnemy1[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}else if(lastDir == -2) {
			g.drawImage(downEnemy1[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}if (ghostMode == true){
			g.drawImage(enemyDamaged[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
}
