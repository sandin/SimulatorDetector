package com.github.sandin.simulator.detector;


import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simulator Detector
 */
public class SimulatorDetector {

    public static final int REAL_DEVICE          = 0; // 真机
    public static final int SYZS_SIMULATOR       = 1; // 腾讯手游助手
    public static final int LDMNQ_SIMULATOR      = 2; // 雷电
    public static final int YESHEN_SIMULATOR     = 3; // 夜神
    public static final int MUMU_SIMULATOR       = 4; // MuMu
    public static final int HAIMAWAN_SIMULATOR   = 5; // 海马玩
    public static final int TIANTIAN_SIMULATOR   = 6; // 天天
    public static final int BLUESTACKS_SIMULATOR = 7; // 蓝叠
    public static final int UNKNOWN_SIMULATOR    = 8; // 其他模拟器
    public static final int XYAZ_SIMULATOR       = 9; // 逍遥

    /**
     * Get Device Type
     *
     * @return device type
     */
    public static int getDeviceType() {
        int result = REAL_DEVICE;
        if (isFileExists("/dev/virtpipe-common")
                && isFileExists("/dev/virtpipe-render")
                && isFileExists("/data/data/com.tencent.tinput")
                && (isFileContainsContent("/proc/version", "Linux version 3.10.0-tencent (ychuang@ubuntu)")
                 || isFileContainsContent("/proc/version", "Linux version 4.4.163+ (mqq@10-51-151-156)")
                 || isFileContainsContent("/proc/version", "Linux version 3.10.0-tencent (mqq@10-51-151-156)")
                 || isFileContainsContent("/proc/version", "Linux version 4.4.163-tencent (cibuilder@VM_0_97_centos)"))) {
            result = SYZS_SIMULATOR; // 腾讯手游助手
        }
        if (isFileExists("/mnt/shared/TxGameAssistant")
                && isFileExists("/system/lib/vboxpcism.ko")
                && isFileExists("/system/priv-app/ZKOP/ZKOP.apk")
                && isFileExists("/system/priv-app/TiantianIME/TiantianIME.apk")
                && isFileExists("/system/etc/init.tiantian.sh")
                && isFileExists("/init.ttVM_x86.rc")
                && isFileExists("/etc/init.tiantian.sh")) {
            result = SYZS_SIMULATOR; // 腾讯手游助手
        }

        if (result == SYZS_SIMULATOR) {
            // 容错，特判
            if (isFileExists("/data/./data/com.bignox.app.store.hd") && isFileExists("/data/./data/com.bignox.google.installer")) {
                return YESHEN_SIMULATOR;
            } else if (isFileExists("/system/bin/nemuVM-nemu-control") && isFileExists("/system/bin/nemuVM-prop") && isFileExists("/system/bin/nemuVM-nemu-sf") && isFileExists("/system/bin/nemuVM-nemu-service")) {
                return MUMU_SIMULATOR;
            }
            return SYZS_SIMULATOR;
        }

        if (isFileExists("/system/bin/ldinit")
                || isFileExists("/system/app/ldAppStore")
                || isFileExists("/system/priv-app/ldAppStore/ldAppStore.apk")) {
            return LDMNQ_SIMULATOR; // 雷电
        }
        if (isFileExists("/data/./data/com.bignox.app.store.hd")
                && isFileExists("/data/./data/com.bignox.google.installer")) {
            return YESHEN_SIMULATOR; // 夜神
        }

        if (isFileExists("/system/bin/nemuVM-nemu-control")
                && isFileExists("/system/bin/nemuVM-prop")
                && isFileExists("/system/bin/nemuVM-nemu-sf")
                && isFileExists("/system/bin/nemuVM-nemu-service")) {
            return MUMU_SIMULATOR; //  MuMu
        }

        if (isFileExists("/system/bin/droid4x")
                && isFileExists("/system/bin/droid4x-prop")
                && isFileExists("/system/bin/droid4x-vbox-sf")
                && isFileExists("/system/bin/droid4x_setprop")) {
            return HAIMAWAN_SIMULATOR; // 海马玩
        }

        if (isFileExists("/ueventd.ttVM_x86.rc")
                && isFileExists("/init.ttVM_x86.rc")
                && isFileExists("/fstab.ttVM_x86")
                && isFileExists("/system/bin/ttVM-prop")
                && isFileExists("/system/bin/ttVM-vbox-sf")
                && isFileExists("/system/bin/ttVM_setprop")
                && isFileExists("/dev/ttipc_ime")) {
            return TIANTIAN_SIMULATOR; // 天天
        }

        if (/*(Build.CPU_ABI.contains("x86") || Build.CPU_ABI2.contains("x86"))
                &&*/ (
                isFileExists("/data/data/com.bluestacks.settings")
                        || isFileExists("/data/user/0/com.bluestacks.settings/")
                        || isFileExists("/sdcard/DCIM/SharedFolder/bs_avatar.png")
                        || isFileExists("/dev/bst_gps")
                        || isFileExists("/dev/bst_ime")
        )) {
            return BLUESTACKS_SIMULATOR; // 蓝叠
        }

        if (isFileExists("/data/data/com.microvirt.installer")
            || isFileExists("/sdcard/Android/data/com.microvirt.launcher2")
            || isFileExists("/sdcard/Android/data/com.microvirt.guide")) {
            return XYAZ_SIMULATOR; // 逍遥
        }

        if (isFileExists("/dev/vboxguest")
                || isFileExists("/dev/vboxuser")
                || isFileExists("/system/lib/vboxguest.ko")
                || isFileExists("/system/lib/vboxsf.ko")
                || isFileExists("/sys/class/misc/tboxguest")
                || isFileExists("/sys/class/misc/tboxuser")
                || isFileExists("/init.x86.rc")
                || isFileExists("/ueventd.android_x86.rc")
                || isFileContainsContent("/proc/self/maps", "lib_renderControl_enc.so")) {
            return UNKNOWN_SIMULATOR; // 其他模拟器
        }

        return REAL_DEVICE; // 真机
    }

    private static boolean isFileExists(String filename) {
        try {
            return new File(filename).exists();
        } catch (Throwable ignore) {
        }
        return false;
    }

    private static boolean isFileContainsContent(String filename, String content) {
        boolean found = false;
        BufferedReader reader = null;
        try {
            File file = new File(filename);
            if (file.exists() && !file.isDirectory() && file.canRead()) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line = reader.readLine();
                while (line != null) {
                    if (line.contains(content)) {
                        found = true;
                        break;
                    }
                    line = reader.readLine();
                }
            }
        } catch (Throwable ignore) {
            // ignore
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                    // ignore
                }
            }
        }
        return found;
    }

}
