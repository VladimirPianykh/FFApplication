package com.application.workshop.manager;

import com.application.workshop.WorkArea;
import com.futurefactory.Root;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkAreaManager {
    private static List<WorkArea> areas;
    static{
        loadAreas();}
    public static List<WorkArea> getAreas(){return areas;}
    public static void registerArea(WorkArea area){
        areas.add(area);}
    public static void save(){
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(Root.folder+"areas.ser"))){
            oos.writeObject(areas);// Сериализация списка задач
        }catch(IOException e){e.printStackTrace();}
    }
    // Метод для загрузки задач из файла
    @SuppressWarnings("unchecked")
    private static void loadAreas(){
        File file=new File(Root.folder+"areas.ser");
        if(file.exists()){
            try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file))){
                areas =(ArrayList<WorkArea>)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            areas = new ArrayList<>();
        }
    }
}
