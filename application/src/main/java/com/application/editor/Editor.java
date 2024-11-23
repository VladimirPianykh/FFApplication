package com.application.editor;

import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LinearGradientPaint;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;
import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.application.customer.Customer;
import com.application.order.Order;
import com.application.product.ProductType;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.Editable.ActionRecord;
import com.futurefactory.Data;
import com.futurefactory.HButton;
import com.futurefactory.IEditor;
import com.futurefactory.PathIcon;
import com.futurefactory.ProgramStarter;
import com.futurefactory.Root;

public class Editor implements IEditor{
	public void constructEditor(Editable editable){
		JDialog editor=new JDialog(ProgramStarter.frame,true);
		editor.setSize(Root.SCREEN_SIZE);
		editor.setUndecorated(true);
		editor.setLayout(null);
		CardLayout layout=new CardLayout();
		JPanel mainPanel=new JPanel(layout);
		mainPanel.setBounds(0,0,editor.getWidth(),editor.getHeight());
		PathIcon leftIcon=new PathIcon("ui/left.png",editor.getHeight()/13,editor.getHeight()/13),r=new PathIcon("ui/right.png",editor.getHeight()/13,editor.getHeight()/13);
		HButton left=new HButton(10,7){
			public void paintComponent(Graphics g){
				Graphics2D g2=(Graphics2D)g;
				int c=scale*5;
				g2.setColor(new Color(c,pressed?c:c*2,c));
				g2.fillRect(0,0,getWidth(),getHeight());
				leftIcon.paintIcon(this,g2,(getWidth()-leftIcon.getIconWidth())/2,(getHeight()-leftIcon.getIconHeight())/2);
			}
		},right=new HButton(10,7){
			public void paintComponent(Graphics g){
				Graphics2D g2=(Graphics2D)g;
				int c=scale*5;
				g2.setColor(new Color(c,pressed?c:c*2,c));
				g2.fillRect(0,0,getWidth(),getHeight());
				r.paintIcon(this,g2,(getWidth()-r.getIconWidth())/2,(getHeight()-r.getIconHeight())/2);
			}
		};
		left.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){layout.previous(mainPanel);editor.repaint();}
		});
		right.setAction(new AbstractAction(){
			public void actionPerformed(ActionEvent e){layout.next(mainPanel);editor.repaint();}
		});
		left.setBounds(editor.getWidth()/100,editor.getHeight()*4/5,editor.getHeight()/10,editor.getHeight()/10);
		right.setBounds(editor.getWidth()*99/100-editor.getHeight()/10,editor.getHeight()*4/5,editor.getHeight()/10,editor.getHeight()/10);
		left.setFocusable(false);
		right.setFocusable(false);
		editor.add(left);editor.add(right);
		editor.add(mainPanel);
		JButton ok=new JButton(){
			public void paint(Graphics g){
				g.setClip(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),getHeight(),getHeight()));
				g.setColor(getModel().isPressed()?Color.DARK_GRAY:Color.GRAY);
				g.fillRect(0,0,getWidth(),getHeight());
				g.setColor(Color.BLACK);
				FontMetrics fm=g.getFontMetrics();
				g.drawString(getText(),(getWidth()-fm.stringWidth(getText()))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
				if(getModel().isRollover()){
					g.setColor(new Color(255,255,255,200));
					((Graphics2D)g).setStroke(new BasicStroke(getHeight()/10));
					g.drawRoundRect(0,0,getWidth(),getHeight(),getHeight(),getHeight());
				}
			}
		};
		ok.setBounds(editor.getWidth()*2/5,editor.getHeight()*9/10,editor.getWidth()/5,editor.getHeight()/20);
		ok.setOpaque(false);
		ok.setText("Готово");
		ok.setFont(new Font(Font.DIALOG,Font.PLAIN,ok.getHeight()));
		JTextField nameField=new JTextField(editable.name);
		nameField.setBounds(editor.getWidth()/5,editor.getHeight()/100,editor.getWidth()*3/5,editor.getHeight()/10);
		nameField.setFont(new Font(Font.DIALOG,Font.PLAIN,nameField.getHeight()*2/3));
		nameField.setBackground(Color.DARK_GRAY);
		nameField.setForeground(Color.LIGHT_GRAY);
		ok.addActionListener(e->{editor.dispose();editable.name=nameField.getText();});
		JPanel tab1=new JPanel(null);
		tab1.setBackground(Color.BLACK);
		tab1.add(nameField);
		tab1.add(ok);
		mainPanel.add(tab1,"tab1");
		JPanel form=new JPanel(new GridLayout(2,0));
		JScrollPane sForm=new JScrollPane(form);
		for(Field f:editable.getClass().getFields()){
			if(!f.isAnnotationPresent(EditorEntry.class))continue;
			JLabel name=new JLabel(f.getName());
			name.setBackground(Color.DARK_GRAY);
			name.setForeground(Color.WHITE);
			name.setBorder(null);
			form.add(name);
			form.add(createEditorComponent(editable,f,editor.getHeight()*3/20));
		}
		sForm.setBounds(editor.getWidth()/8,editor.getHeight()/8,editor.getWidth()*3/4,Math.min(form.getComponentCount()*editor.getHeight()*3/20,editor.getHeight()*3/4));
		form.setPreferredSize(new Dimension(sForm.getWidth(),Math.max(sForm.getHeight(),form.getComponentCount()*sForm.getHeight()/5)));
		if(editable instanceof Order){
			JPanel tab2=new JPanel(null);
			tab2.setBackground(new Color(102,107,89));
			JPanel history=new JPanel(null);
			history.setPreferredSize(new Dimension(editor.getWidth(),editor.getHeight()*editable.records.size()/4));
			history.setOpaque(false);
			JScrollPane s=new JScrollPane(history,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			s.setSize(editor.getWidth(),editor.getHeight()*3/4);
			s.getViewport().setBackground(new Color(19,31,19));
			s.getVerticalScrollBar().setOpaque(false);
			s.getVerticalScrollBar().setUnitIncrement(editor.getHeight()/30);
			tab2.add(s);
			class LocalComment extends HButton{
				private static final float[]f={0,0.4f,0.5f,0.6f,1};
				private static final Color[]c1={new Color(44,66,65),new Color(47,77,75),new Color(58,97,94),new Color(47,77,75),new Color(44,66,65)};
				private static final Color[]c2={new Color(68,71,71),new Color(79,84,84),new Color(82,92,92),new Color(87,94,94),new Color(68,71,71)};
				private final BasicStroke stroke=new BasicStroke(editor.getHeight()/150);
				private ActionRecord s;
				public LocalComment(ActionRecord s,int index){
					super(15,5);
					setBounds(editor.getWidth()/10,index*editor.getHeight()/4+editor.getHeight()/40,editor.getWidth()*4/5,editor.getHeight()/5);
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
						g2.drawString("Нажмите, чтобы скопировать.",(getWidth()-fm.stringWidth("Нажмите, чтобы скопировать."))/2,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())*3/4);
					}
				}
			}
			int i=0;
			if(editable.records.isEmpty()){
				JLabel l=new JLabel("Записи пусты.");
				l.setBounds(0,0,editor.getWidth(),editor.getHeight());
				l.setFont(new Font(Font.DIALOG,Font.BOLD,editor.getHeight()/10));
				history.add(l);
			}else for(ActionRecord c:editable.records){
				history.add(new LocalComment(c,i));
				i++;
			}
			JLabel tab2Name=new JLabel("Записи");
			tab2Name.setBounds(0,editor.getHeight()*9/10,editor.getWidth(),editor.getHeight()/10);
			tab2Name.setForeground(Color.BLACK);
			tab2Name.setFont(new Font(Font.DIALOG,Font.BOLD,editor.getHeight()/40));
			tab2Name.setHorizontalAlignment(JLabel.CENTER);
			tab2.add(tab2Name);
			mainPanel.add(tab2,"tab2");
		}else if(editable instanceof Customer){
		}else if(editable instanceof ProductType){
		}else throw new IllegalArgumentException();
		layout.show(mainPanel,"tab1");
		nameField.requestFocusInWindow();
		nameField.setSelectionStart(0);
		nameField.setSelectionEnd(nameField.getText().length());
		editor.setVisible(true);
	}
	public static Component createEditorComponent(Editable o,Field f,int h){
		Component a=createEditorBase(o,f,h);
		a.setFont(new Font(Font.DIALOG,Font.PLAIN,h/2));
		a.setBackground(Color.DARK_GRAY);
		a.setForeground(Color.WHITE);
		return a;
	}
	@SuppressWarnings("unchecked")
	private static Component createEditorBase(Editable o,Field f,int h){
		try{
			if(f.getType()==String.class){
				JTextField a=new JTextField((String)f.get(o));
				a.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent e){}
					public void focusLost(FocusEvent o){try{f.set(o,a.getText());}catch(IllegalAccessException ex){}}
				});
				return a;
			}else if(f.getType()==Integer.class){
				JSpinner a=new JSpinner(new SpinnerNumberModel(1,0,10000,1));
				a.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent e){}
					public void focusLost(FocusEvent o){try{f.set(o,a.getValue());}catch(IllegalAccessException ex){}}
				});
				return a;
			}else if(Editable.class.isAssignableFrom(f.getType())){
				JComboBox<Editable>a=new JComboBox<>();
				for(Editable e:Data.getInstance().getGroup((Class<? extends Editable>)f.getType()))a.addItem(e);
				a.addFocusListener(new FocusListener(){
					public void focusGained(FocusEvent e){}
					public void focusLost(FocusEvent o){try{f.set(o,a.getSelectedItem());}catch(IllegalAccessException ex){}}
				});
				return a;
			}
		}catch(IllegalAccessException ex){}
		throw new UnsupportedOperationException("Component for "+f.getType()+" is not defined.");
	}
}
