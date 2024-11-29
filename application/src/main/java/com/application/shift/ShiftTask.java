package com.application.shift;

import com.application.ProductType;
import com.application.workshop.WorkArea;
import com.futurefactory.Data;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

/**
 * Объект для регистрации задания на смену
 */

public class ShiftTask extends Data.Editable {
    /**
     * Дата создания,
     * Вид лесопродукции,
     * Количество лесопродукции,
     * Смена, в которую будет выполняться задание (для простоты берем, что смена – это день, т.е. здесь указывается конкретная дата, например, смена: 25.10.2024)
     * Рабочий участок
     * Дополнительное описание (произвольное текстовое описание, многострочное поле).
     */
    @EditorEntry(translation = "Дата создания")
    public LocalDate creationDate;
    @EditorEntry(translation = "Вид лесопродукции")
    public ProductType productType;
    @EditorEntry(translation = "Количество лесопродукции")
    public int quantity;
    @EditorEntry(translation = "Смена, в которую будет выполняться задание", editorBaseSource = WorkAreaEditor.class)
    public LocalDate shiftDate;
    @EditorEntry(translation = "Рабочий участок", editorBaseSource = WorkAreaEditor.class)
    public WorkArea workArea;
    @EditorEntry(translation = "Дополнительное описание")
    public String additionalInfo;

    public ShiftTask(
            ProductType productType,
            int quantity,
            LocalDate shiftDate,
            WorkArea workArea,
            String additionalInfo
    ) {
        super(productType == null ? "Задание на смену" : productType.name + " #" + (int) (Math.random() * 100000000));
        this.creationDate = LocalDate.now();
        this.productType = productType;
        this.quantity = quantity;
        this.shiftDate = shiftDate;
        this.workArea = workArea;
        this.additionalInfo = additionalInfo == null ? "" : additionalInfo;
    }

    public ShiftTask() {
        this(null, 1, LocalDate.now().plusDays(3), null, "");
    }

    /**
     * Для регистрации задания на смену все поля, кроме дополнительного описания, должны быть заполнены.
     */
    public static class Verifier implements com.futurefactory.editor.Verifier {
        @Override
        public boolean verify(Data.Editable editable) {
            ShiftTask task = (ShiftTask) editable;
            return task.shiftDate != null
                    && task.productType != null
                    && task.workArea != null;
        }
    }

    /**
     * Реализовать выбор рабочего участка из списка свободных на указанную смену.
     */
    public class WorkAreaEditor implements EditorEntryBase {
        JComboBox<WorkArea> areasBox = new JComboBox<>();

        public Component createEditorBase(Data.Editable o, Field f) {
            JPanel p = new JPanel(new GridLayout(1, 0));
            if (f.getType() == WorkArea.class) {
                List<WorkArea> availableAreas = WorkAreaShiftManager.getNotReservedAreas(shiftDate);
                WorkArea[] areasArray = new WorkArea[availableAreas.size()];
                for (int i = 0; i < availableAreas.size(); i++) {
                    areasArray[i] = availableAreas.get(i);
                }

                this.areasBox = new JComboBox<>(areasArray);
                areasBox.addActionListener((e) -> {
                    workArea = (WorkArea) areasBox.getSelectedItem();
                });
                p.add(areasBox);

                return p;
            } else if (f.getType() == LocalDate.class) {
                JTextField date = new JTextField();

                date.addActionListener((e) -> {
                    try {
                        shiftDate = LocalDate.parse(date.getText());

                        List<WorkArea> availableAreas = WorkAreaShiftManager.getNotReservedAreas(shiftDate);
                        WorkArea[] areasArray = new WorkArea[availableAreas.size()];
                        for (int i = 0; i < availableAreas.size(); i++) {
                            areasArray[i] = availableAreas.get(i);
                        }
                        this.areasBox = new JComboBox<>(areasArray);
                        areasBox.addActionListener((ev) -> {
                            workArea = (WorkArea) areasBox.getSelectedItem();
                        });
                    } catch (Exception exc) {
                    }
                });

                p.add(date);

                return p;
            }

            throw new UnsupportedOperationException();
        }
    }
}
