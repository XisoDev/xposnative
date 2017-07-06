package com.example.macarrow.xPos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.macarrow.xPos.Services.CarType_Services;
import com.example.macarrow.xPos.Services.Garage_Service;
import com.example.macarrow.xPos.Services.Month_Service;
import com.example.macarrow.xPos.adapter.PanelCarTypeViewAdapter;
import com.example.macarrow.xPos.adapter.PanelMonthTypeViewAdapter;
import com.example.macarrow.xPos.fragment.Calcu_Fragment;
import com.example.macarrow.xPos.fragment.Cooper_Fragment;
import com.example.macarrow.xPos.fragment.History_Fragment;
import com.example.macarrow.xPos.fragment.Config_Fragment;
import com.example.macarrow.xPos.fragment.Current_Fragment;
import com.example.macarrow.xPos.fragment.Month.Month_Add;
import com.example.macarrow.xPos.fragment.Month.Month_Carlist;
import com.example.macarrow.xPos.fragment.Month_Fragment;
import com.example.macarrow.xPos.fragment.Payment.Payment_Discount;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

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
        final LinearLayout tabCurrent = (LinearLayout) findViewById(R.id.tab_current);
        final LinearLayout tabHistory = (LinearLayout) findViewById(R.id.tab_history);
        final LinearLayout tabMonth = (LinearLayout) findViewById(R.id.tab_month);
        final LinearLayout tabCooper = (LinearLayout) findViewById(R.id.tab_cooper);
        final LinearLayout tabCalcu = (LinearLayout) findViewById(R.id.tab_calcu);

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

                                    AlertDialog.Builder adb = new AlertDialog.Builder(activity);

                                    View view = activity.getLayoutInflater().inflate(R.layout.config_addcartype, null);
                                    final TextView titleCarType = (TextView)view.findViewById(R.id.title_car_type);
                                    final Button insertCartype = (Button)view.findViewById(R.id.insert_cartype);
                                    final Button closeCartype = (Button)view.findViewById(R.id.close_cartype);
                                    final EditText Car_type_title = (EditText)view.findViewById(R.id.car_type_title);
                                    final EditText Minute_unit = (EditText)view.findViewById(R.id.minute_unit);
                                    final EditText Minute_free = (EditText)view.findViewById(R.id.minute_free);
                                    final EditText Amount_unit = (EditText)view.findViewById(R.id.amount_unit);
                                    final EditText Basic_amount = (EditText)view.findViewById(R.id.basic_amount);
                                    final EditText Basic_minute = (EditText)view.findViewById(R.id.basic_minute);

                                    adb.setView(view);
                                    // 다이얼 로그 크기 조정
                                    final Dialog cts = adb.create();
                                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                                    params.copyFrom(cts.getWindow().getAttributes());
                                    params.width = 845;
                                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    cts.show();
                                    Window window = cts.getWindow();
                                    window.setAttributes(params);
                                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                    titleCarType.setText("차종 추가");
                                    insertCartype.setVisibility(View.VISIBLE);

                                    View.OnClickListener clickListener = new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switch (v.getId()) {

                                                case R.id.insert_cartype :

                                                    try {

                                                        if (Car_type_title.getText().toString().equals("")) {
                                                            Toast.makeText(v.getContext(), "차종명을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                            return;

                                                        } else if (Minute_free.getText().toString().equals("")) {
                                                            Toast.makeText(v.getContext(), "최초무료시간을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                            return;

                                                        } else if (Basic_amount.getText().toString().equals("")) {
                                                            Toast.makeText(v.getContext(), "기본요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                            return;

                                                        } else if (Basic_minute.getText().toString().equals("")) {
                                                            Toast.makeText(v.getContext(), "기본시간을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                            return;

                                                        } else if (Amount_unit.getText().toString().equals("")) {
                                                            Toast.makeText(v.getContext(), "추가요금을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                            return;

                                                        } else if (Minute_unit.getText().toString().equals("")) {
                                                            Toast.makeText(v.getContext(), "추가요금 단위(분)을 입력하지 않았습니다", Toast.LENGTH_SHORT).show();
                                                            return;

                                                        }

                                                    } catch (Exception e) {

                                                        e.printStackTrace();

                                                    }

                                                    String car_type_title = Car_type_title.getText().toString();
                                                    int minute_free = Integer.parseInt(Minute_free.getText().toString());
                                                    int basic_amount = Integer.parseInt(Basic_amount.getText().toString());
                                                    int basic_minute = Integer.parseInt(Basic_minute.getText().toString());
                                                    int amount_unit = Integer.parseInt(Amount_unit.getText().toString());
                                                    int minute_unit = Integer.parseInt(Minute_unit.getText().toString());
                                                    carTypeServices.insert(car_type_title, minute_free, basic_amount, basic_minute, amount_unit, minute_unit, "N");
                                                    cts.dismiss();
                                                    break;

                                                case R.id.close_cartype :

                                                    cts.dismiss();
                                                    break;
                                            }
                                        }
                                    };
                                    insertCartype.setOnClickListener(clickListener);
                                    closeCartype.setOnClickListener(clickListener);
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

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("월차 확인");
                            adb.setNegativeButton("다른 번호 입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            adb.setPositiveButton(carNum+"차량을 월차로 입차 하시겠습니까?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle args = new Bundle();
                                    args.putString("car_num", carNum);
                                    Month_Carlist month_car = new Month_Carlist();
                                    month_car.setArguments(args);
                                    month_car.setCancelable(false);
                                    month_car.show(getFragmentManager(), "month_car");
                                }
                            });
                            adb.show();
                            mainField.setText("관리번호");

                        } else {

                            View view = activity.getLayoutInflater().inflate(R.layout.panel_cartypelist, null);
                            GridView gridView = (GridView)view.findViewById(R.id.panel_cartypeList);
                            final List<Map<String, Object>> list = carTypeServices.getResult("N");
                            PanelCarTypeViewAdapter adapter = new PanelCarTypeViewAdapter(activity, R.layout.panel_cartypelist_item, list);
                            gridView.setAdapter(adapter);

                            AlertDialog.Builder cartypelistViewDialog = new AlertDialog.Builder(activity);
                            cartypelistViewDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mainField.setText("관리번호");
                                }
                            });
                            cartypelistViewDialog.setView(view);

                            // 다이얼 로그 크기 조정
                            final Dialog dialog = cartypelistViewDialog.create();
                            LayoutParams params = new LayoutParams();
                            params.copyFrom(dialog.getWindow().getAttributes());
                            params.width = 845;
                            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setAttributes(params);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    int idx = (int) list.get(position).get("idx");
                                    long start_date = System.currentTimeMillis();
                                    String car_num = mainField.getText().toString();

                                    Map<String, Object> map = carTypeServices.getResultForUpdate(idx);
                                    String car_type_title = (String) map.get("car_type_title");
                                    int minute_free = (Integer) map.get(("minute_free") + "");
                                    int basic_amount = (Integer) map.get(("basic_amount") + "");
                                    int basic_minute = (Integer) map.get(("basic_minute") + "");
                                    int amount_unit = (Integer) map.get(("amount_unit") + "");
                                    int minute_unit = (Integer) map.get(("minute_unit") + "");

                                    garageService.insert(
                                            start_date,
                                            car_num,
                                            car_type_title,
                                            minute_unit,
                                            minute_free,
                                            amount_unit,
                                            basic_amount,
                                            basic_minute,
                                            0,
                                            0,
                                            0,
                                            0);

                                    fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                    mainField.setText("관리번호");
                                    dialog.dismiss();

                                }
                            });
                        }

                        break;

                    case R.id.outCarChk:

                        if (carNum.equals("관리번호")) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("관리번호를 입력해주세요");
                            adb.setNegativeButton("닫기", null);
                            adb.show();

                        } else if (garageService.doubleCarNum(carNum) != 1) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("없는 관리번호 입니다");
                            adb.setNegativeButton("닫기", null);
                            adb.show();
                            mainField.setText("관리번호");

                        } else {

                            String car_num = mainField.getText().toString();
                            Bundle args = new Bundle();
                            args.putString("car_num", car_num);
                            Payment_Discount payment_discount = new Payment_Discount();
                            payment_discount.setArguments(args);
                            payment_discount.setCancelable(false);
                            payment_discount.show(getFragmentManager(), "payment_discount");

                            mainField.setText("관리번호");

                        }
                        break;

                    case R.id.openMonthChk:

                        if (carNum.equals("관리번호")) {

                            View view = activity.getLayoutInflater().inflate(R.layout.panel_cartypelist, null);
                            GridView gridView = (GridView)view.findViewById(R.id.panel_cartypeList);
                            final List<Map<String, Object>> list = monthService.getByCarNum(toDay, "");
                            TextView Title = (TextView)view.findViewById(R.id.title);
                            PanelMonthTypeViewAdapter adapter = new PanelMonthTypeViewAdapter(activity, R.layout.panel_monthlist_item, list);
                            gridView.setAdapter(adapter);

                            // 타이틀
                            Title.setText("월차 선택");

                            AlertDialog.Builder openMonthDialog = new AlertDialog.Builder(activity);
                            openMonthDialog.setNegativeButton("닫기",null);
                            openMonthDialog.setView(view);

                            // 다이얼 로그 크기 조정
                            final Dialog dialog = openMonthDialog.create();
                            LayoutParams params = new LayoutParams();
                            params.copyFrom(dialog.getWindow().getAttributes());
                            params.width = 845;
                            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            dialog.show();
                            Window window = dialog.getWindow();
                            window.setAttributes(params);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                FragmentManager fm = getFragmentManager();
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    int idx = (int) list.get(position).get("idx");
                                    String car_num = (String) list.get(position).get("car_num");
                                    long start_date = System.currentTimeMillis();
                                    String car_type_title = (String) list.get(position).get("car_type_title");

                                    if (garageService.doubleCarNum(car_num) > 0) {
                                        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                                        adb.setTitle("입차된 월차입니다");
                                        adb.setNegativeButton("닫기",null);
                                        adb.show();
                                        return;
                                    } else {
                                        garageService.insert(start_date, car_num, car_type_title, 0, 0, 0, 0, 0, idx, 0, 0, 0);
                                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else if (monthService.findCarNum(toDay, carNum) > 0) {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("월차 확인");
                            adb.setNegativeButton("다른 번호 입력", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            adb.setPositiveButton(carNum+"차량을 월차로 입차 하시겠습니까?", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Bundle args = new Bundle();
                                    args.putString("car_num", carNum);
                                    Month_Carlist month_car = new Month_Carlist();
                                    month_car.setArguments(args);
                                    month_car.setCancelable(false);
                                    month_car.show(getFragmentManager(), "month_car");
                                }
                            });
                            adb.show();
                            mainField.setText("관리번호");
                        } else {

                            AlertDialog.Builder adb = new AlertDialog.Builder(activity);
                            adb.setTitle("없는 월차번호입니다");
                            adb.setNegativeButton("다른 번호 입력", null);
                            adb.show();
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
                        break;

                    case R.id.tab_current:
                        fm.beginTransaction().replace(R.id.content_fragment, new Current_Fragment()).commit();
                        mainTitle.setText("입차목록");
                        break;

                    case R.id.tab_history:
                        fm.beginTransaction().replace(R.id.content_fragment, new History_Fragment("all")).commit();
                        mainTitle.setText("입출차기록");
                        break;

                    case R.id.tab_month:
                        fm.beginTransaction().replace(R.id.content_fragment, new Month_Fragment("possibility")).commit();
                        mainTitle.setText("월차");
                        break;

                    case R.id.tab_cooper:
                        String cooper;
                        cooper = "cooper";
                        fm.beginTransaction().replace(R.id.content_fragment, new Cooper_Fragment(cooper)).commit();
                        mainTitle.setText("지정주차");
                        break;

                    case R.id.tab_calcu:
                        fm.beginTransaction().replace(R.id.content_fragment, new Calcu_Fragment()).commit();
                        mainTitle.setText("정산");
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