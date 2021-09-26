package com.yoon.mystock;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DataModel {
    /**
     * id = primaryKEY
     * autoGenerate -> 자동으로 증가
     */

    @PrimaryKey
    @NonNull
    private String name;
    private String curUnitPrice;
    private String curQuantity;

    // 생성자
    public DataModel(String name, String curUnitPrice, String curQuantity) {
        this.name = name;
        this.curUnitPrice = curUnitPrice;
        this.curQuantity = curQuantity;
    }

    public int getCount(){
        return AppData.GetInstance().mDataCnt;
    }

    /**
     * toString 재정의 -> 내용확인하기 위함
     */
    @NonNull
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DataModel{");
        sb.append("name=").append(name);
        sb.append(", curUnitPrice=").append(curUnitPrice);
        sb.append(", curQuantity=").append(curQuantity);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        DataModel myEntity = (DataModel) obj;
        return myEntity.name == this.name;
    }

    public static DiffUtil.ItemCallback<DataModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<DataModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull DataModel oldItem, @NonNull DataModel newItem) {
            return oldItem.name == newItem.name && oldItem.curUnitPrice == newItem.curUnitPrice;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DataModel oldItem, @NonNull DataModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getCurUnitPrice() {
        return curUnitPrice;
    }

    public void setCurUnitPrice(String curUnitPrice) {
        this.curUnitPrice = curUnitPrice;
    }

    public String getCurQuantity() {
        return curQuantity;
    }

    public void setCurQuantity(String curQuantity) {
        this.curQuantity = curQuantity;
    }
}
