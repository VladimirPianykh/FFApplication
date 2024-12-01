package com.application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.application.shift.ShiftTask;
import com.application.shift.WorkAreaShiftManager;
import com.application.workshop.WorkArea;
import com.application.workshop.Workshop;
import com.futurefactory.Data;
import com.futurefactory.HButton;
import com.futurefactory.ProgramStarter;
import com.futurefactory.Root;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.User.Feature;

/**
 * Рабочий стол для заданий на смену.
 * Должен быть доступен службе производства.
 */
public class ShiftTaskBoard implements Feature{
	public static final ShiftTaskBoard instance=new ShiftTaskBoard();
	@SuppressWarnings("unchecked")
	public void fillTab(JPanel content,JPanel tab,Font font){
		JPanel taskPanel=new JPanel(new GridLayout(0,1));
		JScrollPane s=new JScrollPane(taskPanel){
			public void paint(Graphics g){
				Graphics2D g2=(Graphics2D)g;
				g2.setClip(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10));
				g2.setColor(Color.DARK_GRAY);
				g2.fillRect(0,0,getWidth(),getHeight());
				paintChildren(g);
				g2.setColor(Color.LIGHT_GRAY);
				g2.setStroke(new BasicStroke(getHeight()/30));
				g2.drawRoundRect(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10);
			}
		};
		s.setBounds(tab.getWidth()/20,tab.getHeight()/10,tab.getWidth()*3/5,tab.getHeight()*4/5);
		s.setOpaque(false);
		s.getViewport().setOpaque(false);
		taskPanel.setOpaque(false);
		HButton addTask=new HButton(20,5){
			@Override
			public void paint(Graphics g){
				Graphics2D g2=(Graphics2D)g;
				g2.setClip(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10));
				g2.setStroke(new BasicStroke(getHeight()/20));
				g2.setColor(new Color(40,40+scale*5,40));
				g2.fillRoundRect(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10);
				g2.setStroke(new BasicStroke(getHeight()/20));
				g2.setColor(new Color(60,200-scale*10,40));
				g2.drawLine(getWidth()/2,getHeight()/4,getWidth()/2,getHeight()*3/4);
				g2.drawLine(getWidth()/2-getHeight()/4,getHeight()/2,getWidth()/2+getHeight()/4,getHeight()/2);
				g2.setColor(new Color(60,200-scale*5,40));
				g2.drawRoundRect(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10);
			}
		};
		addTask.setBounds(s.getX()+s.getWidth()*17/20,s.getY()+s.getHeight()*17/20,s.getWidth()/10,s.getHeight()/10);
		class TaskButton extends HButton{
			private ShiftTask t;
			public TaskButton(ShiftTask t){
				this.t=t;
				addActionListener(e->{ProgramStarter.editor.constructEditor(t,false);});
			}
			public void paint(Graphics g){
				super.paintComponent(g);
				Graphics2D g2=(Graphics2D)g;
				FontMetrics fm=g2.getFontMetrics();
				g2.setColor(Color.BLACK);
				g2.drawString(t.name.toString(),getWidth()/30,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
				g2.setColor(Color.DARK_GRAY);
				g2.drawString(t.shiftDate.toString(),getWidth()*29/30-fm.stringWidth(t.shiftDate.toString()),(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
			}
		}
		addTask.addActionListener(e->{
			ShiftTask task=new ShiftTask();
			Data.getInstance().getGroup(ShiftTask.class).add(task);
			taskPanel.add(new TaskButton(task));
			taskPanel.revalidate();
		});
		for(ShiftTask t:(EditableGroup<ShiftTask>)Data.getInstance().getGroup(ShiftTask.class)){TaskButton b=new TaskButton(t);taskPanel.add(b);}
		taskPanel.doLayout();
		for(Component c:taskPanel.getComponents())c.setFont(new Font(Font.DIALOG,Font.ITALIC,c.getHeight()/2));
		JPanel areaPanel=new JPanel();
		areaPanel.setBounds(tab.getWidth()*7/10,tab.getHeight()/10,tab.getWidth()/5,tab.getHeight()*4/5);
		areaPanel.setBackground(Color.DARK_GRAY);
		class AreaLabel extends JLabel{
			private WorkArea w;
			public AreaLabel(WorkArea w){
				this.w=w;
				setPreferredSize(new Dimension(Root.SCREEN_SIZE.height/8,Root.SCREEN_SIZE.height/8));
				setOpaque(false);
			}
			public void paint(Graphics g){
				Graphics2D g2=(Graphics2D)g;
				g2.setClip(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10));
				g2.setColor(Color.DARK_GRAY);
				g2.fillRect(0,0,getWidth(),getHeight());
				FontMetrics fm=g2.getFontMetrics();
				g2.setColor(Color.WHITE);
				g2.drawString(w.name,(getWidth()-fm.stringWidth(w.name))/2,getHeight()/10+fm.getAscent()+fm.getLeading()-fm.getDescent());
				String s=w.performance==Integer.MAX_VALUE?"неограничено":(w.performance-WorkAreaShiftManager.getPerformanceOccupied(w,LocalDate.now()))+"/"+w.performance;
				g2.drawString(s,(getWidth()-fm.stringWidth(s))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
				g2.setStroke(new BasicStroke(getHeight()/20));
				g2.drawRoundRect(0,0,getWidth(),getHeight(),getHeight()/10,getHeight()/10);
			}
		}
		for(Workshop w:(EditableGroup<Workshop>)Data.getInstance().getGroup(Workshop.class))for(WorkArea p:w.parts)areaPanel.add(new AreaLabel(p));
		areaPanel.revalidate();
		tab.add(addTask);
		tab.add(s);
		tab.add(areaPanel);
	}
	public void paint(Graphics2D g2,BufferedImage image,int s){
		g2.setStroke(new BasicStroke(s/50));
		g2.drawLine(s/4,s/3,s*3/4,s/3);
		g2.drawLine(s/4,s/2,s*3/4,s/2);
		g2.drawLine(s/4,s*2/3,s*3/4,s*2/3);
	}
	public String toString(){return "Задания на смену";}
}
