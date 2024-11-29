package com.application.shift;

import com.application.workshop.WorkArea;
import com.futurefactory.Data;

import java.time.LocalDate;
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

        var taskGroup = Data.getInstance().getGroup(ShiftTask.class);
        for (Data.Editable taskEditable : taskGroup) {
            ShiftTask task = (ShiftTask) taskEditable;
            if (task.workArea.equals(area)) {
                result.add(task.shiftDate);
            }
        }

        return result;
    }

    public static List<WorkArea> getNotReservedAreas(LocalDate date) {
        HashSet<WorkArea> resultSet = new HashSet<>();
        var taskGroup = Data.getInstance().getGroup(ShiftTask.class);

        //добавляем все потом удаляем неподходящие
        for (Data.Editable taskEditable : taskGroup) {
            ShiftTask task = (ShiftTask) taskEditable;
            resultSet.add(task.workArea);
        }

        for (Data.Editable taskEditable : taskGroup) {
            ShiftTask task = (ShiftTask) taskEditable;
            if (task.shiftDate.equals(date)) {
                resultSet.remove(task.workArea);
            }
        }

        return resultSet.stream().toList();
    }
}
