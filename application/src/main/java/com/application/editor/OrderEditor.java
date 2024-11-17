package com.application.editor;

import com.application.customer.Customer;
import com.application.order.Order;
import com.application.order.OrderStatus;
import com.application.product.ProductType;
import com.futurefactory.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class OrderEditor extends JPanel {
    public OrderEditor(Order order, Container parent) {
        setSize(parent.getSize());
        setBackground(new Color(102, 107, 89));

        // Поля формы
        JTextField registeredDateField = new JTextField(order.registrationDate.toString()),
                requiredDateField = new JTextField(order.requiredDate.toString()),
                customerInfoField = new JTextField(order.customerInfo == null ? "" : order.customerInfo.toString()),
                productTypeField = new JTextField(order.productType == null ? "" : order.productType.toString()),
                quantityField = new JTextField(order.quantity);
        JTextArea additionalInfoField = new JTextArea(order.additionalInfo);
        JComboBox<OrderStatus> statusField = new JComboBox<>(OrderStatus.values());
        statusField.setSelectedItem(order.status);

        // Устанавливаем переход по Enter
        setEnterKeyTraversal(registeredDateField);
        setEnterKeyTraversal(requiredDateField);
        setEnterKeyTraversal(customerInfoField);
        setEnterKeyTraversal(productTypeField);
        setEnterKeyTraversal(quantityField);

        JPanel tempPanel = new JPanel();
        GridLayout layout = new GridLayout(0, 2);
        layout.setVgap(getHeight() / 100); //Можно просто `getHeight()`, ибо выше стоит `setSize(parent.getSize()).
        tempPanel.setLayout(layout);

        // Метки для полей
        tempPanel.add(new JLabel("Дата регистрации заказа (YYYY-MM-DD):"));
        tempPanel.add(registeredDateField);
        tempPanel.add(new JLabel("Дата выполнения (YYYY-MM-DD):"));
        tempPanel.add(requiredDateField);
        tempPanel.add(new JLabel("Информация о клиенте:"));
        tempPanel.add(customerInfoField);
        tempPanel.add(new JLabel("Тип продукта:"));
        tempPanel.add(productTypeField);
        tempPanel.add(new JLabel("Количество:"));
        tempPanel.add(quantityField);
        tempPanel.add(new JLabel("Дополнительная информация:"));
        tempPanel.add(new JScrollPane(additionalInfoField));
        tempPanel.add(new JLabel("Статус:"));
        tempPanel.add(statusField);
        JButton createButton = new JButton("Сохранить");
        tempPanel.add(createButton);
        JLabel resultLabel = new JLabel();
        tempPanel.add(resultLabel);

        createButton.addActionListener((ActionEvent event) -> {
            try {
                // Считываем данные из формы
                LocalDate requiredDate;
                LocalDate registerDate;
                try {
                    registerDate = LocalDate.parse(requiredDateField.getText());
                    requiredDate = LocalDate.parse(requiredDateField.getText());
                } catch (DateTimeParseException exc) {
                    resultLabel.setText("Ошибка в формате даты");
                    return;
                }
                String customerInfo = customerInfoField.getText();
                String productTypeName = productTypeField.getText();
                String quantity = quantityField.getText();
                String additionalInfo = additionalInfoField.getText();
                OrderStatus status = (OrderStatus) statusField.getSelectedItem();

                /*
                 * TODO @borisaushev: использовать JComboBox<Client> и JComboBox<ProductType>
                 * Недочет:
                 * Нам приходится сравнивать информацию о клиентах с уже существующими,
                 * А что делать если такого клиента нет?
                 * Делать нового?
                 * Тогда мы вместе с созданием заказа создаем еще и клиента
                 * Что немного странно
                 */
                Customer customer = null;
                outer:
                for (Data.EditableGroup<?> group : Data.getInstance().editables) {
                    if (group.type == Customer.class) {
                        for (Data.Editable editable : group) {
                            if (editable.name.equals(customerInfo)) {
                                customer = (Customer) editable;
                                break outer;
                            }
                        }
                    }
                }

                ProductType productType = null;
                outer:
                for (Data.EditableGroup<?> group : Data.getInstance().editables) {
                    if (group.type == ProductType.class) {
                        for (Data.Editable editable : group) {
                            if (editable.name.equals(productTypeName)) {
                                productType = (ProductType) editable;
                                break outer;
                            }
                        }
                    }
                }

                //Валидируем
                if (status == OrderStatus.APPROVED && (customer == null || productType == null || quantity.isEmpty())) {
                    //используем html чтобы все уместилось
                    resultLabel.setText("<html>Укажите информацию о клиенте,<br>" +
                            "вид лесопродукции и количество заказываемой лесопродукции</html>");
                    return;
                }

                //Если клиент не найден, создаем нового
                if (customer == null && !customerInfo.isEmpty()) {
                    customer = new Customer(customerInfo);
                }
                //Если продукт не найден, создаем новый
                if (productType == null && !productTypeName.isEmpty()) {
                    productType = new ProductType(productTypeName);
                }

                order.productType = productType;
                order.customerInfo = customer;
                order.requiredDate = requiredDate;
                order.registrationDate = registerDate;
                order.quantity = quantity;
                order.additionalInfo = additionalInfo;
                order.status = status;

                resultLabel.setText("Сохранено");

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        setLayout(null);
        /*
         * Тут надо подбирать отношение, периодически запуская приложение и
         * проверяя (для этого надо зайти за коммерческую службу и создать новый заказ),
         * всё ли нормально выглядит.
         */
        tempPanel.setBounds(getWidth()/90,getHeight()/50, getWidth() / 2, getHeight() / 2);
        add(tempPanel);

        parent.add(this, "tab1");
    }

    private void setEnterKeyTraversal(JComponent c) {
        c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "transfer");
        c.getActionMap().put("transfer", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                c.transferFocus();
            }
        });
    }
}
