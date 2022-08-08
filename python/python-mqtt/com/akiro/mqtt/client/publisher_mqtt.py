import random
import sys
import time

from paho.mqtt import client as mqtt_client
from configparser import ConfigParser
from pathlib import Path

#Read config.ini file
config_object = ConfigParser()
path_current_directory = sys.path[1]
config_object.read(path_current_directory + "/python-mqtt/conf/config.ini")
contents = Path(path_current_directory + "/python-mqtt/conf/sampleMessage.json").read_text()


# MQTT Broker
brokerConfig = config_object["MQTTConfig"]
broker = brokerConfig["host"]
port = int(brokerConfig["port"])
# MQTT Topic
topic = brokerConfig["topic"]
# generate client ID with pub prefix randomly
client_id = f'python-mqtt-{random.randint(0, 1000)}'
# MQTT Credentials
username = brokerConfig["username"]
password = brokerConfig["password"]
# Client Keep Alive time period
keepalive = int(brokerConfig["keepalive"])


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port, keepalive)
    return client


def publish(client):
    msg_count = 0
    while True:
        time.sleep(1)
        # msg = f"messages: {msg_count}"
        msg = contents
        result = client.publish(topic, msg, 1, False)
        # result: [0, 1]
        status = result[0]
        if status == 0:
            print(f"Send `{msg}` to topic `{topic}`")
        else:
            print(f"Failed to send message to topic {topic}")
        msg_count += 1


def run():
    client = connect_mqtt()
    client.loop_start()
    publish(client)
    client.loop_stop()


if __name__ == '__main__':
    run()
