package com.application.editor;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JPanel;

import com.application.order.Order;

public class OrderEditor extends JPanel{
    public OrderEditor(Order e,Container parent){
        setSize(parent.getSize());
        setBackground(new Color(102,107,89));
        //TODO @borisaushev: создать редактируемый компонент для каждого из полей заказа.
		/*
		 * Пиши прямо здесь (сразу после "Todo"), добавляй их на tab2.
		 * - При нажатии на Enter фокус должен быть передан следующему компоненту.
		 * - Не забудь задать каждому компоненту положение (bounds) относительно ширины и высоты редактора.
		 */
        parent.add(this,"tab1");
    }
}
