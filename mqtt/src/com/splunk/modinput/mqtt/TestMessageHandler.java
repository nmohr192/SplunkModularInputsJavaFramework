package com.splunk.modinput.mqtt;

import com.splunk.modinput.SplunkLogEvent;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestMessageHandler extends AbstractMessageHandler{

    @Override
    public void handleMessage(String topic, MqttMessage message, MQTTModularInput.MessageReceiver context)
            throws Exception {

        String t = "12-21-17;22:20:42;07620000;01;0.0954568;0;0;0;0;-37;0;0;0.0744804;5.43273;0;0;0;91.492;0;0;V;";
        transportMessage(t);
    }

    public static List<String> chunkData(String text, int size) {

        List<String> ret = new ArrayList<String>((text.length() + size - 1)
                / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    @Override
    public void setParams(Map<String, String> params) {
        // Do nothing , params not used

    }
}
