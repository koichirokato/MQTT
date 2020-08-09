package sample;

import mqtt.MqttPublisher;
import mqtt.MqttThread;

public class TestMqttClient {
	public static void main(String[] args) {
		MqttThread mthread = new MqttThread("localhost","testTopic2");
		MqttPublisher publisher = new MqttPublisher("localhost","testTopic1");
		mthread.start();
				
		while(true) {
			if(mthread.isNew()) System.out.println(MqttThread.recieveData);
			
			publisher.publish("Hello");
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO ©“®¶¬‚³‚ê‚½ catch ƒuƒƒbƒN
				e.printStackTrace();
			}
		}
	}
}
