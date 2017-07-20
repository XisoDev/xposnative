package com.example.macarrow.xPos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.macarrow.xPos.Services.CarType_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.fragment.Calcu_Fragment;
import com.example.macarrow.xPos.fragment.Config.Config_CarType_Add;
import com.example.macarrow.xPos.fragment.Cooper_Fragment;
import com.example.macarrow.xPos.fragment.Garage.CarTypeList;
import com.example.macarrow.xPos.fragment.History_Fragment;
import com.example.macarrow.xPos.fragment.Config_Fragment;
import com.example.macarrow.xPos.fragment.Current_Fragment;
import com.example.macarrow.xPos.fragment.Month.Month_Add;
import com.example.macarrow.xPos.fragment.Month_Fragment;
import com.example.macarrow.xPos.fragment.Payment.Payment_Discount;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class MainActivity extends Activity {

    private InputMethodManager imm;
    private Activity activity;
    private View decorView;
    private int uiOption;
    private TextView mainTitle;

    // 다이얼로그 닫기
    //private DialogInterface mPopupDig = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        UsbDevice udevice = (UsbDevice) getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        HashMap<String,UsbDevice> deviceList = manager.getDeviceList();
//        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
//        while(deviceIterator.hasNext()) {
//            UsbDevice device = deviceIterator.next();
//            Log.d("xPos", "deviceIterator : " + deviceIterator + ", device : " + device);
//        }

        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 전체화면
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOption);

        setContentView(R.layout.activity_main);
        activity = this;

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();

        final CarType_Services carTypeServices = new CarType_Services(this, "car_type.db", null, 1);
        final Garage_Service garageService  = new Garage_Service(this, "garage.db", null, 1);
        final Month_Service monthService = new Month_Service(this, "month.db", null, 1);

        final TextView mainField = (TextView) findViewById(R.id.mainField);
        mainTitle = (TextView) findViewById(R.id.main_title);
        final LinearLayout inCarChk = (LinearLayout) findViewById(R.id.inCarChk);
        final LinearLayout outCarChk = (LinearLayout) findViewById(R.id.outCarChk);
        final LinearLayout openMonthChk = (LinearLayout) findViewById(R.id.openMonthChk);
        final LinearLayout doCard = (LinearLayout) findViewById(R.id.do_card);
        final LinearLayout doPaycash = (LinearLayout) findViewById(R.id.do_paycash);
        final LinearLayout openCash = (LinearLayout) findViewById(R.id.open_cash);
        final TextView clearMF = (TextView) findViewById(R.id.clearMF);
        final TextView addMFdash = (TextView) findViewById(R.id.addMFdash);
        final TextView addMF1 = (TextView) findViewById(R.id.addMF1);
        final TextView addMF2 = (TextView) findViewById(R.id.addMF2);
        final TextView addMF3 = (TextView) findViewById(R.id.addMF3);
        final TextView addMF4 = (TextView) findViewById(R.id.addMF4);
        final TextView addMF5 = (TextView) findViewById(R.id.addMF5);
        final TextView addMF6 = (TextView) findViewById(R.id.addMF6);
        final TextView addMF7 = (TextView) findViewById(R.id.addMF7);
        final TextView addMF8 = (TextView) findViewById(R.id.addMF8);
        final TextView addMF9 = (TextView) findViewById(R.id.addMF9);
        final TextView addMF0 = (TextView) findViewById(R.id.addMF0);
        final LinearLayout tabConfig = (LinearLayout) findViewById(R.id.tab_config);
        final TextView tabConfigTxt = (TextView) findViewById(R.id.tab_config_txt);
        final LinearLayout tabCurrent = (LinearLayout) findViewById(R.id.tab_current);
        final TextView tabCurrentTxt = (TextView) findViewById(R.id.tab_current_txt);
        final LinearLayout tabHistory = (LinearLayout) findViewById(R.id.tab_history);
        final TextView tabHistoryTxt = (TextView) findViewById(R.id.tab_history_txt);
        final LinearLayout tabMonth = (LinearLayout) findViewById(R.id.tab_month);
        final TextView tabMonthTxt = (TextView) findViewById(R.id.tab_month_txt);
        final LinearLayout tabCooper = (LinearLayout) findViewById(R.id.tab_cooper);
        final TextView tabCooperTxt = (TextView) findViewById(R.id.tab_cooper_txt);
        final LinearLayout tabCalcu = (LinearLayout) findViewById(R.id.tab_calcu);
        final TextView tabCalcuTxt = (TextView) findViewById(R.id.tab_calcu_txt);

        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
        tabCurrentTxt.setTextColor(getResources().getColor(R.color.white));

        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
        tabConfigTxt.setTextColor(getResources().getColor(R.color.black));
        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
        tabHistoryTxt.setTextColor(getResources().getColor(R.color.black));
        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
        tabMonthTxt.setTextColor(getResources().getColor(R.color.black));
        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
        tabCooperTxt.setTextColor(getResources().getColor(R.color.black));
        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
        tabCalcuTxt.setTextColor(getResources().getColor(R.color.black));

        // 숫자패널
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mainField.getText().equals("관리번호")) {

                    mainField.setText("");
                    String text = (String) mainField.getText();
                    text += ((TextView) v).getText();
                    mainField.setText(text);

                } else {

                    String text = (String) mainField.getText();
                    text += ((TextView) v).getText();
                    mainField.setText(text);

                }
            }
        };

        // 숫자패널 다 지우기
        clearMF.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                mainField.setText("관리번호");
                return false;
            }
        });

        // 월차추가
        openMonthChk.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Bundle args = new Bundle();
                args.putString("status", "new");
                Month_Add month_add = new Month_Add();
                month_add.setArguments(args);
                month_add.setCancelable(false);
                month_add.show(getFragmentManager(), "month_add");
                return true;
            }
        });

        // Button case 처리
        View.OnClickListener clickListener = new View.OnClickListener() {

            FragmentManager fm = getFragmentManager();
            Calendar calendar = Calendar.getInstance();
            Calendar today = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            int toDay = Integer.parseInt(sdf.format(today.getTime()));

            @Override
            public void onClick(View v) {

                final String carNum = mainField.getText().toString();

                switch (v.getId()) {

                    case R.id.inCarChk:

                        if (carNum.equals("관리번호")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("관리번호를 입력해주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();

                        } else if (carTypeServices.car_type("N") < 1) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("차종등록을 해주세요");
                            adb.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Bundle args = new Bundle();
                                    args.putString("status", "new");
                                    args.putString("is_daycar", "N");
                                    Config_CarType_Add config_carType_add = new Config_CarType_Add();
                                    config_carType_add.setArguments(args);
                                    config_carType_add.setCancelable(false);
                                    config_carType_add.show(getFragmentManager(), "config_carType_add");
                                }
                            });
                            adb.show();

                        } else if (garageService.doubleCarNum(carNum) > 0) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("같은 관리번호가 입차되어 있습니다");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            mainField.setText("관리번호");

                        } else if (monthService.findCarNum(toDay, carNum) > 0) {

                            Map<String, Object> carNumMap = monthService.getMonthCarNum(toDay, carNum);
                            final String car_num = (String) carNumMap.get("car_num");
                            final String car_type_title = (String) carNumMap.get("car_type_title");
                            final int idx = (int) carNumMap.get("idx");

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("월차 확인");
                            adb.setNegativeButton("다른 번호 입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            adb.setPositiveButton(car_num+"차량을 월차로 입차 하시겠습니까?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (garageService.findMonthCarNum(car_num) > 0) {
                                        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                                        adb.setTitle("같은 관리번호가 입차되어 있습니다");
                                        adb.setNegativeButton("닫기", null);
                                        adb.show();
                                        mainField.setText("관리번호");
                                    } else {
                                        long start_date = System.currentTimeMillis();
                                        GregorianCalendar gcalendar = new GregorianCalendar();
                                        int year = gcalendar.get(Calendar.YEAR);
                                        int month = gcalendar.get(Calendar.MONTH)+1;
                                        int day = gcalendar.get(Calendar.DAY_OF_MONTH);
                                        garageService.insert(start_date, year, month, day, car_num, car_type_title, 0, 0, 0, 0, 0, idx, 0, 0, 0);
                                        mainField.setText("관리번호");
                                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                    }
                                }
                            });
                            adb.show();

                        } else {

                            Bundle args = new Bundle();
                            args.putString("car_num", carNum);
                            args.putString("status", "nomal");
                            CarTypeList carTypeList = new CarTypeList();
                            carTypeList.setArguments(args);
                            carTypeList.setCancelable(false);
                            carTypeList.show(getFragmentManager(), "nomal_car");

                            mainField.setText("관리번호");
                        }
                        break;

                    case R.id.outCarChk:

                        final Map<String, Object> map = garageService.getByIdx(carNum);

                        if (carNum.equals("관리번호")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("관리번호를 입력해주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();

                        } else if (garageService.findMonthCarNum(carNum) > 0) {

                            Map<String, Object> carNumMap = garageService.getByIdx(carNum);
                            String car_num = (String) carNumMap.get("car_num");

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("월차 출차");
                            adb.setNegativeButton("다른 번호 입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            adb.setPositiveButton(car_num+"월차 차량을 출차하시겠습니까?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String is_out = "Y";
                                    String is_paid = "";
                                    int total_amount = 0;
                                    long endDate = System.currentTimeMillis();
                                    if (total_amount > 0) {
                                        is_paid = "N";
                                    } else {
                                        is_paid = "Y";
                                    }
                                    garageService.updateForceOut(is_out, total_amount, endDate, is_paid, (int) map.get("idx"));
                                    fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                }
                            });
                            adb.show();
                            mainField.setText("관리번호");

                        } else if (garageService.doubleCarNum(carNum) != 1) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("없는 관리번호 입니다");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            mainField.setText("관리번호");

                        } else {

                            Bundle args = new Bundle();
                            args.putInt("idx", (int) map.get("idx"));
                            Payment_Discount payment_discount = new Payment_Discount();
                            payment_discount.setArguments(args);
                            payment_discount.setCancelable(false);
                            payment_discount.show(getFragmentManager(), "payment_discount");
                            mainField.setText("관리번호");
                        }
                        break;

                    case R.id.openMonthChk:

                        if (monthService.findCarNum(toDay, carNum) > 0) {

                            Bundle args = new Bundle();
                            args.putString("car_num", carNum);
                            args.putString("status", "month");
                            CarTypeList carTypeList = new CarTypeList();
                            carTypeList.setArguments(args);
                            carTypeList.setCancelable(false);
                            carTypeList.show(getFragmentManager(), "month_car");
                            mainField.setText("관리번호");

                        } else {

                            String car_num = "";
                            if (mainField.equals("관리번호")) {
                                car_num = "";
                            } else {
                                car_num = carNum;
                            }

                            Bundle args = new Bundle();
                            args.putString("status", "new");
                            args.putString("car_num", car_num);
                            Month_Add month_add = new Month_Add();
                            month_add.setArguments(args);
                            month_add.setCancelable(false);
                            month_add.show(getFragmentManager(), "month_add");
                            mainField.setText("관리번호");
                        }
                        break;

                    case R.id.clearMF:

                        if (mainField.getText().equals("관리번호")) {

                            mainField.setText("관리번호");

                        } else {

                            String text = (String) mainField.getText();
                            text = text.substring(0, mainField.length()-1);

                            if (text.length() < 1) {

                                mainField.setText("관리번호");

                            } else {

                                mainField.setText(text);

                            }
                        }
                        break;

                    case R.id.do_card:

                        break;

                    case R.id.do_paycash:

                        break;

                    case R.id.open_cash:

                        Intent intent = new Intent(activity, Xserial.class);
                        startActivity(intent);
                        break;

                    case R.id.tab_config:
                        fm.beginTransaction().replace(R.id.content_fragment, new Config_Fragment()).commit();
                        mainTitle.setText("설정");

                        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
                        tabConfigTxt.setTextColor(getResources().getColor(R.color.white));

                        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCurrentTxt.setTextColor(getResources().getColor(R.color.black));
                        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabHistoryTxt.setTextColor(getResources().getColor(R.color.black));
                        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabMonthTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCooperTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCalcuTxt.setTextColor(getResources().getColor(R.color.black));

                        break;

                    case R.id.tab_current:
                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                        mainTitle.setText("입차목록");

                        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
                        tabCurrentTxt.setTextColor(getResources().getColor(R.color.white));

                        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabConfigTxt.setTextColor(getResources().getColor(R.color.black));
                        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabHistoryTxt.setTextColor(getResources().getColor(R.color.black));
                        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabMonthTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCooperTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCalcuTxt.setTextColor(getResources().getColor(R.color.black));

                        break;

                    case R.id.tab_history:
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment("all")).commit();
                        mainTitle.setText("입출차기록");

                        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
                        tabHistoryTxt.setTextColor(getResources().getColor(R.color.white));

                        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCurrentTxt.setTextColor(getResources().getColor(R.color.black));
                        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabConfigTxt.setTextColor(getResources().getColor(R.color.black));
                        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabMonthTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCooperTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCalcuTxt.setTextColor(getResources().getColor(R.color.black));

                        break;

                    case R.id.tab_month:
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment("possibility")).commit();
                        mainTitle.setText("월차");

                        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
                        tabMonthTxt.setTextColor(getResources().getColor(R.color.white));

                        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCurrentTxt.setTextColor(getResources().getColor(R.color.black));
                        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabConfigTxt.setTextColor(getResources().getColor(R.color.black));
                        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabHistoryTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCooperTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCalcuTxt.setTextColor(getResources().getColor(R.color.black));

                        break;

                    case R.id.tab_cooper:
                        String cooper;
                        cooper = "cooper";
                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                        mainTitle.setText("지정주차");

                        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
                        tabCooperTxt.setTextColor(getResources().getColor(R.color.white));

                        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabMonthTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCurrentTxt.setTextColor(getResources().getColor(R.color.black));
                        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabConfigTxt.setTextColor(getResources().getColor(R.color.black));
                        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabHistoryTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCalcuTxt.setTextColor(getResources().getColor(R.color.black));

                        break;

                    case R.id.tab_calcu:
                        fm.beginTransaction().replace(R.id.content_fragment, new Calcu_Fragment()).commit();
                        mainTitle.setText("정산");

                        tabCalcu.setBackground(getResources().getDrawable(R.drawable.tabs_bg_select));
                        tabCalcuTxt.setTextColor(getResources().getColor(R.color.white));

                        tabCooper.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCooperTxt.setTextColor(getResources().getColor(R.color.black));
                        tabMonth.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabMonthTxt.setTextColor(getResources().getColor(R.color.black));
                        tabCurrent.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabCurrentTxt.setTextColor(getResources().getColor(R.color.black));
                        tabConfig.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabConfigTxt.setTextColor(getResources().getColor(R.color.black));
                        tabHistory.setBackground(getResources().getDrawable(R.drawable.tabs_bg));
                        tabHistoryTxt.setTextColor(getResources().getColor(R.color.black));

                        break;

                }
            }
        };

        inCarChk.setOnClickListener(clickListener);
        outCarChk.setOnClickListener(clickListener);
        openMonthChk.setOnClickListener(clickListener);
        doCard.setOnClickListener(clickListener);
        doPaycash.setOnClickListener(clickListener);
        openCash.setOnClickListener(clickListener);
        clearMF.setOnClickListener(clickListener);
        tabConfig.setOnClickListener(clickListener);
        tabCurrent.setOnClickListener(clickListener);
        tabHistory.setOnClickListener(clickListener);
        tabMonth.setOnClickListener(clickListener);
        tabCooper.setOnClickListener(clickListener);
        tabCalcu.setOnClickListener(clickListener);
        addMF1.setOnClickListener(onClickListener);
        addMF2.setOnClickListener(onClickListener);
        addMF3.setOnClickListener(onClickListener);
        addMF4.setOnClickListener(onClickListener);
        addMF5.setOnClickListener(onClickListener);
        addMF6.setOnClickListener(onClickListener);
        addMF7.setOnClickListener(onClickListener);
        addMF8.setOnClickListener(onClickListener);
        addMF9.setOnClickListener(onClickListener);
        addMF0.setOnClickListener(onClickListener);
        addMFdash.setOnClickListener(onClickListener);

    }

    // 화면 터치시 전체화면
    public void linearOnClick(View v) {
        imm.hideSoftInputFromWindow(mainTitle.getWindowToken(), 0);
        decorView.setSystemUiVisibility( uiOption );
    }
    // 전체화면
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if( hasFocus ) { decorView.setSystemUiVisibility( uiOption ); }
    }
    // 화면 회전시 프레그먼트 중복X
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}