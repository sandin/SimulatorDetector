package com.github.sandin.simulatordetector;

import android.os.Bundle;
import android.widget.TextView;

import com.github.sandin.simulator.detector.SimulatorDetector;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int deviceType = SimulatorDetector.getDeviceType();
        String deviceTypeStr = deviceType2Str(deviceType);
        ((TextView)findViewById(R.id.deviceType)).setText("模拟器检测结果: " + deviceTypeStr + "(deviceType=" + deviceType + ")");
    }

    private static String deviceType2Str(int deviceType) {
        switch (deviceType) {
            case SimulatorDetector.REAL_DEVICE:
                return "真机";
            case SimulatorDetector.SYZS_SIMULATOR:
                return "腾讯手游助手";
            case SimulatorDetector.LDMNQ_SIMULATOR:
                return "雷电";
            case SimulatorDetector.YESHEN_SIMULATOR:
                return "夜神";
            case SimulatorDetector.MUMU_SIMULATOR:
                return "MuMu";
            case SimulatorDetector.HAIMAWAN_SIMULATOR:
                return "海马玩";
            case SimulatorDetector.TIANTIAN_SIMULATOR:
                return "天天";
            case SimulatorDetector.BLUESTACKS_SIMULATOR:
                return "蓝叠";
            case SimulatorDetector.UNKNOWN_SIMULATOR:
                return "其他模拟器";
            default:
                return "未知";
        }
    }
}
