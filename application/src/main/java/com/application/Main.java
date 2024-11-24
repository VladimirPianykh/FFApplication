package com.application;

import com.application.access.ApplicationPermission;
import com.application.access.ApplicationRole;
import com.application.customer.Customer;
import com.application.editor.Editor;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.product.ProductType;
import com.futurefactory.Data;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.User.Feature;
import com.futurefactory.HButton;
import com.futurefactory.PathIcon;
import com.futurefactory.ProgramStarter;
import com.futurefactory.Registrator;
import com.futurefactory.Root;
import com.futurefactory.User;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Main{
	public static class TaskBoard implements Feature{
		@Override
		public void fillTab(JPanel arg0, JPanel arg1, Font arg2) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'fillTab'");
		}

		@Override
		public void paint(Graphics2D g2, BufferedImage image, int s) {
			
		}

	}
	static{
        Registrator.register(ApplicationRole.values());
        Registrator.register(ApplicationPermission.values());
	}
	public static void main(String[]args){
		Data d=Data.getInstance();
		EditableGroup<Customer>customers=null;
		EditableGroup<ProductType>productTypes=null;
		EditableGroup<Order>orders=null;
		boolean firstLaunch=User.getUserCount()==0;
		if(firstLaunch){
			//Регистрация служб
			User.register("Коммерческая служба","pass").role=ApplicationRole.COMMERCIAL_SERVICE;
			User.register("Служба производства","pass").role=ApplicationRole.PRODUCTION_SERVICE;
			User.register("Служба технолога","pass").role=ApplicationRole.TECH_SERVICE;
			//Регистрация групп элементов
			customers=new EditableGroup<Customer>(
				new PathIcon("ui/customer.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/customer_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				Customer.class
			);
			productTypes=new EditableGroup<ProductType>(
				new PathIcon("ui/product.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/product_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				ProductType.class
			);
			orders=new EditableGroup<Order>(
				new PathIcon("ui/order.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/order_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				Order.class
			){
				public JButton createElementButton(Editable e,Font font){
					return new HButton(){
						public void paint(Graphics g){
							Graphics2D g2=(Graphics2D)g;
							g2.setColor(switch(((Order)e).status){
								case APPROVED->Color.ORANGE;
								case IN_PRODUCTION->Color.YELLOW;
								case COMPLETED->Color.GREEN;
								case DRAFT->Color.GRAY;
							});
							g2.fillRect(0,0,getWidth(),getHeight());
							g2.setFont(font);
							FontMetrics fm=g2.getFontMetrics();
							g2.setColor(Color.BLACK);
							g2.drawString(e.name,getWidth()/100,(getHeight()+fm.getAscent()+fm.getLeading()-fm.getDescent())/2);
							elementIcon.paintIcon(this,g2,getWidth()-elementIcon.getIconWidth(),0);
							g2.setColor(new Color(0,0,0,scale*10));
							g2.fillRect(0,0,getWidth(),getHeight());
						}
					};
				}
			};
			d.editables.add(customers);
			d.editables.add(productTypes);
			d.editables.add(orders);
		}
		ProgramStarter.welcomeMessage="Добро пожаловать в \"Лесозавод №10 Белка\".\nВыберите службу, чтобы продолжить.";
		ProgramStarter.authRequired=false;
		ProgramStarter.editor=new Editor();
		ProgramStarter.runProgram();
		if(firstLaunch){
			//Ввод тестовых данных
			productTypes.add(new ProductType("Сырые пиломатериалы"));
			productTypes.add(new ProductType("Сухие пиломатериалы"));
			productTypes.add(new ProductType("Рейки"));
			productTypes.add(new ProductType("Строганные доски"));
			productTypes.add(new ProductType("Брус"));
			productTypes.add(new ProductType("Пеллеты"));
			customers.add(new Customer("Boris Aushev"));
			customers.add(new Customer("Vladimir Pianykh"));
			orders.add(new Order(LocalDate.now(),customers.get(0),productTypes.get(0),3,"",OrderStatus.APPROVED));
			orders.add(new Order(LocalDate.now(),customers.get(1),productTypes.get(1),3,"",OrderStatus.APPROVED));
			orders.add(new Order(LocalDate.now(),customers.get(1),productTypes.get(2),3,"",OrderStatus.APPROVED));
			Data.save();
		}
	}
}