import random
import sys

from paho.mqtt import client as mqtt_client

from configparser import ConfigParser

#Read config.ini file
path_current_directory = sys.path[1]
config_object = ConfigParser()
config_object.read(path_current_directory + "/python-mqtt/conf/config.ini")

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


def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")

    client.subscribe(topic)
    client.on_message = on_message


def run():
    client = connect_mqtt()
    subscribe(client)
    client.loop_forever()


if __name__ == '__main__':
    run()
