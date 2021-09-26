package com.yoon.mystock;


import android.os.AsyncTask;

/**
 * 비동기 처리 해주는 것은 dataModelDAO() 이다.
 * InsertAsyncTask 생성자를 만들어 dataModelDAO() 객체를 받는다.
 **/
public class DAOAsyncTask extends AsyncTask<DataModel, Void, Void> {
    private DataModelDAO mDataModelDAO;
    private String mType;
    private String mName;
    private String mUnitPrice;
    private String mQuantity;

    public DAOAsyncTask(DataModelDAO dataModelDAO, String type, String name, String unitPrice, String quantity) {
        this.mDataModelDAO = dataModelDAO;
        this.mType = type;
        this.mName = name;
        this.mUnitPrice = unitPrice;
        this.mQuantity = quantity;
    }

    @Override
    protected Void doInBackground(DataModel... dataModels) {
        if (mType.equals(Key.INSERT)) {
            mDataModelDAO.insert(dataModels[0]);
        } else if (mType.equals(Key.UPDATE)) {
            if (mDataModelDAO.getData(mName) != null) {
                if (!mName.isEmpty() && !mUnitPrice.isEmpty() && !mQuantity.isEmpty() ) {
                    mDataModelDAO.dataAllUpdate(mName, mUnitPrice, mQuantity);
                }
//                else if (!mTitle.isEmpty() && mContent.isEmpty()) {
//                    mDataModelDAO.dataUpdate(mId, mTitle,"", mColor);
//                } else if (mTitle.isEmpty() && !mContent.isEmpty()) {
//                    mDataModelDAO.dataContentUpdate(mId, mContent, mColor);
//                }
            }
        } else if (mType.equals(Key.DELETE)) {
            if (mDataModelDAO.getData(mName) != null) {
                mDataModelDAO.delete(mDataModelDAO.getData(mName));
            }
        } else if (mType.equals(Key.CLEAR)) {
            mDataModelDAO.clearAll();
        } else if (mType.equals(Key.SELECT)){
            mDataModelDAO.getData(mName);
        }
        return null;
    }
}