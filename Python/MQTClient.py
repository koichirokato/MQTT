import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import time

class MyMQTTClass(mqtt.Client):
    def __init__(self):
        super().__init__()
        self.recieve_data = ""
        self.recieve_time = ""
        self.lasttime     = ""

    def on_connect(self, mqttc, obj, flags, rc):
        print("rc: "+str(rc))

    def on_message(self, mqttc, obj, msg):
        print(msg.topic+" "+str(msg.qos)+" "+str(msg.payload))
        self.recieve_time = time.time()
        self.recieve_data = (msg.payload).decode()

    def run(self, hostname, topic):
        self.connect(hostname, 1883, 60)
        self.subscribe(topic, 0)

        self.loop_start()
        
        rc = 0

        return rc

    def publish_message(self, host_name, topic, message):
        publish.single(topic, message, hostname=host_name)

    def isNew(self):
        flag = False
        if self.lasttime==self.recieve_time: flag =  False
        else: flag = True
        self.lasttime = self.recieve_time
        return flag

# If you want to use a specific client id, use
# mqttc = MyMQTTClass("client-id")
# but note that the client id must be unique on the broker. Leaving the client
# id parameter empty will generate a random id for you.
mqttc = MyMQTTClass()
rc = mqttc.run("localhost","testTopic1")

print("rc: "+str(rc))

i=0
while(1):
    i+=1
    # print(i)
    # mqttc.publish_message("localhost", "testTopic2",i)
    
    if mqttc.isNew(): print(mqttc.recieve_data)

    # time.sleep(5)