# Android模拟器检测SDK

## 简要说明

通过识别模拟器设备指纹特征，判断是否为模拟器，以及具体模拟器的类型。

​             

## 使用说明

将simulatordetector.jar放入到项目libs目录下。

调用Java接口：

```java
int deviceType = SimulatorDetector.getDeviceType();
```

返回值为设备类型：

* 0：真机
* 1：腾讯手游助手
* 2：雷电
* 3：夜神
* 4：MuMu
* 5：海马玩
* 6：天天
* 7：蓝叠
* 8：其他模拟器
* 9：逍遥

​        

Unity调用示例：

```c#
#if UNITY_ANDROID
        using (var simulatorDetectorJavaClass = new AndroidJavaClass("com.github.sandin.simulator.detector.SimulatorDetector"))
        {
            if (simulatorDetectorJavaClass != null)
            {
                deviceType = simulatorDetectorJavaClass.CallStatic<int>("getDeviceType");
            }
        }
#endif
```

