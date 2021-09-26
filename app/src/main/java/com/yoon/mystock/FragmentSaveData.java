package com.yoon.mystock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yoon.mystock.databinding.FragmentSaveDataBinding;

public class FragmentSaveData extends Fragment {

    private FragmentSaveData This = this;
    private FragmentSaveDataBinding mBinding;
    private Adapter mAdapter;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_save_data, container, false);
        View mmRoot = mBinding.getRoot();
        return mmRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppData.GetInstance().mDB.dataModelDAO().getAll().observe(getActivity(), dataList -> {
            mAdapter.submitList(dataList);
            mAdapter.notifyDataSetChanged();
            AppData.GetInstance().mDataCnt = dataList.size();
        });

        mAdapter = new Adapter(getActivity());
        mAdapter.setListener(new Adapter.Listener() {
            @Override
            public void eventRemoveItem(DataModel dataModel) {
                if (dataModel != null) {
                    new DAOAsyncTask(
                            AppData.GetInstance().mDB.dataModelDAO(),
                            Key.DELETE,
                            dataModel.getName(),
                            "",
                            ""
                    ).execute();

                    if (mListener != null) {
                        mListener.didRespond(Key.EVENT_BACK, dataModel);
                    }
                }
            }

            @Override
            public void eventItemClick(DataModel dataModel) {
                if (dataModel != null) {
                    if (mListener != null) {
                        mListener.didRespond(Key.EVENT_CLICK_ITEM, dataModel);
                    }
                }
            }
        });
        mBinding.recycler.setHasFixedSize(true);
        mBinding.recycler.setItemAnimator(new DefaultItemAnimator());
        mBinding.recycler.setAdapter(mAdapter);
        mBinding.recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBinding.saveDataFrameLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.didRespond(Key.EVENT_BACK, null);
            }
        });
    }

    public interface Listener {
        public void didRespond(String event, DataModel data);
    }
}