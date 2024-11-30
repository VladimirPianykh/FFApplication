package com.application;

import com.application.access.ApplicationRole;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.shift.ShiftTask;
import com.application.workshop.WorkArea;
import com.application.workshop.Workshop;
import com.application.workshop.preparation.PreparationTask;
import com.application.workshop.preparation.WorkshopPrepStatus;
import com.application.workshop.timber.TimberProductTask;
import com.futurefactory.*;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.EditableGroup;

import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;

import javax.swing.*;

public class Main{
	public static void main(String[]args){
		EditableGroup<Customer>customers=null;
		EditableGroup<ProductType>productTypes=null;
		EditableGroup<Order>orders=null;
		EditableGroup<Workshop>workshops=null;
		EditableGroup<TimberProductTask>productionTasks=null;
		EditableGroup<PreparationTask>preparationTasks=null;
		EditableGroup<ShiftTask>shiftTasks=null;
		boolean firstLaunch=ProgramStarter.isFirstLaunch();
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
			workshops=new EditableGroup<Workshop>(
				new PathIcon("ui/factory.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/product_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				Workshop.class
			);
			productionTasks=new EditableGroup<TimberProductTask>(
				new PathIcon("ui/order.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/order_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				TimberProductTask.class
			);
			preparationTasks=new EditableGroup<>(
				new PathIcon("ui/order.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/order_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				PreparationTask.class
			);
			shiftTasks=new EditableGroup<>(
				new PathIcon("ui/order.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				new PathIcon("ui/order_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
				ShiftTask.class
			);
			Registrator.register(customers);
			Registrator.register(productTypes);
			Registrator.register(orders);
			Registrator.register(workshops);
			Registrator.register(productionTasks);
			Registrator.register(preparationTasks);
			Registrator.register(shiftTasks);
		}
		ProgramStarter.welcomeMessage="Добро пожаловать в \"Лесозавод №10 Белка\".\nВыберите службу,чтобы продолжить.";
		ProgramStarter.authRequired=false;
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
			orders.add(new Order(LocalDate.now().plusDays(1),customers.get(0),productTypes.get(0),3,"",OrderStatus.IN_PRODUCTION));
			orders.add(new Order(LocalDate.now().plusDays(1),customers.get(1),productTypes.get(1),3,"",OrderStatus.IN_PRODUCTION));
			orders.add(new Order(LocalDate.now().plusDays(1),customers.get(1),productTypes.get(2),3,"",OrderStatus.IN_PRODUCTION));
			workshops.add(new Workshop("Лесопильный цех",
				new WorkArea[]{
					new WorkArea("Лесопильная линия №1",50),
					new WorkArea("Лесопильная линия №2",100)
				}
			));
			workshops.add(new Workshop("Сушильный комплекс",
				new WorkArea[]{
					new WorkArea("Сушильная камера №1",50),
					new WorkArea("Сушильная камера №2",60),
					new WorkArea("Сушильная камера №3",80),
					new WorkArea("Сушильная камера №4",85)
				}
			));
			workshops.add(new Workshop("Цех строжки и обработки",
				new WorkArea[]{
					new WorkArea("Линия строжки №1",50),
					new WorkArea("Линия строжки №2",80),
					new WorkArea("Линия строжки №3",100)
				}
			));
			workshops.add(new Workshop("Пеллетный цех",
				new WorkArea[]{
					new WorkArea("Дробилка",Integer.MAX_VALUE),
					new WorkArea("Сушилка",Integer.MAX_VALUE),
					new WorkArea("Гранулятор №1",Integer.MAX_VALUE),
					new WorkArea("Гранулятор №2",Integer.MAX_VALUE)
				}
			));
			productionTasks.add(new TimberProductTask(LocalDate.now().plusDays(3),orders.get(0),productTypes.get(0),3,Arrays.asList(new Workshop[]{workshops.get(0)}),""));
			productionTasks.add(new TimberProductTask(LocalDate.now().plusDays(3),orders.get(1),productTypes.get(1),3,Arrays.asList(new Workshop[]{workshops.get(0),workshops.get(1)}),""));
			productionTasks.add(new TimberProductTask(LocalDate.now().plusDays(3),orders.get(2),productTypes.get(2),3,Arrays.asList(new Workshop[]{workshops.get(0),workshops.get(1),workshops.get(2)}),""));
			for(WorkArea area:workshops.get(0).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(0),area,"Описание1",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(1).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(1),area,"Описание2",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(2).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(2),area,"Описание3",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(3).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(2),area,"Описание4",WorkshopPrepStatus.CREATED));
			shiftTasks.add(new ShiftTask(productTypes.get(0), 1, LocalDate.now().plusDays(3), workshops.get(2).parts[0], ""));
			Data.save();
		}
	}
}