package com.application.shift;

import com.application.ShiftTaskBoard;
import com.application.workshop.WorkArea;
import com.application.workshop.manager.WorkAreaManager;
import com.futurefactory.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для хранения информации о занятости участков.
 * ТЗ
 * Реализовать выбор рабочего участка из списка свободных на указанную смену.
 */
public class WorkAreaShiftManager {
    private WorkAreaShiftManager() {
    }

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

    public static List<WorkArea> getNotReservedAreas(LocalDate date) {
        ArrayList<ShiftTask>tasks=ShiftTaskBoard.instance.tasks;
        var areas = WorkAreaManager.getAreas();
        System.out.println("tasks: " + tasks.size());
        System.out.println("areas: " + areas.size());
        System.out.println("date: " + date);

        //добавляем все потом удаляем неподходящие
        HashSet<WorkArea> resultSet = new HashSet<>(areas);

        for (Data.Editable taskEditable : tasks) {
            ShiftTask task = (ShiftTask) taskEditable;
            if (task.shiftDate.equals(date)) {
                resultSet.remove(task.workArea);
            }
        }

        System.out.println("result: " + resultSet.size());
        return resultSet.stream().toList();
    }
    public static int getPerformanceOccupied(WorkArea area) {
        //TODO @borisaushev: посчитать, сколько производительности используется сейчас всеми заданиями (сумма quantity)
        throw new UnsupportedOperationException();
    }
}
