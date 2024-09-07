package com.application;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.Editable.ActionRecord;
import com.futurefactory.HButton;
import com.futurefactory.IEditor;
import com.futurefactory.Root;

public class Editor extends JDialog implements IEditor{
    public Editor(){super(null,ModalityType.APPLICATION_MODAL);}
    public void constructEditor(Editable editable){
        Editor th=this;
		setSize(Root.SCREEN_SIZE);
		setUndecorated(true);
        CardLayout layout=new CardLayout();
		setContentPane(new JPanel(layout));
		Model m=editable==null?new Model("New model"):(Model)editable;
		JPanel tab1=new JPanel(null);
		JPanel history=new JPanel(null);
		history.setPreferredSize(new Dimension(getWidth(),getHeight()*m.records.size()/4));
		history.setOpaque(false);
		JScrollPane s=new JScrollPane(history,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		s.setSize(getWidth(),getHeight()*3/4);
		s.getViewport().setBackground(new Color(19,31,19));
		s.getVerticalScrollBar().setOpaque(false);
		s.getVerticalScrollBar().setUnitIncrement(getHeight()/30);
		tab1.add(s);
		class LocalComment extends HButton{
			private static final float[]f={0,0.4f,0.5f,0.6f,1};
			private static final Color[]c1={new Color(44,66,65),new Color(47,77,75),new Color(58,97,94),new Color(47,77,75),new Color(44,66,65)};
			private static final Color[]c2={new Color(68,71,71),new Color(79,84,84),new Color(82,92,92),new Color(87,94,94),new Color(68,71,71)};
			private final BasicStroke stroke=new BasicStroke(th.getHeight()/150);
			private ActionRecord s;
			public LocalComment(ActionRecord s,int index){
				super(15,5);
				setBounds(th.getWidth()/10,index*th.getHeight()/4+th.getHeight()/40,th.getWidth()*4/5,th.getHeight()/5);
				this.s=s;
				setAction(new AbstractAction(){
					public void actionPerformed(ActionEvent e){
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s.text),null);
					}
				});
			}
			public void paint(Graphics g){
				Graphics2D g2=(Graphics2D)g;
				int h=getHeight()/10;
				g2.setPaint(new LinearGradientPaint(0,0,getHeight()*2,getHeight(),f,c2));
				g2.fillRoundRect(0,0,getWidth(),getHeight(),h,h);
				g2.setPaint(new LinearGradientPaint(0,0,getHeight()*2,getHeight(),f,c1));
				g2.setStroke(stroke);
				g2.drawRoundRect(0,0,getWidth(),getHeight(),h,h);
				if(s.text.charAt(0)==':'){
					String t=s.text.split(":")[1];
					switch(t){
						case "CREATED","DELETED":
							g2.setColor(t.equals("CREATED")?new Color(81,122,40):new Color(148,55,9));
							g2.setFont(new Font(Font.DIALOG,Font.PLAIN,getHeight()/2));
							FontMetrics fm=g2.getFontMetrics();
							g2.drawString("CREATED",(getWidth()-fm.stringWidth("CREATED"))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
							Shape s=g2.getFont().createGlyphVector(g2.getFontRenderContext(),"CREATED").getOutline((getWidth()-fm.stringWidth("CREATED"))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
							g2.setStroke(new BasicStroke(getHeight()/50));
							g2.setColor(new Color(255,255,255,100));
							g2.draw(s);
							g2.setStroke(new BasicStroke());
							g2.draw(s);
							// g2.setColor(new Color(15,23,12));
							// String t=s.text.substring(s.text.split(":")[1].length()+2,s.text.length());
							// g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,getHeight()/2));
							// fm=g2.getFontMetrics();
							// g2.drawString(t,(getWidth()-fm.stringWidth(t))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
						break;
					}
				}else if(s.text.charAt(0)=='>'){
					g2.setColor(new Color(87,31,14));
					g2.setFont(new Font(Font.DIALOG,Font.PLAIN,getHeight()/(4)));
					g2.drawString(s.text.split(":")[0],getWidth()/100,getHeight()-getHeight()/100);
					g2.setColor(Color.BLACK);
					String[]t=s.text.substring(s.text.split(":")[0].length()+1,s.text.length()).split("\n");
					g2.setFont(new Font(Font.MONOSPACED,Font.BOLD,getHeight()/(t.length*2)));
					FontMetrics fm=g2.getFontMetrics();
					for(int i=0;i<t.length;i++)g2.drawString(t[i],(getWidth()-fm.stringWidth(t[i]))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2+fm.getHeight()*(i-t.length/2));
				}
				g2.setColor(new Color(196,196,181));
				g2.setFont(new Font(Font.DIALOG,Font.PLAIN,getHeight()/10));
				FontMetrics fm=g2.getFontMetrics();
				String time=s.time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				g2.drawString(time,getWidth()-(getHeight()/50+fm.stringWidth(time)),getHeight()*49/50);
				if(scale!=0){
					g2.setColor(new Color(0,0,0,scale*5));
					g2.fillRoundRect(0,0,getWidth(),getHeight(),h,h);
					g2.setColor(new Color(255,255,255,scale*10));
					g2.drawString("Click to copy.",(getWidth()-fm.stringWidth("Click to copy."))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())*3/4);
				}
			}
		}
		int i=0;
		if(m.records.isEmpty()){
			JLabel l=new JLabel("Logs are empty.");
			l.setBounds(0,0,getWidth(),getHeight());
			l.setFont(new Font(Font.DIALOG,Font.BOLD,getHeight()/10));
			history.add(l);
		}else for(ActionRecord c:m.records){
			history.add(new LocalComment(c,i));
			i++;
		}
		tab1.setBackground(new Color(102,107,89));
		add(tab1,"tab1");
		layout.show(getContentPane(),"tab1");
		setVisible(true);
    }
}
