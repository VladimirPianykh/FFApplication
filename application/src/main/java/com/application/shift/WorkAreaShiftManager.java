package com.application.shift;

import com.application.workshop.WorkArea;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для хранения информации о занятости участков.
 * ТЗ
 * Реализовать выбор рабочего участка из списка свободных на указанную смену.
 */
public class WorkAreaShiftManager {
    private static HashMap<WorkArea, List<LocalDate>> reservedDates;

    private WorkAreaShiftManager() {
    }

    public static void reserveDateForArea(WorkArea area, LocalDate date) {
        if (!reservedDates.containsKey(area)) {
            reservedDates.put(area, new LinkedList<>());
        }
        reservedDates.get(area).add(date);
    }

    public static List<LocalDate> getReservedDates(WorkArea area) {
        return reservedDates.get(area);
    }

    public static List<WorkArea> getNotReservedAreas(LocalDate date) {
        List<WorkArea> result = new LinkedList<>();
        for (var entry : reservedDates.entrySet()) {
            List<LocalDate> reservedDates = entry.getValue();
            if (!reservedDates.contains(date)) {
                WorkArea area = entry.getKey();
                result.add(area);
            }
        }

        return result;
    }
}
