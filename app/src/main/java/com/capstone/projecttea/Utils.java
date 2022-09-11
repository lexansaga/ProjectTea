package com.capstone.projecttea;

public class Utils {

    public  Utils(){

    }

    public static Object CheckTextIfNull(Object object){
        if(object == null){
           return object.toString().trim();
        }
        return object;


    }
}
