package mqtt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttSubscriber  implements MqttCallback {
	Timestamp recieveTime;
	Timestamp lastTime;
	String broker = "";
	String topic  = "";
	
	//コンストラクタ
	public MqttSubscriber(String brokerHostName,String subscribeTopic) {
		broker = "tcp://"+brokerHostName+":1883";
		topic  = subscribeTopic;
	}
	
    /**
     * MQTTブローカーとの接続を失った時に呼び出される.
     */
    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost!");
        //再接続がしたかったらここに処理を書く
        System.exit(1);
    }

    /**
     * メッセージの送信が完了したときに呼ばれるCallback.
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //Subscribe側からは呼び出されない？
    }

    /**
     * メッセージを受信したときに呼ばれる。
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
//        System.out.println("Message arrived");
//        System.out.println("Topic:"+ topic);
//        System.out.println("Message: " + new String(message.getPayload()));
        
        recieveTime = new Timestamp(System.currentTimeMillis());
        MqttThread.recieveData = new String(message.getPayload());
    }
    
    public boolean isNew() {
    	boolean flag = false;
    	if(recieveTime==lastTime) flag = false;
    	else flag = true;
    	lastTime=recieveTime;
    	return flag;
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            MqttSubscriber subscriber = new MqttSubscriber("localhost","testTopic1");
            subscriber.subscribe();
        } catch(MqttException me) {
            System.out.println("reason: "   + me.getReasonCode());
            System.out.println("message: "  + me.getMessage());
            System.out.println("localize: " + me.getLocalizedMessage());
            System.out.println("cause: "    + me.getCause());
            System.out.println("exception: "+ me);
        }
    }

    /**
     * メッセージを受信する.
     * 標準入力があるまで接続し続ける.
     * 
     * @throws MqttException
     * @throws InterruptedException 
     */
    public void subscribe() throws MqttException, InterruptedException {
        //Subscribe設定
        final int qos             = 2;
        final String clientId     = "Subscribe";

        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        client.setCallback(this);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(false);

        System.out.println("Connecting to broker:"+broker);
        client.connect(connOpts);

        client.subscribe(topic, qos);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try{
            //標準入力を受け取るまで待ち続ける
            br.readLine();
        }catch(IOException e){
            System.exit(1);
        }
        client.disconnect();
        client.close();
        System.out.println("Disconnected");
    }
}