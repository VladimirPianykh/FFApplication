package com.application.editor;

import com.application.order.Order;
import com.application.order.OrderStatus;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
		JTextField registeredDateField = new JTextField(order.registrationDate.toString()),
		requiredDateField = new JTextField(order.requiredDate.toString()),
		customerInfoField = new JTextField(order.customerInfo.toString()),
		productTypeField = new JTextField(order.productType.toString()),
		quantityField = new JTextField(order.quantity);
		JTextArea additionalInfoField = new JTextArea(order.additionalInfo);
		JComboBox<OrderStatus> statusField = new JComboBox<>(OrderStatus.values());
		//TODO @borisaushev: разместить всё на JPanel и поставить JPanel сверху
		statusField.setSelectedItem(order.status);
		// Устанавливаем переход по Enter
		setEnterKeyTraversal(registeredDateField);
		setEnterKeyTraversal(requiredDateField);
		setEnterKeyTraversal(customerInfoField);
		setEnterKeyTraversal(productTypeField);
		setEnterKeyTraversal(quantityField);
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
		JButton createButton = new JButton("Сохранить");
		add(createButton);
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
				//TODO @borisaushev: обработать результат
				order.requiredDate=requiredDate; //Поля сохраняются вот так.
				// new Customer(customerInfo) //Никаких новых клиентов, их нужно получить из списка зарегистрированных.
				// new ProductType(productTypeName)
				// quantity,
				// additionalInfo,
				// status

				// Отображаем результат
			} catch (Exception ex) {throw new RuntimeException(ex);}
		});
		parent.add(this, "tab1");
	}

	/**
	 * Настройка перехода фокуса на следующее поле при нажатии Enter.
	 */
	private void setEnterKeyTraversal(JComponent c){
		c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"transfer");
		c.getActionMap().put("transfer",new AbstractAction(){
			public void actionPerformed(ActionEvent e){c.transferFocus();}
		});
	}
}
