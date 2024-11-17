package com.application.editor;

import com.application.customer.Customer;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.product.ProductType;
import com.futurefactory.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

public class OrderEditor extends JPanel {
    public OrderEditor(Order order, Container parent) {
        setSize(parent.getSize());
        setBackground(new Color(102, 107, 89));

        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(15);
        setLayout(layout);

        // Поля формы
        JTextField registeredDateField = new JTextField(order.registrationDate.toString());
        JTextField requiredDateField = new JTextField(order.requiredDate.toString());
        JTextField customerInfoField = new JTextField(order.customerInfo.toString());
        JTextField productTypeField = new JTextField(order.productType.toString());
        JTextField quantityField = new JTextField(order.quantity);
        JTextArea additionalInfoField = new JTextArea(order.additionalInfo);
        JComboBox<OrderStatus> statusField = new JComboBox<>(OrderStatus.values());
        statusField.setSelectedItem(order.status);

        // Устанавливаем переход по Enter
        setEnterKeyTraversal(registeredDateField, requiredDateField);
        setEnterKeyTraversal(requiredDateField, customerInfoField);
        setEnterKeyTraversal(customerInfoField, productTypeField);
        setEnterKeyTraversal(productTypeField, quantityField);
        setEnterKeyTraversal(quantityField, additionalInfoField);
        //Не ставим т.к. доп инфа может быть на несколько строк
        //setEnterKeyTraversal(additionalInfoField, statusField);

        // Метки для полей
        add(new JLabel("Дата регистрации заказа"));
        add(registeredDateField);
        add(new JLabel("Дата выполнения (YYYY-MM-DD):"));
        add(requiredDateField);
        add(new JLabel("Информация о клиенте:"));
        add(customerInfoField);
        add(new JLabel("Тип продукта:"));
        add(productTypeField);
        add(new JLabel("Количество:"));
        add(quantityField);
        add(new JLabel("Дополнительная информация:"));
        add(new JScrollPane(additionalInfoField));
        add(new JLabel("Статус:"));
        add(statusField);

        // Кнопка создания заказа
        JButton createButton = new JButton("Сохранить");
        add(createButton);

        // Отображение результатов
        JLabel resultLabel = new JLabel();
        add(resultLabel);

        createButton.addActionListener((ActionEvent event) -> {
            try {
                // Считываем данные из формы
                LocalDate requiredDate = LocalDate.parse(requiredDateField.getText());
                String customerInfo = customerInfoField.getText();
                String productTypeName = productTypeField.getText();
                String quantity = quantityField.getText();
                String additionalInfo = additionalInfoField.getText();
                OrderStatus status = (OrderStatus) statusField.getSelectedItem();

                // Создаем объект Order
                //Не очень понял куда и как его надо сохранять поэтому оставлю так
                //TODO: обработать результат
                Order savedOrder = new Order(
                        requiredDate,
                        new Customer(customerInfo),
                        new ProductType(productTypeName),
                        quantity,
                        additionalInfo,
                        status
                );

                // Отображаем результат
                resultLabel.setText("Заказ сохранен: " + savedOrder);
                System.out.println(savedOrder);
            } catch (Exception ex) {
                //TODO: добавить обработку ошибок
                resultLabel.setText("Ошибка: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        //я не понял что такое tab1, а с ним у меня ниче не работает
        //TODO: поменять
        //parent.add(this, "tab1");
        parent.add(this);
    }

    //Чисто чтобы ознакомиться с результатом
    //TODO: удалить метод после просмотра
    public static void main(String[] args) {
        User.register("Boris", "123");
        Order order = new Order();
        order.status = OrderStatus.APPROVED;

        JFrame frame = new JFrame("Смотрим как будет выглядеть");
        frame.setSize(1920, 1080);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(1920, 1080);

        new OrderEditor(order, panel);

        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * Настройка перехода фокуса на следующее поле при нажатии Enter.
     */
    private void setEnterKeyTraversal(JComponent current, JComponent next) {
        current.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    next.requestFocus();
                }
            }
        });
    }
}
