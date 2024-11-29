package com.application.shift;

import com.application.ProductType;
import com.application.workshop.WorkArea;
import com.futurefactory.Data;
import com.futurefactory.editor.EditorEntry;

import java.time.LocalDate;

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
     * //TODO: разобраться
     * Реализовать выбор рабочего участка из списка свободных на указанную смену.
     */
    @EditorEntry(translation = "Дата создания")
    public LocalDate creationDate;
    @EditorEntry(translation = "Вид лесопродукции")
    public ProductType productType;
    @EditorEntry(translation = "Количество лесопродукции")
    public int quantity;
    @EditorEntry(translation = "Смена, в которую будет выполняться задание")
    public LocalDate shiftDate;
    @EditorEntry(translation = "Рабочий участок")
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
}
