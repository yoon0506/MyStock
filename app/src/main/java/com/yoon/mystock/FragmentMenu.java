package com.yoon.mystock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.yoon.mystock.databinding.FragmentMenuBinding;

public class FragmentMenu extends Fragment {

    private FragmentMenu This = this;
    private FragmentMenuBinding mBinding;

    private Listener mListener = null;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        View mmRoot = mBinding.getRoot();
        return mmRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.fragmentMenuLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.didRespond(Key.EVENT_BACK, null);
            }
        });

        mBinding.calculator.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.didRespond(Key.EVENT_CLICK_BUTTON, Key.CALCULATOR);
            }
        });

        mBinding.currentStock.setOnClickListener(v -> {
            if(mListener != null){
                mListener.didRespond(Key.EVENT_CLICK_BUTTON, Key.CURRENT_STOCK);
            }
        });
    }

    public interface Listener {
        public void didRespond(String eventType, String key);
    }
}