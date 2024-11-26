package com.application;

import com.application.access.ApplicationPermission;
import com.application.access.ApplicationRole;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.workshop.WorkArea;
import com.application.workshop.Workshop;
import com.application.workshop.preparation.PreparationTask;
import com.application.workshop.preparation.WorkshopPrepStatus;
import com.application.workshop.timber.TimberProductTask;
import com.futurefactory.*;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.User.Feature;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Main{
	public static class TaskBoard implements Feature{
		private TaskBoard(){}
		public static TaskBoard instance=new TaskBoard();
		public void fillTab(JPanel content,JPanel tab,Font font){
			// Получение списка задач
			var tasksGroup=Data.getInstance().getGroup(PreparationTask.class);
			LinkedList<PreparationTask> tasks = new LinkedList<>();
			for(var taskEditable : tasksGroup) {
				tasks.add((PreparationTask) taskEditable);
			}
			tab.setLayout(new BorderLayout());// Используем BorderLayout для размещения компонентов
			// Создаем панель с выбором цехов и таблицей
			JPanel subTab=new JPanel(new BorderLayout());
			subTab.setOpaque(false);
			subTab.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));// Отступы
			Workshop[]list;
			@SuppressWarnings("unchecked")
			EditableGroup<Workshop>group=(EditableGroup<Workshop>)Data.getInstance().getGroup(Workshop.class);
			if(group==null)throw new RuntimeException("Не найдены цеха");
			list=new Workshop[group.size()];
			for(int i=0;i<group.size();++i)list[i]=(Workshop)group.get(i);
			JComboBox<Workshop>workshops=new JComboBox<>(list);
			workshops.setFont(font);
			workshops.setBorder(BorderFactory.createTitledBorder("Выбор цеха"));
			var tableModel=new DefaultTableModel(new String[]{"Дата","Описание","Участок"},0){
				public boolean isCellEditable(int row,int column){return false;}
			};
			var tasksTable=new JTable(tableModel);
			tasksTable.setFont(font);
			tasksTable.setRowHeight(30);
			tasksTable.getTableHeader().setFont(font.deriveFont(Font.BOLD));
			tasksTable.setFillsViewportHeight(true);
			tasksTable.setDefaultRenderer(Object.class,new TaskTableCellRenderer());
			JScrollPane scrollPane=new JScrollPane(tasksTable);
			scrollPane.setBorder(BorderFactory.createTitledBorder("Список задач"));
			JComboBox<Workshop>finalWorkshops=workshops;
			workshops.addActionListener(a->{
				if(finalWorkshops.getSelectedItem()==null)return;
				tableModel.setRowCount(0);// Очистка таблицы
				Workshop selectedWorkshop=(Workshop)finalWorkshops.getSelectedItem();
				for(WorkArea area:selectedWorkshop.parts){
					for(var prepTask:tasks){
						if(prepTask.workArea.equals(area)){
							tableModel.addRow(new Object[]{
									prepTask.preparationDate,
									prepTask.preparationDetails,
									prepTask.workArea.name
							});
						}
					}
				}
			});
			subTab.add(workshops,BorderLayout.NORTH);
			subTab.add(scrollPane,BorderLayout.CENTER);
			tab.add(subTab,BorderLayout.CENTER);
			tab.revalidate();
			tab.repaint();
		}
		public void paint(Graphics2D g2,BufferedImage image,int s){
			g2.setStroke(new BasicStroke(s/10));
			g2.drawRect(s/10,s/5,s*4/5,s*3/5);
			g2.setStroke(new BasicStroke(s/20));
			g2.drawPolygon(new int[]{s/2,s/4,s/2,s/3,s/2,s/2,s/2,s*2/3,s/2,s*3/4,s/2,s*2/3,s/2,s/2,s/2,s/3,s/2},
						 new int[]{s/2,s/2,s/2,s/3,s/2,s/4,s/2,s/3,s/2,s/2,s/2,s*2/3,s/2,s*3/4,s/2,s*2/3,s/2},16);
		}
		public String toString(){return "Рабочий стол";}
		private static class TaskTableCellRenderer extends DefaultTableCellRenderer{
			@Override
			public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column){
				Component c=super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
				// Extract LocalDate value
				LocalDate preparationDate=(LocalDate)table.getValueAt(row,0);
				LocalDate startProductionDate=LocalDate.now();// Replace with actual production LocalDate logic
				// Highlight rows based on LocalDate comparison
				if(preparationDate.equals(startProductionDate))c.setBackground(Color.RED);else if(preparationDate.equals(startProductionDate.minusDays(1))){
					c.setBackground(Color.YELLOW);
				}else{c.setBackground(Color.WHITE);}
				return c;
			}
		}
	}
	static{
		Registrator.register(ApplicationRole.values());
		Registrator.register(ApplicationPermission.values());
		Registrator.register(TaskBoard.instance);
	}
	public static void main(String[]args){
		EditableGroup<Customer>customers=null;
		EditableGroup<ProductType>productTypes=null;
		EditableGroup<Order>orders=null;
		EditableGroup<Workshop>workshops=null;
		EditableGroup<TimberProductTask>productionTasks=null;
		EditableGroup<PreparationTask>preparationTasks=null;
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
			Registrator.register(customers);
			Registrator.register(productTypes);
			Registrator.register(orders);
			Registrator.register(workshops);
			Registrator.register(productionTasks);
			Registrator.register(preparationTasks);
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
					new WorkArea("Лесопильная линия №1"),
					new WorkArea("Лесопильная линия №2")
				}
			));
			workshops.add(new Workshop("Сушильный комплекс",
				new WorkArea[]{
					new WorkArea("Сушильная камера №1"),
					new WorkArea("Сушильная камера №2"),
					new WorkArea("Сушильная камера №3"),
					new WorkArea("Сушильная камера №4")
				}
			));
			workshops.add(new Workshop("Цех строжки и обработки",
				new WorkArea[]{
					new WorkArea("Линия строжки №1"),
					new WorkArea("Линия строжки №2"),
					new WorkArea("Линия строжки №3")
				}
			));
			workshops.add(new Workshop("Пеллетный цех",
				new WorkArea[]{
					new WorkArea("Дробилка"),
					new WorkArea("Сушилка"),
					new WorkArea("Гранулятор №1"),
					new WorkArea("Гранулятор №2")
				}
			));
			productionTasks.add(new TimberProductTask(LocalDate.now().plusDays(3),orders.get(0),productTypes.get(0),3,Arrays.asList(new Workshop[]{workshops.get(0)}),""));
			productionTasks.add(new TimberProductTask(LocalDate.now().plusDays(3),orders.get(1),productTypes.get(1),3,Arrays.asList(new Workshop[]{workshops.get(0),workshops.get(1)}),""));
			productionTasks.add(new TimberProductTask(LocalDate.now().plusDays(3),orders.get(2),productTypes.get(2),3,Arrays.asList(new Workshop[]{workshops.get(0),workshops.get(1),workshops.get(2)}),""));
			for(WorkArea area:workshops.get(0).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(0),area,"Описание1",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(1).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(1),area,"Описание2",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(2).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(2),area,"Описание3",WorkshopPrepStatus.CREATED));
			for(WorkArea area:workshops.get(3).parts) preparationTasks.add(new PreparationTask(LocalDate.now().plusDays(1),productionTasks.get(2),area,"Описание4",WorkshopPrepStatus.CREATED));
			Data.save();
		}
	}
}