package com.application.access;

import com.application.TaskBoard;
import com.futurefactory.Registrator;
import com.futurefactory.User;
import com.futurefactory.User.Permission;
import com.futurefactory.defaults.features.DefaultFeature;

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
			ApplicationPermission.CREATE_TIMBERPRODUCTTASK,
			ApplicationPermission.READ_ORDER,
			ApplicationPermission.READ_PRODUCTTYPE,
			ApplicationPermission.READ_WORKSHOP,
			ApplicationPermission.READ_TIMBERPRODUCTTASK,
			ApplicationPermission.CREATE_SHIFTTASK,
			ApplicationPermission.READ_SHIFTTASK
		},new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING}
	),
	TECH_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.CREATE_TIMBERPRODUCTTASK,
			ApplicationPermission.READ_PRODUCTTYPE,
            ApplicationPermission.READ_WORKSHOP,
			ApplicationPermission.READ_TIMBERPRODUCTTASK,
			ApplicationPermission.CREATE_PREPARATIONTASK,
			ApplicationPermission.READ_PREPARATIONTASK,
			ApplicationPermission.CREATE_SHIFTTASK,
			ApplicationPermission.READ_SHIFTTASK
		},new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING,TaskBoard.instance}
	);

	ApplicationRole(Permission[]permissions,User.Feature[]features){
		Registrator.register(this,features,permissions);
	}
}
