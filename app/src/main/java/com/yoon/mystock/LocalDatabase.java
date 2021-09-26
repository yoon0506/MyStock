package com.yoon.mystock;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database 객체 -> abstract class , RoomDatabase 상속
 * Database annotation -> Entity 클래스와 버전을 명시
 **/
@Database(entities = {DataModel.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    /**
     * 이 객체가 제공하는 Data Access Object
     * AppDatabase 가 생성되고 TodoDAO 를 통해서 조작한다.
     **/
    public abstract DataModelDAO dataModelDAO();

}