package com.splunk.modinput.mqtt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttMessage;


import com.splunk.modinput.SplunkLogEvent;

import com.splunk.modinput.mqtt.MQTTModularInput.MessageReceiver;

import org.json.*;

public class JSONMessageHandler extends AbstractMessageHandler {

	@Override
	public void handleMessage(String topic, MqttMessage message,MessageReceiver context)
			throws Exception {

		SplunkLogEvent splunkEvent = buildCommonEventMessagePart(context);

		splunkEvent.addPair("topic", topic);
        
        
		String messageBody = getMessageBody(message);
        if(isJSONValid(messageBody)){
            JSONObject jsonMessage = new JSONObject(messageBody);
		
		    Iterator<String> keys = jsonMessage.keys();
		    while(keys.hasNext()) {
    		    String key = keys.next();
			    String value = jsonMessage.get(key).toString();
			
			    splunkEvent.addPair(key, value);
		    }
        } 
        else 
        {
            splunkEvent.addPair("msg", messageBody);
        }
		
		String text = splunkEvent.toString();
		transportMessage(text,String.valueOf(System.currentTimeMillis()),"");

	}

	public static List<String> chunkData(String text, int size) {

		List<String> ret = new ArrayList<String>((text.length() + size - 1)
				/ size);

		for (int start = 0; start < text.length(); start += size) {
			ret.add(text.substring(start, Math.min(text.length(), start + size)));
		}
		return ret;
    }
    
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

	@Override
	public void setParams(Map<String, String> params) {
		// Do nothing , params not used

	}

}
