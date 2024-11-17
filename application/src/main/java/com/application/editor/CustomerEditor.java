package com.application.editor;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JPanel;

import com.application.customer.Customer;

public class CustomerEditor extends JPanel{
    public CustomerEditor(Customer e,Container parent){
        setSize(parent.getSize());
        setBackground(new Color(102,107,89));
        //TODO: fill CustomerEditor
        parent.add(this,"tab1");
    }
}
