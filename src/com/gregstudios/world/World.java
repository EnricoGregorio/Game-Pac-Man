package com.gregstudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.gregstudios.entities.Coins;
import com.gregstudios.entities.Enemy;
import com.gregstudios.entities.Enemy2;
import com.gregstudios.entities.Enemy3;
import com.gregstudios.entities.Enemy4;
import com.gregstudios.entities.Entity;
import com.gregstudios.entities.MegaCoins;
import com.gregstudios.entities.Player;
import com.gregstudios.graficos.Spritesheet;
import com.gregstudios.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH,HEIGHT;
	public static final int TILE_SIZE = 32;
	
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(),pixels, 0, map.getWidth());
			for(int xx = 0; xx < map.getWidth(); xx++){
				for(int yy = 0; yy < map.getHeight(); yy++){
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx*32,yy*32,Tile.TILE_FLOOR);
					if(pixelAtual == 0xFF000000){
						//Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx*32,yy*32,Tile.TILE_FLOOR);
					}else if(pixelAtual == 0xFFFFFFFF){
						//Parede
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL);
					}
					

					else if(pixelAtual == 0xFFEAEAEA){
						//Parede vertical.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_VERTICAL);
					}else if(pixelAtual == 0xFFD3D3D3){
						//Parede Vertical fechando EM CIMA.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_VERTICAL_D);
					}else if(pixelAtual == 0xFFD2D2D2){
						//Parede Vertical fechando EMBAIXO.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_VERTICAL_U);
					}
					
					else if(pixelAtual == 0xFFE2E2E2){
						//Parede Horizontal.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_HORIZONTAL);
					}else if(pixelAtual == 0xFFD5D5D5){
						//Parede Horizontal fechando à DIREITA.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_HORIZONTAL_L);
					}else if(pixelAtual == 0xFFD4D4D4){
						//Parede Horizontal fechando à ESQUERDA.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_HORIZONTAL_R);
					}


					else if(pixelAtual == 0xFFE0E0E0){
						//Parede Diagonal direita para baixo.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_DIAGONAL_RD);
					}else if(pixelAtual == 0xFFD7D7D7){
						//Parede Diagonal esquerda para baixo.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_DIAGONAL_LD);
					}else if(pixelAtual == 0xFFD1D1D1){
						//Parede Diagonal direita para cima.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_DIAGONAL_RU);
					}else if(pixelAtual == 0xFFD0D0D0){
						//Parede Diagonal  esquerda para cima.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_DIAGONAL_LU);
					}
					

					else if(pixelAtual == 0xFFD8D8D8){
						//Parede 3 direções com parede em CIMA.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_3U);
					}else if(pixelAtual == 0xFFD6D6D6){
						//Parede 3 direções com parede em BAIXO.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_3D);
					}else if(pixelAtual == 0xFFC9C9C9){
						//Parede 3 direções com parede a DIREITA.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_3R);
					}else if(pixelAtual == 0xFFC8C8C8){
						//Parede 3 direções com parede a ESQUERDA.
						tiles[xx + (yy * WIDTH)] = new WallTile(xx*32,yy*32,Tile.TILE_WALL_3L);
					}
					
					
					else if(pixelAtual == 0xFF0026FF) {
						//Player.
						Game.player.setX(xx*32);
						Game.player.setY(yy*32);
					}
					

					else if(pixelAtual == 0xFFFFD800){
						//Moedas para o nosso player comer.
						Coins coins = new Coins(xx*32,yy*32, 32, 32, 0, Entity.MINICOIN_SPRITE);
						coins.setMask(12, 12, 8, 8);
						Game.entities.add(coins);
						Game.coins_contagem+=10;
					}else if(pixelAtual == 0xFFFFC900){
						//Mega moedas para o nosso player comer.
						MegaCoins megacoins = new MegaCoins(xx*32,yy*32, 32, 32, 0, Entity.MEGACOIN_SPRITE);
						megacoins.setMask(12, 12, 8, 8);
						Game.entities.add(megacoins);
						Game.coins_contagem+=50;
					}


					else if(pixelAtual == 0xFFFF0000) {
						//Instanciar inimigo 1.
						Enemy enemy = new Enemy(xx*32, yy*32, 32, 32, 1, Entity.ENEMY1);
						Game.entities.add(enemy);
					}else if(pixelAtual == 0xFF00E4C6) {
						//Instanciar inimigo 2.
						Enemy2 enemy = new Enemy2(xx*32, yy*32, 32, 32, 1, Entity.ENEMY2);
						Game.entities.add(enemy);
					}else if(pixelAtual == 0xFFFF6A00) {
						//Instanciar inimigo 3.
						Enemy3 enemy = new Enemy3(xx*32, yy*32, 32, 32, 1, Entity.ENEMY3);
						Game.entities.add(enemy);
					}else if(pixelAtual == 0xFFFF00DC) {
						//Instanciar inimigo 4.
						Enemy4 enemy = new Enemy4(xx*32, yy*32, 32, 32, 1, Entity.ENEMY4);
						Game.entities.add(enemy);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xnext,int ynext){
		
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	}
	
	public static void restartGame(String level){
		Game.entities.clear();
		Game.entities = new ArrayList<Entity>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 32, 32, 2, Game.spritesheet.getSprite(64, 0, 32, 32));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);
	}
	
	//Método para renderizar, e renderizar apenas o que aparece na tela.
	public void render(Graphics g){
		int xstart = Camera.x / TILE_SIZE;
		int ystart = Camera.y / TILE_SIZE;
		
		int xfinal = xstart + (Game.WIDTH / TILE_SIZE);
		int yfinal = ystart + (Game.HEIGHT / TILE_SIZE);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
	
}
