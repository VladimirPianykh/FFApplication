package com.application.shift;

import com.application.workshop.WorkArea;
import com.application.workshop.manager.WorkAreaManager;
import com.futurefactory.Data;

import java.time.LocalDate;
import java.util.*;

/**
 * Класс для хранения информации о занятости участков.
 * ТЗ
 * Реализовать выбор рабочего участка из списка свободных на указанную смену.
 */
public class WorkAreaShiftManager {
    private WorkAreaShiftManager() {
    }

    //Нигде не используется, но пусть будет
    public static List<LocalDate> getReservedDates(WorkArea area) {
        List<LocalDate> result = new LinkedList<>();

        var taskGroup = Data.getInstance().getGroup(ShiftTask.class);
        for (Data.Editable taskEditable : taskGroup) {
            ShiftTask task = (ShiftTask) taskEditable;
            if (task.workArea.equals(area)) {
                result.add(task.shiftDate);
            }
        }

        return result;
    }

    /**
     * @param task задание на смену
     * @return (количество уже занятых единиц + task.quantity >= 0)
     */
    public static boolean quantityMatchesLimit(ShiftTask task) {
        var taskGroup = Data.getInstance().getGroup(ShiftTask.class);

        int availablePerf = task.workArea.performance;

        for (Data.Editable taskEditable : taskGroup) {
            ShiftTask curTask = (ShiftTask) taskEditable;
            if (curTask.shiftDate.equals(task.shiftDate)
                && curTask.workArea.equals(task.workArea)
                && curTask.equals(task) == false
            ) {
                availablePerf -= curTask.quantity;
            }
        }

        return availablePerf >= task.quantity;
    }

    /**
     *
     * @param date
     * @return Список участков для которых есть хоть 1 свободная единица производства
     */
    public static List<WorkArea> getNotReservedAreas(LocalDate date) {
        var taskGroup = Data.getInstance().getGroup(ShiftTask.class);
        var areas = WorkAreaManager.getAreas();

        HashMap<WorkArea, Integer> availableMap = new HashMap<>();

        for (var area : areas) {
            availableMap.put(area, area.performance);
        }

        for (Data.Editable taskEditable : taskGroup) {
            ShiftTask task = (ShiftTask) taskEditable;
            if (task.shiftDate.equals(date) && availableMap.containsKey(task.workArea)) {
                availableMap.put(task.workArea, availableMap.get(task.workArea) - task.quantity);
            }
        }

        List<WorkArea> availableAreas = new ArrayList<>();
        for(var entry : availableMap.entrySet()) {
            if(entry.getValue() > 0) {
                availableAreas.add(entry.getKey());
            }
        }

        return availableAreas;
    }
}
