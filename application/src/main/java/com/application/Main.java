package com.application;

import com.application.access.ApplicationPermission;
import com.application.access.ApplicationRole;
import com.application.customer.Customer;
import com.application.editor.Editor;
import com.application.order.Order;
import com.application.product.ProductType;
import com.futurefactory.Data;
import com.futurefactory.Data.Editable;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.PathIcon;
import com.futurefactory.ProgramStarter;
import com.futurefactory.Root;
import com.futurefactory.User;
import java.awt.Color;
import java.awt.Font;
import java.util.Collections;

import javax.swing.JButton;

public class Main{
	// public static enum ApplicationFeature implements Feature{
		
	// }
	static{
        Collections.addAll(User.registeredRoles,ApplicationRole.values());
        Collections.addAll(User.registeredPermissions,ApplicationPermission.values());
		// for(Feature f:ApplicationFeature.values())User.registeredFeatures.add(f);
		//user features
		// WorkFrame.ftrMap.put(DefaultRole.ENGINEER,new Feature[]{Feature.HISTORY,Feature.MODEL_EDITING});
		// WorkFrame.ftrMap.put(DefaultRole.PD_MANAGER,new Feature[]{Feature.HISTORY});
		// WorkFrame.ftrMap.put(DefaultRole.PROCUREMENT_MANAGER,new Feature[]{Feature.HISTORY,Feature.MODEL_EDITING});
		// WorkFrame.ftrMap.put(DefaultRole.PRODUCTION_MANAGER,new Feature[]{Feature.HISTORY,Feature.MODEL_EDITING});
		// WorkFrame.ftrMap.put(DefaultRole.SALES_MANAGER,new Feature[]{Feature.HISTORY});
		// WorkFrame.ftrMap.put(DefaultRole.SD_MANAGER,new Feature[]{Feature.HISTORY,Feature.MODEL_EDITING});
		// WorkFrame.ftrMap.put(DefaultRole.STOREKEEPER,new Feature[]{Feature.HISTORY,Feature.MODEL_EDITING});
		// WorkFrame.ftrMap.put(DefaultRole.TESTER,new Feature[]{Feature.HISTORY,Feature.MODEL_EDITING});
		//user permissions
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
					JButton b=super.createElementButton(e, font);
					b.setForeground(switch(((Order)e).status){
						case APPROVED->Color.ORANGE;
						case IN_PRODUCTION->Color.YELLOW;
						case COMPLETED->Color.GREEN;
						case DRAFT->b.getForeground();
					});
					return b;
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
			productTypes.add(new ProductType("Строганные доски"));
			productTypes.add(new ProductType("Рейки"));
			productTypes.add(new ProductType("Брус"));
			productTypes.add(new ProductType("Пеллеты"));
			customers.add(new Customer("Boris Aushev"));
			customers.add(new Customer("Vladimir Pianykh"));
		}
	}
}