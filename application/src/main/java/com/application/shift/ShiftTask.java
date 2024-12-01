package com.application.shift;

import com.application.ProductType;
import com.application.ShiftTaskBoard;
import com.application.workshop.WorkArea;
import com.futurefactory.Data;
import com.futurefactory.editor.EditorEntry;
import com.futurefactory.editor.EditorEntryBase;
import com.futurefactory.editor.VerifiedInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

/**
 * Объект для регистрации задания на смену
 * Объект должен быть доступен в
 * разделах Служба производства и Служба технолога
 */

@VerifiedInput(verifier = ShiftTask.Verifier.class)
public class ShiftTask extends Data.Editable {
    /**
     * Дата создания,
     * Вид лесопродукции,
     * Количество лесопродукции,
     * Смена, в которую будет выполняться задание (для простоты берем, что смена – это день, т.е. Здесь указывается конкретная дата, например, смена: 25.10.2024)
     * Рабочий участок
     * Дополнительное описание (произвольное текстовое описание, многострочное поле).
     */
    @EditorEntry(translation = "Дата создания")
    public LocalDate creationDate;
    @EditorEntry(translation = "Вид лесопродукции")
    public ProductType productType;
    @EditorEntry(translation = "Количество лесопродукции")
    public int quantity;
    @EditorEntry(translation = "Смена и рабочий участок", editorBaseSource = WorkAreaAndShiftDateEditor.class)
    public LocalDate shiftDate;
    /**
     * Панель для редактирования участка объединена в панель для смены
     */
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
        ShiftTaskBoard.instance.tasks.add(this);
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
                    && task.workArea != null
                    && WorkAreaShiftManager.quantityMatchesLimit(task);
        }
    }

    /**
     * Реализовать выбор рабочего участка из списка свободных на указанную смену.
     */
    public static class WorkAreaAndShiftDateEditor implements EditorEntryBase {

        /**
         * Класс предназначен только для ShiftTask, Не использовать в других случаях!!!
         * Создаем панель и накидываем на нее связанные поля для даты смены и доступных на это время участков.
         * Логика такая:
         * Каждый раз при обновлении поля для смены мы парсим введенную дату и ищем участки,
         * на которых в эту дату есть хоть 1 свободная единица производительности.
         * Эти участки теперь помещаем в JComboBox для выбора участка
         *
         * Проверку на quantity делает класс verifier
         */
        public Component createEditorBase(Data.Editable o, Field f) {
            JPanel p = new JPanel(new GridLayout(1, 2));
            ShiftTask task = (ShiftTask) o;
            //Берем все участки на которых есть хоть 1 свободная единица производительности
            List<WorkArea> availableAreas = WorkAreaShiftManager.getNotReservedAreas(task.shiftDate);
            WorkArea[] areasArray = new WorkArea[availableAreas.size()];
            for (int i = 0; i < availableAreas.size(); i++) {
                areasArray[i] = availableAreas.get(i);
            }

            JComboBox<WorkArea> newBox = new JComboBox<>(areasArray);

            // Добавляем ItemListener для отслеживания изменений выбора
            newBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    task.workArea = (WorkArea) newBox.getSelectedItem();
                    System.out.println("WorkArea updated to: " + newBox.getSelectedItem());
                }
            });
            if (task.workArea != null) {
                newBox.removeItem(task.workArea);
                newBox.addItem(task.workArea);
                newBox.setSelectedItem(task.workArea);
            }


            // Создаём текстовое поле для даты
            JTextField date = new JTextField();
            date.setText(task.shiftDate.toString());

            // Добавляем DocumentListener для обработки изменений в поле даты
            date.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    updateDateAndComboBox();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    updateDateAndComboBox();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    updateDateAndComboBox();
                }

                private void updateDateAndComboBox() {
                    try {
                        // Парсим текст в поле даты и обновляем поле в объекте
                        LocalDate parsedDate = LocalDate.parse(date.getText());
                        task.shiftDate = parsedDate;
                        System.out.println("Date updated to: " + parsedDate);
                        // Обновляем значения newBox (WorkArea)
                        updateComboBox(newBox, parsedDate);

                    } catch (Exception ex) {
                        System.out.println("Invalid date format: " + date.getText());
                    }
                }
            });

            p.add(date);
            p.add(newBox);

            return p;
        }


        private void updateComboBox(JComboBox<WorkArea> newBox, LocalDate date) {
            List<WorkArea> availableAreas = WorkAreaShiftManager.getNotReservedAreas(date);
            newBox.removeAllItems(); // Удаляем старые элементы
            for (WorkArea area : availableAreas) {
                newBox.addItem(area); // Добавляем новые значения
            }
            System.out.println("ComboBox updated with areas for date: " + date);
        }

    }
}
