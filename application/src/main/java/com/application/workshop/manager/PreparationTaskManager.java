package com.application.workshop.manager;

import com.application.workshop.preparation.PreparationTask;
import com.futurefactory.Root;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PreparationTaskManager{
	private static final String FILE_NAME="tasks.ser";// Имя файла для сохранения данных
	private static List<PreparationTask>tasks;// Список задач
	static{tasks=new ArrayList<>();loadTasks();}
	// Метод возвращает список задач
	public static List<PreparationTask>getTasks(){return tasks;}
	// Метод для регистрации новой задачи
	public static void registerTask(PreparationTask task){tasks.add(task);}
	// Метод для сохранения задач в файл
	public static void save(){
		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(Root.folder+FILE_NAME))){
			oos.writeObject(tasks);// Сериализация списка задач
		}catch(IOException e){e.printStackTrace();}
	}
	// Метод для загрузки задач из файла
	@SuppressWarnings("unchecked")
	private static void loadTasks(){
		File file=new File(Root.folder+FILE_NAME);
		if(file.exists()){
			try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file))){
				tasks=(ArrayList<PreparationTask>)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            tasks = new ArrayList<>();
        }
    }
}
