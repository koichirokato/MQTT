import paho.mqtt.publish as publish
import time
i = 0
while True:
    time.sleep(3)
    i += 1
    print(i)
    publish.single("testTopic2", i, hostname="localhost")