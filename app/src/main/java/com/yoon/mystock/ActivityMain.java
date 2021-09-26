package com.yoon.mystock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.yoon.mystock.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ActivityMain extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private ActivityMain This = this;
    private ActivityMainBinding mBinding;

    private ArrayList<Fragment> mFragmentStack = new ArrayList<>();
    private FragmentMenu mFragmentMenu;
    private FragmentCalculator mFragmentCalculator;
    private FragmentSaveData mFragmentSaveData;

    private GestureDetector mGestureDetector;
    static final int SWIPE_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상단바
        Window mmWindow = getWindow();
        mmWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        mmWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mmWindow.setStatusBarColor(getResources().getColor(R.color.lightYellow));

        mBinding = DataBindingUtil.setContentView(This, R.layout.activity_main);
        AppData.GetInstance().mDB = Room.databaseBuilder(This, LocalDatabase.class, "test-0728")
                .build();
        showFragmentCalculator();
        mGestureDetector = new GestureDetector(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void showFragmentMenu() {
        try {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.white));

            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentMenu = new FragmentMenu();
            mFragmentMenu.setListener((eventType, key) -> {
                if (eventType.equals(Key.EVENT_CLICK_BUTTON) && key != null) {
                    switch (key) {
                        case Key.CALCULATOR:
                            boolean mmShowCal = true;
                            for (Fragment fragmentName : mFragmentStack) {
                                if (fragmentName.toString().contains(Key.FRAGMENT_CALCULATOR)) {
                                    Toast.makeText(This, "이미 열려있음", Toast.LENGTH_SHORT).show();
                                    mmShowCal = false;
                                }
                            }

                            // 상단바
                            Window mmWindow = getWindow();
                            mmWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            mmWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            mmWindow.setStatusBarColor(getResources().getColor(R.color.lightYellow));

                            removeFragment(mFragmentMenu, Key.LEFT);
                            if (mmShowCal) {
                                showFragmentCalculator();
                            }
                            break;
                        case Key.CURRENT_STOCK:
                            Toast.makeText(This, "current stock", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }else if(eventType.equals(Key.EVENT_BACK) && key == null){
                    // 상단바
                    Window mmWindow = getWindow();
                    mmWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    mmWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    mmWindow.setStatusBarColor(getResources().getColor(R.color.lightYellow));

                    removeFragment(mFragmentMenu, Key.LEFT);
                }
            });
            mBinding.mainFullFrame.setVisibility(View.VISIBLE);
            mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_in_left);
            mFragmentTransaction.add(R.id.main_full_frame, mFragmentMenu);
            mFragmentTransaction.commit();
            mFragmentStack.add(mFragmentMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragmentCalculator() {
        try {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentCalculator = new FragmentCalculator();
            mFragmentCalculator.setListener((eventType, data) -> {
                if (eventType != null && data != null && data.size() > 0) {
                    switch (eventType) {
                        case Key.EVENT_SAVE_DATA:
                            new DAOAsyncTask(AppData.GetInstance().mDB.dataModelDAO(), Key.INSERT, data.get(Key.NAME), "", "")
                                    .execute(new DataModel(data.get(Key.NAME), data.get(Key.CURRENT_UNIT_PRICE), data.get(Key.CURRENT_QUANTITY)));
                            hideKeyboard(This);
                            break;
                        case Key.EVENT_UPDATE_DATA:
                            new DAOAsyncTask(AppData.GetInstance().mDB.dataModelDAO(), Key.UPDATE, data.get(Key.NAME), "", "")
                                    .execute(new DataModel(data.get(Key.NAME), data.get(Key.CURRENT_UNIT_PRICE), data.get(Key.CURRENT_QUANTITY)));
                            hideKeyboard(This);
                            break;
                    }
                }
            });
            mBinding.mainFullFrame.setVisibility(View.VISIBLE);
            mFragmentTransaction.replace(R.id.main_full_frame, mFragmentCalculator);
            mFragmentTransaction.commit();
            mFragmentStack.add(mFragmentCalculator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFragmentSaveData() {
        try {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentSaveData = new FragmentSaveData();
            mFragmentSaveData.setListener((eventType, data) -> {
                if (eventType != null) {
                    switch (eventType) {
                        case Key.EVENT_BACK:
                            removeFragment(mFragmentSaveData, Key.UP);
                            break;
                        case Key.EVENT_CLICK_ITEM:
                            if(data != null && data.getCount() > 0) {
                                mFragmentCalculator.setStockEditTxt(data);
                                removeFragment(mFragmentSaveData, Key.UP);
                            }
                            break;

                    }
                }
            });
            mBinding.mainFullFrame.setVisibility(View.VISIBLE);
            mFragmentTransaction.add(R.id.main_full_frame, mFragmentSaveData);
            mFragmentTransaction.commit();
            mFragmentStack.add(mFragmentSaveData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long mBackKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (mFragmentStack.size() > 0 && mFragmentStack.get(mFragmentStack.size() - 1).toString().contains(Key.FRAGMENT_MENU)) {
            removeFragment(mFragmentMenu, Key.LEFT);
            Timber.tag("checkCheck").d("return");
            return;
        } else if (mFragmentStack.size() > 0 && mFragmentStack.get(mFragmentStack.size() - 1).toString().contains(Key.FRAGMENT_SAVE_DATA)) {
            removeFragment(mFragmentSaveData, Key.UP);
            Timber.tag("checkCheck").d("return");
            return;
        }

        // 두 번 눌러 종료.
        if (System.currentTimeMillis() <= mBackKeyPressedTime + 3000) {
            exitApp();
        }
        if (System.currentTimeMillis() > mBackKeyPressedTime + 3000) {
            mBackKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(This, "'뒤로가기' 버튼을 한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFragment(Fragment fragment, String direction) {
        if (fragment != null) {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

            // fragment 제거 방향
            switch (direction) {
                case Key.UP:
                    mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_out_up, R.anim.anim_slide_out_up);
                    break;
                case Key.LEFT:
                    mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_out_left, R.anim.anim_slide_out_left);
                    break;
                default:
                    break;
            }
            mFragmentTransaction.remove(fragment);
            mFragmentTransaction.commit();
            fragment.onDestroy();
            fragment.onDetach();
            fragment = null;
            mFragmentStack.remove(mFragmentStack.size() - 1);
        }
    }

    private void exitApp() {
        Intent mmIntent = new Intent(This, ActivityLanding.class);
        mmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mmIntent.putExtra(Key.EVENT_APP_EXIT, true);
        startActivity(mmIntent);
    }

    // 키보드 숨기기.
    private void hideKeyboard(Activity activity) {
        if (This.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    // for fragment slide.
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;

        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        //which was grater? movement across  Y or X?
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // right or left swipe
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_THRESHOLD) {
                if (diffX > 0) {    // to right
                    try {
                        if (!mFragmentStack.get(mFragmentStack.size() - 1).toString().contains(Key.FRAGMENT_MENU)) {
                            showFragmentMenu();
                        }
                    } catch (Exception e) {
                        Timber.tag("checkCheck").d("error : %s", e);
                    }
                } else {    // to left
//                    Window mmWindow = getWindow();
//                    mmWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    mmWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    mmWindow.setStatusBarColor(getResources().getColor(R.color.lightGreen));
//
//                    removeFragment(mFragmentMenu, Key.LEFT);
                }
                hideKeyboard(This);
            }
        } else {
            // up or down swipe
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_THRESHOLD) {
                if (diffY > 0) {   // to bottom
//                    removeFragment(mFragmentSaveData, Key.UP);
                } else {  // to top
                    try {
                        if (!mFragmentStack.get(mFragmentStack.size() - 1).toString().contains(Key.FRAGMENT_SAVE_DATA)) {
                            showFragmentSaveData();
                        }
                    } catch (Exception e) {
                        Timber.tag("checkCheck").d("error : %s", e);
                    }
                }
                hideKeyboard(This);
            }
        }
        return result;
    }
}