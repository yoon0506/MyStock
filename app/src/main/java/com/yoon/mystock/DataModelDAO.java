package com.yoon.mystock;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object -> Todo Entity 에 접근하기 위한 인터페이스
 *
 * @Dao annotation 추가
 * Todo 에서 어떤 동작을 할 것인지 정의한다.
 */
@Dao
public interface DataModelDAO {
    /**
     * Todo 라는 테이블에 여러 내용이 있을 것이다.
     * Todo 의 모든 내용을 List 에 담는 getAll() 이라는 메소드가 필요하다.
     * Query annotation -> 실제 sql query 를 사용할 수 있음
     * LiveData<객체> -> 해당 객체는 관찰 가능한 객체가 된다.
     **/
    @Query("SELECT * FROM DataModel")
    LiveData<List<DataModel>> getAll();

    /**
     * id로 데이터 찾기
     */
    @Query("SELECT * FROM DataModel WHERE name = :mName")
    DataModel getData(String mName);

    /**
     * Insert annotation -> 내용추가
     **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DataModel dataModel);

    /**
     * Delete annotation -> 내용 삭제
     **/
    @Delete
    void delete(DataModel dataModel);

    /**
     * title로 데이터를 찾고 입력받은 String 으로 전체 내용을 변경
     **/
    @Query("UPDATE DataModel SET name =:mName, curUnitPrice =:mCurUnitPrice, curQuantity = :mCurQuantity")
    void dataAllUpdate(String mName, String mCurUnitPrice, String mCurQuantity);

    /**
     * Clear All -> 리스트 전체삭제
     **/
    @Query("DELETE FROM DataModel")
    void clearAll();

//    /**
//     * for onItemMove
//     */
//    @Query("SELECT * FROM DataModel ORDER BY id ASC")
//    LiveData<List<DataModel>> sortByPosition();
}