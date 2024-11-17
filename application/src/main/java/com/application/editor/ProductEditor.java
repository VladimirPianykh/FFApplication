package com.application.editor;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JPanel;

import com.application.product.ProductType;

public class ProductEditor extends JPanel{
    public ProductEditor(ProductType e,Container parent){
        setSize(parent.getSize());
        setBackground(new Color(102,107,89));
        //TODO: fill ProductEditor
        parent.add(this,"tab1");
    }
}
