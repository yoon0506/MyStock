package com.yoon.mystock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.yoon.mystock.databinding.FragmentCalculatorBinding;

import java.util.HashMap;

public class FragmentCalculator extends Fragment {

    private FragmentCalculator This = this;
    private FragmentCalculatorBinding mBinding;
    private DataModel mData;

    // '계산'버튼을 눌렀는지 여부 확인.
    // '계산'버튼을 누르면 '반영'버튼으로 변경.
    private boolean mIsCal = false;

    private Listener mListener = null;
    public void setListener(Listener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calculator, container, false);
        View mmRoot = mBinding.getRoot();
        return mmRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.resetBtn.setOnClickListener(v -> {
            if(mIsCal && mBinding.calBtn.getText().equals("반영")) {
                mIsCal = false;
                mBinding.calBtn.setText("계산");
            }else {
                resetAllEditTxt();
            }
        });

        mBinding.calBtn.setOnClickListener(v -> {
            if (mBinding.curUnitPrice.getText().toString().equals("")
                    || mBinding.curQuantity.getText().toString().equals("")
                    || mBinding.preUnitPrice.getText().toString().equals("")
                    || mBinding.preQuantity.getText().toString().equals("")) {
                Toast.makeText(getContext(), "빈 칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            int mmCurUnitPrice = Integer.parseInt(mBinding.curUnitPrice.getText().toString());
            int mmCurQuantity = Integer.parseInt(mBinding.curQuantity.getText().toString());
            int mmPreUnitPrice = Integer.parseInt(mBinding.preUnitPrice.getText().toString());
            int mmPreQuantity = Integer.parseInt(mBinding.preQuantity.getText().toString());


            int mmResult = ((mmCurUnitPrice * mmCurQuantity) + (mmPreUnitPrice * mmPreQuantity)) / (mmCurQuantity + mmPreQuantity);
            mBinding.resultUnitPrice.setText(mmResult + "");

            if(!mIsCal && mBinding.calBtn.getText().equals("계산")){
                mIsCal = true;
                mBinding.calBtn.setText("반영");
            }else if(mIsCal && mBinding.calBtn.getText().equals("반영")){
                updateCurInfo();
                mIsCal = false;
                mBinding.calBtn.setText("계산");
            }
        });

        mBinding.saveBtn.setOnClickListener(v -> {
            if (mBinding.curUnitPrice.getText().toString().equals("")
                    && mBinding.curQuantity.getText().toString().equals("")) {
                Toast.makeText(getContext(), "빈 칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mBinding.name.getText().toString().equals("")) {
                Toast.makeText(getContext(), "주식의 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            HashMap<String, String> mmData = new HashMap<>();
            mmData.put(Key.NAME, mBinding.name.getText().toString());
            mmData.put(Key.CURRENT_UNIT_PRICE, mBinding.curUnitPrice.getText().toString());
            mmData.put(Key.CURRENT_QUANTITY, mBinding.curQuantity.getText().toString());

            if (mListener != null) {
                if(!mIsCal) {
                    mListener.didRespond(Key.EVENT_SAVE_DATA, mmData);
                }else if(mIsCal && mBinding.calBtn.getText().equals("반영")){
                    mListener.didRespond(Key.EVENT_SAVE_DATA, mmData);
                }else{
                    mListener.didRespond(Key.EVENT_UPDATE_DATA, mmData);
                }
            }
            resetAllEditTxt();
        });

        if (AppData.GetInstance().mDataModel != null) {
            mData = AppData.GetInstance().mDataModel;
            setStockEditTxt(mData);
        }
    }

    public void setStockEditTxt(DataModel data) {
        resetAllEditTxt();
        mBinding.name.setText(data.getName());
        mBinding.curUnitPrice.setText(data.getCurUnitPrice());
        mBinding.curQuantity.setText(data.getCurQuantity());

        mBinding.preUnitPrice.requestFocus();
    }

    // '반영' 버튼을 누르면 추매단가, 추매수량이 각각 보유단가, 보유수량에 적용됨.
    private void updateCurInfo(){
        int mmUpdateQuantity = Integer.parseInt(mBinding.curQuantity.getText().toString())+Integer.parseInt(mBinding.preQuantity.getText().toString());
        mBinding.curUnitPrice.setText(mBinding.resultUnitPrice.getText());
        mBinding.curQuantity.setText(mmUpdateQuantity+"");
        mBinding.preUnitPrice.setText("");
        mBinding.preQuantity.setText("");
        mBinding.calBtn.setText("계산");

        mBinding.resultUnitPrice.setText("0");
        mIsCal = false;
        mBinding.preUnitPrice.requestFocus();
    }

    private void resetAllEditTxt() {
        mBinding.resultUnitPrice.setText("0");
        mBinding.name.setText("");
        mBinding.curUnitPrice.setText("");
        mBinding.curQuantity.setText("");
        mBinding.preUnitPrice.setText("");
        mBinding.preQuantity.setText("");
        mBinding.calBtn.setText("계산");
        mIsCal = false;
        mBinding.curUnitPrice.requestFocus();
    }

    public interface Listener {
        public void didRespond(String eventType, HashMap<String, String> data);
    }
}