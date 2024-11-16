package com.application;

import com.application.access.ApplicationRole;
import com.application.customer.Customer;
import com.application.order.Order;
import com.application.product.ProductType;
import com.futurefactory.Data;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.PathIcon;
import com.futurefactory.ProgramStarter;
import com.futurefactory.Root;
import com.futurefactory.User;
import com.futurefactory.User.Permission;
import com.futurefactory.defaults.DefaultPermission;
import com.futurefactory.defaults.DefaultRole;

import java.util.Collections;

public class Main{
	// public static enum ApplicationFeature implements Feature{
		
	// }
	// public static enum ApplicationPermission implements Permission{
		
	// }
	static{
        Collections.addAll(User.registeredRoles,ApplicationRole.values());
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
		User.permissions.put(DefaultRole.EMPTY,new Permission[]{DefaultPermission.CREATE});
		for (ApplicationRole role : ApplicationRole.values()) {
			User.permissions.put(role, role.permissions);
		}
	}
	public static void main(String[]args){
		EditableGroup<Customer>customers=new EditableGroup<Customer>(
			new PathIcon("ui/customer.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
			new PathIcon("ui/customer_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
			Customer.class
		);
		//TODO @VladimirPianykh: add new icons
		EditableGroup<ProductType>productTypes=new EditableGroup<ProductType>(
			new PathIcon("ui/customer.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
			new PathIcon("ui/customer_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
			ProductType.class
		);
		EditableGroup<Order>orders=new EditableGroup<Order>(
			new PathIcon("ui/order.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
			new PathIcon("ui/order_add.png",Root.SCREEN_SIZE.width/20,Root.SCREEN_SIZE.width/20),
			Order.class
		);
		Data.getInstance().editables.add(customers);
		Data.getInstance().editables.add(productTypes);
		Data.getInstance().editables.add(orders);
		ProgramStarter.welcomeMessage="Добро пожаловать в \"Лесозавод №10 Белка\".\nВойдите под логином и паролем вашей службы, чтобы продолжить.";
		ProgramStarter.editor=new Editor();
		ProgramStarter.runProgram();
	}
}