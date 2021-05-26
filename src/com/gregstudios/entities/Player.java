package com.gregstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gregstudios.main.Game;
import com.gregstudios.world.Camera;
import com.gregstudios.world.World;

public class Player extends Entity {
	
	public boolean right, left, up, down;
	
	public int lastDir = 1;

	private int frames = 0, maxFrames = 7, index = 0, maxIndex = 1;
	public boolean moved;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;

	public static int lifePlayer = 3;

	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		rightPlayer = new BufferedImage[2];
		leftPlayer = new BufferedImage[2];
		upPlayer = new BufferedImage[2];
		downPlayer = new BufferedImage[2];
		for (int i = 0; i < 2; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(64 + (i*32), 0, 32, 32);
		}
		for (int i = 0; i < 2; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(64 + (i*32), 32, 32, 32);
		}
		for (int i = 0; i < 2; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(128 + (i*32), 32, 32, 32);
		}
		for (int i = 0; i < 2; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(128 + (i*32), 0, 32, 32);
		}
	}

	public void tick() {
		depth = 1;
		moved = false;
		if(right && World.isFree((int)(x + speed), this.getY())) {
			moved = true;
			x+=speed;
			lastDir = 1;
		}else if(left &&World.isFree((int)(x - speed), this.getY())) {
			moved = true;
			x-=speed;
			lastDir = -1;
		}
		if(up && World.isFree(this.getX(), (int)(y - speed))) { 
			moved = true;
			y-=speed;
			lastDir = 2;
		}else if(down && World.isFree(this.getX(),(int)(y + speed))) {
			moved = true;
			y+=speed;
			lastDir = -2;
		}
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
		verifyGetCoin();
		updateCamera();
		if(lifePlayer == 0){
			Game.gameState = "GAME_OVER";
		}
	}

	public void verifyGetCoin(){
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if (current instanceof Coins) {
				if (Entity.isColliding(this, current)) {
					Game.coins_atual+=10;
					Game.entities.remove(i);
					return;
				}
			}else if(current instanceof MegaCoins){
				if (Entity.isColliding(this, current)) {
					Game.coins_atual+=50;
					Game.megaCoins++;
					Game.entities.remove(i);
					return;
				}
			}
		}
	}

	public void render(Graphics g){
		if (lastDir == 1){
			g.drawImage(rightPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if (lastDir == -1){
			g.drawImage(leftPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
		}if (lastDir == 2){
			g.drawImage(upPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
		}else if (lastDir == -2){
			g.drawImage(downPlayer[index], this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
	}
}
