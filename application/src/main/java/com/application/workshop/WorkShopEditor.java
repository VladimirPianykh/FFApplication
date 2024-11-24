package com.application.workshop;

import com.application.editor.EditorEntry;

import java.util.ArrayList;

public class WorkShopEditor {

	@EditorEntry(translation = "Название")
	ArrayList<WorkArea> parts = new ArrayList<>();
	/*
	Создать объект для хранения информации о рабочих участках цехов завода (каждый
	участок принадлежит определенному цеху, т.е. Объекты для хранения информации о цехах
	и участках должны быть связаны между собой).
	!!! Добавить возможность хранения описания характеристик рабочего участка
	Намек на последующие добавление характеристик

	Надо как-то сделать возможность с выбором цеха смотреть его участки

	 */

}
