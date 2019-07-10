package com.demo.li.vc_room_out;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.demo.li.vc_room_out.library.ClientSettingHelper;
import com.demo.li.vc_room_out.library.SettingHepler;
import com.demo.li.vc_room_out.library.ui.BaseActivity;
import com.demo.li.vc_room_out.sockethelper.SocketVisitClient;


public class ClientSettingActivity extends BaseActivity {
    private EditText etServerIP, etStationIP, etName, etNumber;
    public static final int RESULT_SETTING_CHANGED = -2;
    private Spinner mSpinner;
    private ArrayAdapter mArrayAdapter;
    public String mOldStationIp = "";
    public String mOldName = "";
    public String mOldNumber = "";
    int videomode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_setting);


        etServerIP = (EditText) findViewById(R.id.setting_server_ip_et);
        etStationIP = (EditText) findViewById(R.id.setting_station_ip_et);
        etName = (EditText) findViewById(R.id.setting_name_et);
        etNumber = (EditText) findViewById(R.id.setting_number_et);
        findViewById(R.id.setting_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (validSetting()) {
                    saveSetting();
                    isUpdateStationIp();
                    SettingHepler.setVideoMode(videomode);
                    finish();
                }
            }
        });
        findViewById(R.id.setting_cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        readSetting();


        mSpinner = findViewById(R.id.setting_video_spinner);
        String[] spinnerItems = {"320*180 fps 10", "640*480 fps 16"};
        mArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_select, spinnerItems);
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(mArrayAdapter);
        int isSelect = SettingHepler.getVideoMode();
        videomode = isSelect;
        mSpinner.setSelection(isSelect);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                videomode = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void readSetting() {
        etServerIP.setText(ClientSettingHelper.getServerIp());
        etStationIP.setText(ClientSettingHelper.getStationIp());
        mOldStationIp = etStationIP.getText().toString().trim();
        etName.setText(ClientSettingHelper.getDeviceName());
        mOldName = ClientSettingHelper.getDeviceName();
        etNumber.setText(ClientSettingHelper.getDeviceNumber());
        mOldNumber = ClientSettingHelper.getDeviceNumber();
    }

    private void saveSetting() {
        ClientSettingHelper.setServerIp(etServerIP.getText().toString());
        ClientSettingHelper.setStationIp(etStationIP.getText().toString());
        ClientSettingHelper.setDeviceName(etName.getText().toString());
        ClientSettingHelper.setDeviceNumber(etNumber.getText().toString());
    }


    protected boolean validSetting() {
        String error = null;
        if (TextUtils.isEmpty(etServerIP.getText().toString())) {
            error = "服务器IP";
        } else if (TextUtils.isEmpty(etStationIP.getText().toString())) {
            error = "室内IP";
        } else if (TextUtils.isEmpty(etName.getText().toString())) {
            error = "设备名称";
        } else if (TextUtils.isEmpty(etNumber.getText().toString())) {
            error = "设备编号";
        }
        if (!TextUtils.isEmpty(error)) {
            showToast("请设置" + error, false);
            return false;
        }
        return true;
    }


    private void isUpdateStationIp() {
        if (!etStationIP.getText().toString().trim().equals(mOldStationIp) || !etName.getText().toString().trim().equals(mOldName) || !etNumber.getText().toString().trim().equals(mOldNumber)) {
            SocketVisitClient.getInstance().stop();
        }
    }

}
