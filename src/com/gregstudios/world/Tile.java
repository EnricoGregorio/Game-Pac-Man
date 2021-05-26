package com.gregstudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gregstudios.main.Game;

public class Tile {
	
	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0 , 32, 32);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(32, 0, 32, 32);

	public static BufferedImage TILE_WALL_HORIZONTAL_R = Game.spritesheet.getSprite(224, 0, 32, 32);
	public static BufferedImage TILE_WALL_HORIZONTAL = Game.spritesheet.getSprite(192, 0, 32, 32);
	public static BufferedImage TILE_WALL_HORIZONTAL_L = Game.spritesheet.getSprite(256, 0, 32, 32);
	
	public static BufferedImage TILE_WALL_VERTICAL_D = Game.spritesheet.getSprite(288, 0, 32, 32);
	public static BufferedImage TILE_WALL_VERTICAL = Game.spritesheet.getSprite(192, 32, 32, 32);
	public static BufferedImage TILE_WALL_VERTICAL_U = Game.spritesheet.getSprite(288, 32, 32, 32);

	public static BufferedImage TILE_WALL_DIAGONAL_RD = Game.spritesheet.getSprite(224, 32, 32, 32);
	public static BufferedImage TILE_WALL_DIAGONAL_LD = Game.spritesheet.getSprite(256, 32, 32, 32);
	public static BufferedImage TILE_WALL_DIAGONAL_RU = Game.spritesheet.getSprite(224, 64, 32, 32);
	public static BufferedImage TILE_WALL_DIAGONAL_LU = Game.spritesheet.getSprite(256, 64, 32, 32);

	public static BufferedImage TILE_WALL_3U = Game.spritesheet.getSprite(224, 96, 32, 32);
	public static BufferedImage TILE_WALL_3D = Game.spritesheet.getSprite(256, 96, 32, 32);
	public static BufferedImage TILE_WALL_3R = Game.spritesheet.getSprite(224, 128, 32, 32);
	public static BufferedImage TILE_WALL_3L = Game.spritesheet.getSprite(256, 128, 32, 32);

	private BufferedImage sprite;
	private int x,y;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
