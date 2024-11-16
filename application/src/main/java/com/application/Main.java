package com.application;

import com.futurefactory.Data;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.PathIcon;
import com.futurefactory.ProgramStarter;
import com.futurefactory.Root;
import com.futurefactory.User;
import com.futurefactory.User.Permission;
import com.futurefactory.User.Role;
import com.futurefactory.defaults.DefaultPermission;
import com.futurefactory.defaults.DefaultRole;

public class Main{
	public static enum ApplicationRole implements Role{
		COMMERCIAL,
		PRODUCTION,
		TECH,
	}
	// public static enum ApplicationFeature implements Feature{
		
	// }
	// public static enum ApplicationPermission implements Permission{
		
	// }
	static{
		for(Role r:ApplicationRole.values())User.registeredRoles.add(r);
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
	}
	public static void main(String[]args){
		EditableGroup e=new EditableGroup(
			new PathIcon("ui/client.png",Root.SCREEN_SIZE.width/10,Root.SCREEN_SIZE.width/10),
			new PathIcon("ui/client_add.png",Root.SCREEN_SIZE.width/10,Root.SCREEN_SIZE.width/10)
		);
		//TODO: fill EditableGroup
		Data.getInstance().editables.add(e);
		ProgramStarter.welcomeMessage="Добро пожаловать в \"Лесозавод №10 Белка\".\nВойдите под логином и паролем вашей службы, чтобы продолжить.";
		ProgramStarter.editor=new Editor();
		ProgramStarter.runProgram();
	}
}