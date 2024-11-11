package com.application;

import com.futurefactory.ProgramStarter;
import com.futurefactory.User;
import com.futurefactory.User.DefaultPermission;
import com.futurefactory.User.DefaultRole;
import com.futurefactory.User.Permission;
import com.futurefactory.User.Role;

public class Main{
	public static enum ApplicationRole implements Role{
		// STOREKEEPER,
		// ENGINEER,
		// TESTER,
		// PRODUCTION_MANAGER,
		// PROCUREMENT_MANAGER,
		// PD_MANAGER,
		// SD_MANAGER,
		// SALES_MANAGER,
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
		ProgramStarter.editor=new Editor();
		ProgramStarter.runProgram();
	}
}