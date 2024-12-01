package com.application.shift;

import com.application.ShiftTaskBoard;
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

        ArrayList<ShiftTask>tasks=ShiftTaskBoard.instance.tasks;
        for (Data.Editable taskEditable : tasks) {
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
        ArrayList<ShiftTask>tasks=ShiftTaskBoard.instance.tasks;

        int availablePerf = task.workArea.performance;

        for (ShiftTask taskEditable : tasks) {
            ShiftTask curTask = taskEditable;
            if (curTask != null && curTask.shiftDate != null && curTask.workArea != null
                && curTask.shiftDate.equals(task.shiftDate)
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
        ArrayList<ShiftTask>tasks=ShiftTaskBoard.instance.tasks;
        var areas = WorkAreaManager.getAreas();

        HashMap<WorkArea, Integer> availableMap = new HashMap<>();

        for (var area : areas) {
            availableMap.put(area, area.performance);
        }

        for (Data.Editable taskEditable : tasks) {
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
    public static int getPerformanceOccupied(WorkArea area) {
        //TODO @borisaushev: посчитать, сколько производительности используется сейчас всеми заданиями (сумма quantity)
        throw new UnsupportedOperationException();
    }
}
