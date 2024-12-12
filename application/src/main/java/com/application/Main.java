package com.application;

import com.application.access.ApplicationRole;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.shift.ShiftTask;
import com.application.workers.Team;
import com.application.workers.TeamDater;
import com.application.workers.TeamType;
import com.application.workers.Worker;
import com.application.workshop.WorkArea;
import com.application.workshop.Workshop;
import com.application.workshop.manager.WorkAreaManager;
import com.application.workshop.preparation.PreparationTask;
import com.application.workshop.preparation.WorkshopPrepStatus;
import com.application.workshop.timber.TimberProductTask;
import com.futurefactory.*;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.defaults.features.EditableList;
import com.futurefactory.defaults.features.Board;
import com.futurefactory.defaults.features.DatedList;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

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
			User.register("Управление персоналом","pass").role=ApplicationRole.WORKERS_SERVICE;
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
			shiftTasks=new EditableGroup<ShiftTask>(ShiftTask.class);
			Registrator.register(customers);
			Registrator.register(productTypes);
			Registrator.register(orders);
			Registrator.register(workshops);
			Registrator.register(productionTasks);
			Registrator.register(preparationTasks);
			Registrator.register(shiftTasks);
			// Registrator.register(EditableList.getList("Сотрудники").getGroup());
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
			for(WorkArea area:workshops.get(0).parts)preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(0),area,"Описание1",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(1).parts)preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(1),area,"Описание2",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(2).parts)preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(2),area,"Описание3",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(3).parts)preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(2),area,"Описание4",WorkshopPrepStatus.CREATED));
			Team t1=DatedList.<Team>getList("Расписание").createObject();
			t1.name="Распиловка";
			t1.type=TeamType.SAWING;
			t1.area=workshops.get(0).parts[0];
			for(int i=1;i<=4;++i){
				Worker w=EditableList.<Worker>getList("Сотрудники").createObject();
				w.name="Сотрудник "+i;
				w.workshop=workshops.get(0);
				t1.workers.add(w);
				t1.master=w;
			}
			Team t2=DatedList.<Team>getList("Расписание").createObject();
			t2.name="Сушка";
			t2.type=TeamType.DRYING;
			t2.area=workshops.get(1).parts[0];
			for(int i=5;i<=8;++i){
				Worker w=EditableList.<Worker>getList("Сотрудники").createObject();
				w.name="Сотрудник "+i;
				w.workshop=workshops.get(1);
				t2.workers.add(w);
				t2.master=w;
			}
			Team t3=DatedList.<Team>getList("Расписание").createObject();
			t3.name="Дообработка";
			t1.type=TeamType.PROCESSING;
			t3.area=workshops.get(2).parts[0];
			t3.mode=Team.Mode.TWO;
			for(int i=9;i<=12;++i){
				Worker w=EditableList.<Worker>getList("Сотрудники").createObject();
				w.name="Сотрудник "+i;
				w.workshop=workshops.get(2);
				t3.workers.add(w);
				t3.master=w;
			}
			shiftTasks.add(new ShiftTask(productTypes.get(0),3,LocalDate.now(),workshops.get(0).parts[0],""));
			Data.save();
			//Сохранение изменений для участков
			WorkAreaManager.save();
		}
		DatedList.<Team>getList("Расписание").setDateProvider(()->new TeamDater());
		Board.<TimeTableEntry>getBoard("Просмотр расписания")
			.setElementSupplier(()->{
				ArrayList<TimeTableEntry>a=new ArrayList<>();
				for(Team t:DatedList.<Team>getList("Расписание").getObjects())a.add(new TimeTableEntry(t));
				return a;
			})
			.setFilter(new Board.Filter<TimeTableEntry>(){
				static class W extends Wrapper<Worker>{
					public W(Worker w){super(w);}
					public String toString(){return var==null?"Все":var.name;}
				}
				static class S extends Wrapper<Workshop>{
					public S(Workshop w){super(w);}
					public String toString(){return var==null?"Все":var.name;}
				}
				private JComboBox<W>workerBox=new JComboBox<>();
				private JComboBox<S>workshopBox=new JComboBox<>();
				public JComponent getConfigurator(Runnable saver,ArrayList<TimeTableEntry>objects){
					workerBox.setBorder(BorderFactory.createTitledBorder("Сотрудник"));
					workerBox.addItem(new W(null));
					for(Worker w:Data.getInstance().getGroup(Worker.class))workerBox.addItem(new W(w));
					workerBox.addItemListener(e->saver.run());
					workshopBox.setBorder(BorderFactory.createTitledBorder("Цех"));
					workshopBox.addItem(new S(null));
					for(Workshop w:Data.getInstance().getGroup(Workshop.class))workshopBox.addItem(new S(w));
					workshopBox.addItemListener(e->saver.run());
					JPanel p=new JPanel(new GridLayout(1,2));
					p.add(workerBox);
					p.add(workshopBox);
					return p;
				}
				public boolean test(TimeTableEntry t){
					Worker w=((W)workerBox.getSelectedItem()).var;
					Workshop s=((S)workshopBox.getSelectedItem()).var;
					return(w==null||t.team.workers.contains(w))&&(s==null||Arrays.asList(s.parts).contains(t.area));
				}
			})
			.addTableDecorator(t->t.setDefaultEditor(Object.class,null))
			.addTableDecorator(t->t.setDefaultRenderer(Object.class,new FieldCellRenderer()));
	}
}