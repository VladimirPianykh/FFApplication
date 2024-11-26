package com.application.workshop.manager;

import com.application.workshop.preparation.PreparationTask;
import com.futurefactory.Root;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PreparationTaskManager{
	private static List<PreparationTask>tasks;
	static{loadTasks();}
	public static List<PreparationTask>getTasks(){return tasks;}
	public static void registerTask(PreparationTask task){tasks.add(task);}
	public static void save(){
		try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(Root.folder+"tasks.ser"))){
			oos.writeObject(tasks);// Сериализация списка задач
		}catch(IOException e){e.printStackTrace();}
	}
	// Метод для загрузки задач из файла
	@SuppressWarnings("unchecked")
	private static void loadTasks(){
		File file=new File(Root.folder+"tasks.ser");
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
