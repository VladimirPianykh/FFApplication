package com.application.access;

import java.util.function.Supplier;

import javax.swing.SwingUtilities;

import com.application.ShiftTaskBoard;
import com.application.TaskBoard;
import com.application.TimeTableEntry;
import com.application.workers.Team;
import com.application.workers.Worker;
import com.futurefactory.Registrator;
import com.futurefactory.Root;
import com.futurefactory.User;
import com.futurefactory.Data.EditableGroup;
import com.futurefactory.PathIcon;
import com.futurefactory.User.Permission;
import com.futurefactory.defaults.features.DefaultFeature;
import com.futurefactory.defaults.features.EditableList;
import com.futurefactory.defaults.features.Board;
import com.futurefactory.defaults.features.DatedList;

public enum ApplicationRole implements User.Role{
	COMMERCIAL_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.CREATE_ORDER,
			ApplicationPermission.CREATE_CUSTOMER,
			ApplicationPermission.READ_ORDER,
			ApplicationPermission.READ_PRODUCTTYPE,
			ApplicationPermission.READ_CUSTOMER
		},()->new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING}
	),
	PRODUCTION_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.CREATE_PRODUCTTYPE,
			ApplicationPermission.CREATE_TIMBERPRODUCTTASK,
			ApplicationPermission.READ_ORDER,
			ApplicationPermission.READ_PRODUCTTYPE,
			ApplicationPermission.READ_WORKSHOP,
			ApplicationPermission.READ_TIMBERPRODUCTTASK,
		},()->new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING,ShiftTaskBoard.instance}
	),
	TECH_SERVICE(
		new ApplicationPermission[]{
			ApplicationPermission.CREATE_TIMBERPRODUCTTASK,
			ApplicationPermission.READ_PRODUCTTYPE,
            ApplicationPermission.READ_WORKSHOP,
			ApplicationPermission.READ_TIMBERPRODUCTTASK,
			ApplicationPermission.CREATE_PREPARATIONTASK,
			ApplicationPermission.READ_PREPARATIONTASK,
		},()->new User.Feature[]{DefaultFeature.HISTORY,DefaultFeature.MODEL_EDITING,TaskBoard.instance,ShiftTaskBoard.instance}
	),WORKERS_SERVICE(
		new ApplicationPermission[]{},
		()->new User.Feature[]{EditableList.registerList("Сотрудники",new EditableGroup<Worker>(
			new PathIcon("ui/worker.png",Root.SCREEN_SIZE.height/11,Root.SCREEN_SIZE.height/11),
			new PathIcon("ui/worker_add.png",Root.SCREEN_SIZE.height/11,Root.SCREEN_SIZE.height/11),
			Worker.class
		).hide()),DatedList.registerList("Расписание",Team.class),Board.registerBoard("Просмотр расписания",TimeTableEntry.class)}
	);
	ApplicationRole(Permission[]permissions,Supplier<User.Feature[]>features){
		SwingUtilities.invokeLater(()->Registrator.register(this,features.get(),permissions));
	}
}
