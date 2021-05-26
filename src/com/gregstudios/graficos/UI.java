package com.gregstudios.graficos;


import java.awt.Color;
import java.awt.Graphics;

import com.gregstudios.entities.Entity;
import com.gregstudios.entities.Player;
import com.gregstudios.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(Game.fontScorePixel);
		g.drawString("Score: " + Game.coins_atual, 30, 38);
		if (Player.lifePlayer == 3) {
			g.drawImage(Entity.PLAYER, 250, 10,null);
			g.drawImage(Entity.PLAYER, 300, 10,null);
		}else if (Player.lifePlayer == 2) {
			g.drawImage(Entity.PLAYER, 250, 10,null);
		}else{

		}
	}
	
}
