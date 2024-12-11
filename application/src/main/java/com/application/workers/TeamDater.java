package com.application.workers;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

import com.futurefactory.Dater;

import java.awt.Color;
import java.awt.Graphics;

public class TeamDater implements Dater<Team>{
	public JComponent apply(Team t,LocalDate u){
		JButton b=new JButton(){
			public void paintComponent(Graphics g){
				if(u.isBefore(t.startDate))g.setColor(Color.GRAY);
				else{
					if(t.mode==Team.Mode.TWO)g.setColor(ChronoUnit.DAYS.between(t.startDate,u)%4<=1?Color.GREEN:Color.WHITE);
					else g.setColor(ChronoUnit.DAYS.between(t.startDate,u)%2==0?Color.GREEN:Color.WHITE);
				}
				g.fillRect(0,0,getWidth(),getHeight());
			}
		};
		b.setBorder(BorderFactory.createTitledBorder(u.toString()));
		return b;
	}

}
