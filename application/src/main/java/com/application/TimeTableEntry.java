package com.application;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.application.shift.ShiftTask;
import com.application.workers.Team;
import com.application.workshop.WorkArea;
import com.futurefactory.Data;
import com.futurefactory.editor.EditorEntry;

public class TimeTableEntry implements Serializable{
	@EditorEntry(translation="Дата начала")
	public LocalDate startDate;
	@EditorEntry(translation="Участок")
	public WorkArea area;
	@EditorEntry(translation="Бригада")
	public Team team;
	@EditorEntry(translation="Задания на смену")
	public ArrayList<ShiftTask>shiftTasks=new ArrayList<>();
	public TimeTableEntry(Team team){
		this.team=team;
		startDate=team.startDate;
		area=team.area;
		for(ShiftTask t:Data.getInstance().getGroup(ShiftTask.class))
		if(
			t.workArea.equals(area)&&(
				(team.mode==Team.Mode.ONE&&Math.abs(ChronoUnit.DAYS.between(startDate,t.shiftDate))%2==0)||
				(team.mode==Team.Mode.TWO&&Math.abs(ChronoUnit.DAYS.between(startDate,t.shiftDate))%4<2)
			)
		)shiftTasks.add(t);
		System.out.println(shiftTasks.size());
	}
}
