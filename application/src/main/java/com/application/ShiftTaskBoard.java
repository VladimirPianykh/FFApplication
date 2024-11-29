package com.application;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.futurefactory.User.Feature;

/**
 * Рабочий стол для заданий на смену.
 * Должен быть доступен службе производства.
 */
public class ShiftTaskBoard implements Feature{
	// public ArrayList<ShiftTask>tasks=new ArrayList<>();
	@Override
	public void fillTab(JPanel content,JPanel tab,Font font){
		// TODO: fill tab with shift tasks
		throw new UnsupportedOperationException("Unimplemented method 'fillTab'");
	}
	@Override
	public void paint(Graphics2D g2,BufferedImage image,int s){

		g2.drawLine(s/4,s/3,s*3/4,s/3);
		g2.drawLine(s/4,s/2,s*3/4,s/2);
		g2.drawLine(s/4,s*2/3,s*3/4,s*2/3);
		throw new UnsupportedOperationException("Unimplemented method 'paint'");
    }

}
