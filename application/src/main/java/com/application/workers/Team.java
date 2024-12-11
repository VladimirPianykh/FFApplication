package com.application.workers;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import com.futurefactory.Data.Editable;
import com.futurefactory.Selectable;
import com.application.workshop.WorkArea;
import com.application.workshop.Workshop;
import com.futurefactory.Data;
import com.futurefactory.Wrapper;
import com.futurefactory.defaults.editorbases.SelectionListEditor;
import com.futurefactory.defaults.features.TimeTable;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;
import com.futurefactory.editor.VerifiedInput;

@VerifiedInput(verifier=Team.Verifier.class)
public class Team extends Editable{
	public static class Verifier implements com.futurefactory.editor.Verifier{
		public boolean verify(Editable e,boolean isNew){
			Team t=(Team)e;
			if(t.master==null||!t.workers.contains(t.master))return false;
			@SuppressWarnings("unchecked")
			Set<Team>group=((TimeTable<Team>)TimeTable.getTable("Расписание")).getObjects();
			for(Team g:group){
				if(g==t)continue;
				for(Worker w:t.workers)if(g.workers.contains(w))return false;
			}
			for(Worker w:t.workers)if(!Arrays.asList(w.workshop.parts).contains(t.area))return false;
			for(Team g:group)if(g.area==t.area){
				if(g.mode!=t.mode)return false;
				if(t.mode==Mode.ONE){
					if(Math.abs(ChronoUnit.DAYS.between(g.startDate,t.startDate))%2==0)return false;
				}else if(Math.abs(ChronoUnit.DAYS.between(g.startDate,t.startDate))%4!=2)return false;
			}
			return true;
		}
	}
	public static class WorkAreaSelector implements EditorEntryBase{
		public JComponent createEditorBase(Object o,Field f,Wrapper<Runnable>saver){
			JComboBox<WorkArea>c=new JComboBox<>();
			for(Workshop s:Data.getInstance().getGroup(Workshop.class)){
				for(WorkArea w:s.parts)c.addItem(w);
			}
			saver.var=()->{try{f.set(o,c.getSelectedItem());}catch(IllegalAccessException ex){throw new RuntimeException(ex);}};
			return c;
		}
	}
	public static enum Mode{
		ONE("1/1"),
		TWO("2/2");
		private String translation;
		private Mode(String translation){this.translation=translation;}
		public String toString(){return translation;}
	}
	@EditorEntry(translation="Номер")
	public int number;
	@EditorEntry(translation="Вид бригады")
	public TeamType type=TeamType.DRYING;
	@EditorEntry(translation="Рабочий участок",editorBaseSource=WorkAreaSelector.class)
	public WorkArea area;
	@EditorEntry(translation="Дата начала работы")
	public LocalDate startDate=LocalDate.now();
	@EditorEntry(translation="Режим работы")
	public Mode mode=Mode.ONE;
	@EditorEntry(translation="Состав",editorBaseSource=SelectionListEditor.class)
	public Selectable<Worker>workers=new Selectable<>(Worker.class);
	@EditorEntry(translation="Мастер")
	public Worker master;
	public Team(){super("Новая бригада");}
}
