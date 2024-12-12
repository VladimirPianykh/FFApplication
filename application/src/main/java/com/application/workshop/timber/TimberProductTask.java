package com.application.workshop.timber;

import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;
import com.futurefactory.editor.VerifiedInput;
import com.application.ProductType;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.workshop.Workshop;
import com.futurefactory.Data;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.Wrapper;

import java.awt.Component;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

@VerifiedInput(verifier=TimberProductTask.Verifier.class)
public class TimberProductTask extends Data.Editable{
	public static class Verifier implements com.futurefactory.editor.Verifier{
		public String verify(Editable editable,boolean isNew){
			TimberProductTask e=(TimberProductTask)editable;
			if(e.registrationDate==null||e.startDate==null){
				return "Дата регистрации и дата начала производства не могут быть null.";
			}else if(!e.startDate.isAfter(e.registrationDate)){
				return "Дата начала производства должна быть позже даты регистрации.";
			}else if(e.order==null||(isNew&&e.order.status!=OrderStatus.APPROVED)){
				return "Задание на производство можно зарегистрировать только по заказу со статусом \"Согласовано клиентом\".";
			}else if(e.productType==null){
				return "Вид лесопродукции должен быть указан.";
			}else if(e.quantity<=0){
				return "Количество лесопродукции должно быть больше нуля.";
			}else if(e.productionWorkshops==null||e.productionWorkshops.isEmpty()){
				return "Необходимо указать цеха для изготовления лесопродукции.";
			}
			e.order.status=OrderStatus.IN_PRODUCTION;
			return "";
		}
	}
	public static class WorkshopListEditor implements EditorEntryBase{
		@SuppressWarnings("unchecked")
		public JComponent createEditorBase(Object o,Field f,Wrapper<Runnable>saver){
			try{
				JPanel p=new JPanel(new GridLayout(1,0));
				List<Workshop>l=(List<Workshop>)f.get(o);
				for(Workshop w:(EditableGroup<Workshop>)Data.getInstance().getGroup(Workshop.class)){JCheckBox b=new JCheckBox(w.name);b.setSelected(l.contains(w));p.add(b);}
				for(Component c:p.getComponents()){
					((JCheckBox)c).addActionListener(e->{
						List<Workshop>list=new ArrayList<Workshop>();
						int k=0;
						for(Workshop w:(EditableGroup<Workshop>)Data.getInstance().getGroup(Workshop.class)){
							if(((JCheckBox)p.getComponent(k)).isSelected())list.add(w);
							System.out.println(k);
							++k;
						}
						try{f.set(o,list);}catch(IllegalAccessException ex){throw new RuntimeException(ex);}
					});
				}
				return p;
			}catch(IllegalAccessException ex){throw new RuntimeException(ex);}
		}
	}
	@EditorEntry(translation="Дата регистрации задания")
	public LocalDate registrationDate;
	@EditorEntry(translation="Дата, с которой требуется начать выполнять задание")
	public LocalDate startDate;
	@EditorEntry(translation="Вид лесопродукции")
	public ProductType productType;
	@EditorEntry(translation="Заказ на лесопродукцию")
	public Order order;
	@EditorEntry(translation="Количество лесопродукции")
	public int quantity;
	@EditorEntry(translation="Цеха, которые будут задействованы в изготовлении лесопродукции",editorBaseSource=WorkshopListEditor.class)
	public List<Workshop>productionWorkshops;
	@EditorEntry(translation="Дополнительная информация")
	public String additionalInfo;
	public TimberProductTask(LocalDate startDate,
							 Order order,ProductType productType,
							 int quantity,List<Workshop>productionWorkshops,
							 String additionalInfo){
		super("Задание на производство");
		this.registrationDate=LocalDate.now();
		this.startDate=startDate;
		this.order=order;
		this.productType=productType;
		this.quantity=quantity;
		this.productionWorkshops=productionWorkshops;
		this.additionalInfo=additionalInfo;
	}
	public TimberProductTask(){
		this(LocalDate.now().plusDays(3),null,null,1,new ArrayList<Workshop>(),"");
	}
}