package com.application;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.application.workshop.WorkArea;
import com.application.workshop.Workshop;
import com.application.workshop.preparation.PreparationTask;
import com.futurefactory.Data;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.User.Feature;

/**
 * Рабочий стол для заданий на подготовку участка.
 * Должен быть доступен службе технолога.
 */
public class TaskBoard implements Feature{
	private TaskBoard(){}
	public static TaskBoard instance=new TaskBoard();
	public void fillTab(JPanel content,JPanel tab,Font font){
		// Получение списка задач
		var tasksGroup=Data.getInstance().getGroup(PreparationTask.class);
		LinkedList<PreparationTask>tasks=new LinkedList<>();
		for(var taskEditable:tasksGroup)tasks.add((PreparationTask)taskEditable);
		tab.setLayout(new BorderLayout());// Используем BorderLayout для размещения компонентов
		// Создаем панель с выбором цехов и таблицей
		JPanel subTab=new JPanel(new BorderLayout());
		subTab.setOpaque(false);
		subTab.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));// Отступы
		Workshop[]list;
		EditableGroup<Workshop>group=Data.getInstance().getGroup(Workshop.class);
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
		g2.fillOval(s/3,s/3,s/3,s/3);
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