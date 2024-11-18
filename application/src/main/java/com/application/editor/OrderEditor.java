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
	@SuppressWarnings("unchecked")
	public OrderEditor(Order order, Container parent) {
		setSize(parent.getSize());
		setBackground(new Color(102, 107, 89));

		// Поля формы
		JTextField registeredDateField = new JTextField(order.registrationDate.toString()),
				requiredDateField = new JTextField(order.requiredDate.toString()),
				quantityField = new JTextField(order.quantity);
		JTextArea additionalInfoField = new JTextArea(order.additionalInfo);
		JComboBox<OrderStatus> statusField = new JComboBox<>(OrderStatus.values());
		statusField.setSelectedItem(order.status);

		Data.EditableGroup<Customer> customers = null;
		for (Data.EditableGroup<?> group : Data.getInstance().editables)
			if (group.type == Customer.class)
				customers = (Data.EditableGroup<Customer>) group;

		Data.EditableGroup<ProductType> productTypes = null;
		for (Data.EditableGroup<?> group : Data.getInstance().editables)
			if (group.type == ProductType.class)
				productTypes = (Data.EditableGroup<ProductType>) group;

		Customer[] customersArray = new Customer[0];
		if (customers != null) {
			customersArray = new Customer[customers.size()];
			for (int i = 0; i < customersArray.length; i++)
				customersArray[i] = customers.get(i);
		}

		ProductType[] productTypeArray = new ProductType[0];
		if (productTypes != null) {
			productTypeArray = new ProductType[productTypes.size()];
			for (int i = 0; i < productTypeArray.length; i++)
				productTypeArray[i] = productTypes.get(i);
		}

		JComboBox<Customer> customerInfoField = new JComboBox<>(customersArray);
		if (order.customerInfo != null) {
			customerInfoField.setSelectedItem(order.customerInfo);
		}
		JComboBox<ProductType> productTypeField = new JComboBox<>(productTypeArray);
		if (order.productType != null) {
			productTypeField.setSelectedItem(order.productType);
		}

		// Устанавливаем переход по Enter
		setEnterKeyTraversal(registeredDateField);
		setEnterKeyTraversal(requiredDateField);
		setEnterKeyTraversal(customerInfoField);
		setEnterKeyTraversal(productTypeField);
		setEnterKeyTraversal(quantityField);

		JPanel tempPanel = new JPanel();
		GridLayout layout = new GridLayout(0, 2);
		layout.setVgap(getHeight() / 100);
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

				Customer customer = (Customer) customerInfoField.getSelectedItem();
				ProductType productType = (ProductType) productTypeField.getSelectedItem();
				String quantity = quantityField.getText();
				String additionalInfo = additionalInfoField.getText();
				OrderStatus status = (OrderStatus) statusField.getSelectedItem();

				/*
				 * TODO @borisaushev: использовать JComboBox<Client> и JComboBox<ProductType>
				 * Недочет:
				 * В тз написано, что нельзя сохранить заказ в статусе Согласован
				 * если не заполнена инфа о клиенте, тип продукции и кол-во
				 * А у нас невозможно чтобы информация о клиенте и продукте была не заполнена
				 * т.к. у нас идет выбор из предложенных вариантов
				 * */
				String s = " ( ) ";//TODO: remove
				//Валидируем
				if (status == OrderStatus.APPROVED && (customer == null || productType == null || quantity.isEmpty())) {
					//используем html чтобы все уместилось
					resultLabel.setText("<html>Укажите информацию о клиенте,<br>" +
							"вид лесопродукции и количество заказываемой лесопродукции</html>");
					return;
				}
				try{
					order.productType = productType;
					order.customerInfo = customer;
					order.requiredDate = requiredDate;
					order.registrationDate = registerDate;
					order.quantity = Integer.parseInt(quantity);
					order.additionalInfo = additionalInfo;
					order.status = status;

					resultLabel.setText("Сохранено");
				}catch(NumberFormatException ex){resultLabel.setText("Неверный формат ввода количества.");}

			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		});

		setLayout(null);
		tempPanel.setBounds(getWidth()/4,getHeight()/4,getWidth()/2,getHeight()/2);
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
