package mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttThread extends Thread{
	String broker = "";
	String topic  = "";
	static MqttSubscriber subscriber;
	public static String recieveData = "";
	
	//コンストラクタ
	public MqttThread(String brokerHostName,String subscribeTopic) {
		broker = brokerHostName;
		topic  = subscribeTopic;
		subscriber = new MqttSubscriber(broker, topic);
	}
	
	public void run() {
		try {
            subscriber.subscribe();
        } catch(MqttException me) {
            System.out.println("reason: "   + me.getReasonCode());
            System.out.println("message: "  + me.getMessage());
            System.out.println("localize: " + me.getLocalizedMessage());
            System.out.println("cause: "    + me.getCause());
            System.out.println("exception: "+ me);
        } catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}	
	}
	
	public boolean isNew() {
		boolean flag = false;
		flag = subscriber.isNew();
		return flag;
	}
}
