package com.yoon.mystock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.yoon.mystock.databinding.ItemBinding;
import com.yoon.mystock.library._Popup;

public class Adapter extends ListAdapter<DataModel, Adapter.ItemViewHolder> {

    private Context mContext;

    public Adapter(Context context) {
        super(DataModel.DIFF_CALLBACK);
        mContext = context;
    }

    private DataModel mDataModel;
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        mDataModel = getItem(position);
        if (mDataModel != null) {
            holder.onBind(mDataModel);
        }

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        // 마지막 아이템에 마진추가.
        if (position == mDataModel.getCount() - 1) {
            mParams.topMargin = 30;
            mParams.leftMargin = 30;
            mParams.rightMargin = 30;
            mParams.bottomMargin = 50;
        } else {
            mParams.topMargin = 30;
            mParams.leftMargin = 30;
            mParams.rightMargin = 30;
        }
        holder.itemView.setLayoutParams(mParams);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemBinding mmBinding;

        ItemViewHolder(View itemView) {
            super(itemView);
            mmBinding = DataBindingUtil.bind(itemView);

            // 클릭이벤트
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    mListener.eventItemClick(getItem(position));
                }
            });

            // 롱클릭 이벤트
            itemView.setOnLongClickListener(v -> {
                if (mListener != null) {
                    _Popup.GetInstance().ShowBinaryPopup(mContext, "\"" + mDataModel.getName() + "\"" + " 제거하시겠습니까?", "확인", "취소", new _Popup.BinaryPopupListener() {
                        @Override
                        public void didSelectBinaryPopup(String mainMessage, String selectMessage) {
                            if (selectMessage.equals("확인")) {
                                int position = getAdapterPosition();
                                mListener.eventRemoveItem(getItem(position));
                            }
                        }
                    });
                }
                return false;
            });

            if (itemView != null) {
                if (itemView.getTag() != null && itemView.getTag().equals("ADD")) {
                }
            }
        }

        void onBind(DataModel dataModel) {
            mmBinding.cardName.setText(dataModel.getName());
        }
    }


    public interface Listener {
        public void eventItemClick(DataModel dataModel);

        public void eventRemoveItem(DataModel dataModel);
    }
}
