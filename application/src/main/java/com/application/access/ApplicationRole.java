package com.application.access;

import com.application.Main.TaskBoard;
import com.futurefactory.User;
import com.futurefactory.User.Permission;
import com.futurefactory.defaults.DefaultFeature;
import com.futurefactory.WorkFrame;

public enum ApplicationRole implements User.Role {
	COMMERCIAL_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.CREATE_ORDER,
			ApplicationPermission.CREATE_CUSTOMER,
			ApplicationPermission.READ_ORDER,
			ApplicationPermission.READ_PRODUCTTYPE,
			ApplicationPermission.READ_CUSTOMER
		},new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING}
	),
	PRODUCTION_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.CREATE_PRODUCTTYPE,
			ApplicationPermission.READ_ORDER,
			ApplicationPermission.READ_PRODUCTTYPE,
		},new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING}
	),
	TECH_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.READ_PRODUCTTYPE,
            ApplicationPermission.READ_WORKSHOP
		},new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING,TaskBoard.instance}
	);

	ApplicationRole(Permission[]permissions,User.Feature[]features){
		User.permissions.put(this,permissions);
		WorkFrame.ftrMap.put(this,features);
	}
}
