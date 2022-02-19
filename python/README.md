# Akiro - MQTT Python Client Example

The example is based on Eclipse Paho Python Client project
## Note on Eclipse Paho
Eclipse Paho is an open source project which provides a number of clients using which one can connect to the server over MQTT messaging protocol. The Paho Python Client provides a client class with support for MQTT v5.0, MQTT v3.1.1, and v3.1 on Python 2.7 or 3.x. It also provides some helper functions to make publishing one off messages to an MQTT server very straightforward.
For complete information on the project : https://www.eclipse.org/paho/index.php?page=clients/python/index.php

## Installation
The Python client can be downloaded and installed from PyPI using the pip tool:
<pre><code>
pip install paho-mqtt
</code></pre>


## Create a Client instance
Create a Client Instance by providing the required parameters.

<pre><code>
<!-- var mqttClient = new Paho.MQTT.Client(mqtt broker ip, broker port, your clientId/deviceId) -->
import paho.mqtt.client as mqtt

mqttClient = mqtt.Client(client_id="", clean_session=True, userdata=None, protocol=MQTTv311, transport="tcp")
</code></pre>

### Client Parameters
Following parameters are supported for creating a client

client_id(type - string) - the unique client id string used when connecting to the broker. If client_id is zero length or None, then one will be randomly generated. In this case the clean_session parameter must be True.
clean_session(type - boolean) - a boolean that determines the client type. If True, the broker will remove all information about this client when it disconnects. If False, the client is a durable client and subscription information and queued messages will be retained when the client disconnects.
userdata - user defined data of any type that is passed as the userdata parameter to callbacks. It may be updated at a later point with the user_data_set() function.
protocol - the version of the MQTT protocol to use for this client. Can be either MQTTv31 or MQTTv311
transport(type - string) - set to "websockets" to send MQTT over WebSockets. Leave at the default of "tcp" to use raw TCP.

## Set username and password
Set a username and optionally a password for broker authentication. Must be called before connect().

<pre><code>
username = "example"
password = None
mqttClient.username_pw_set(username, password)
</code></pre>

## Connect to the Broker
Connect the client to the broker by passing the connection parameters

<pre><code>
mqttClient.connect(host, port=1883, keepalive=60, bind_address="")
</code></pre>

### Connection Parameters
Following connection parameters are supported

host(type - string) - the hostname or IP address of the remote broker
port(type - number) - the network port of the server host to connect to. Defaults to 1883. Note that the default port for MQTT over SSL/TLS is 8883 so if you are using tls_set() or tls_set_context(), the port may need providing manually
keepalive(type - number) - maximum period in seconds allowed between communications with the broker. If no other messages are being exchanged, this controls the rate at which the client will send ping messages to the broker
bind_address(type - string) - the IP address of a local network interface to bind this client to, assuming multiple interfaces exist

## Callbacks
MQTT Client can be connected by providing the respective callbacks in order to take control on the actions and perform certain business logic.

<pre><code>
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("Connected to MQTT Broker!")
    else:
        print("Failed to connect, return code %d\n", rc)

def on_disconnect(client, userdata, rc):
    if rc == 0:
        print("MQTT Broker disconnected!")
    else:
        print("Failed to disconnect, return code %d\n", rc)

def on_message(client, userdata, message):
    print("Received message '" + str(message.payload) + "' on topic '"
    + message.topic + "' with QoS " + str(message.qos))

def on_publish(client, userdata, mid):
	print("Message published!")

def on_subscribe(client, userdata, mid, granted_qos):
	print("Subscribed client!")

def on_unsubscribe(client, userdata, mid):
	print("Unsubscribed client!")

mqttClient.on_connect = on_connect
mqttClient.on_disconnect = on_disconnect
mqttClient.on_message = on_message
mqttClient.on_publish = on_publish
mqttClient.on_subscribe = on_subscribe
mqttClient.on_unsubscribe = on_unsubscribe

mqttClient.connect(broker, port)
</code></pre>

## Publish Message to a Topic
Once the connection is successfully established with the Broker, we can now publish message to the required topic. 

<pre><code>
publish(topic, payload=None, qos=0, retain=False)
</code></pre>

### Publish Parameters

topic - the topic that the message should be published on
payload - the actual message to send. If not given, or set to None a zero length message will be used. Passing an int or float will result in the payload being converted to a string representing that number. If you wish to send a true int/float, use struct.pack() to create the payload you require
qos - the quality of service level to use
retain - if set to True, the message will be set as the "last known good"/retained message for the topic.

<pre><code>
topic = "python/mqtt"
message = "Test Message"
mqttClient.publish(topic, msg, 1, False)
</code></pre>

## Subscribe to a Topic
Upon subscribing to a topic, you will start receiving the messages at the callback method configured along with the MQTT Message object.

<pre><code>
subscribe(topic, qos=0)
</code></pre>

### Subscribe Parameters

topic - a string specifying the subscription topic to subscribe to.
qos - the desired quality of service level for the subscription. Defaults to 0.

<pre><code>
topic = "python/mqtt"
mqttClient.subscribe(topic)
</code></pre>

## Unsubscribe from a Topic
Unsubscribing from a topic is very similar and simple to subscribe.

<pre><code>
mqttClient.unsubscribe(“topic name”)
</code></pre>

## Disconnect
Disconnect the client from the broker using a simple call.

<pre><code>
mqttClient.disconnect();
</code></pre>

## Complete Example Application
The example application to demonstrate the important operations 

### Publisher Example -
<pre><code>
import random
import time
from paho.mqtt import client as mqtt_client

broker = "<akiro.broker.ip>"
port = 1883
topic = "python/mqtt"
# generate client ID with pub prefix randomly
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = "<username>"
password = "<password>"

def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
    	# your other business logics
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    # Creates a client
    client = mqtt_client.Client(client_id)

    # Sets username and password
    client.username_pw_set(username, password)

    # Binds the callback
    client.on_connect = on_connect

    # Connects the client
    client.connect(broker, port)
    return client


def publish(client):
    msg_count = 0
    while True:
        time.sleep(1)
        msg = f"messages: {msg_count}"

        # Publishes the message
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
</code></pre>

### Subscriber Example -
<pre><code>
import random
from paho.mqtt import client as mqtt_client

broker = "<akiro.broker.ip>"
port = 1883
topic = "python/mqtt"
# generate client ID with pub prefix randomly
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = "<username>"
password = "<password>"

def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
    	# add your business logic

        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

# Creates a client
client = mqtt_client.Client(client_id)

# Sets username and password
client.username_pw_set(username, password)

# Binds the callback
client.on_connect = on_connect

# Connects to a broker
client.connect(broker, port)
return client


def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
    	# Add your business logic here

        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")

    # Subcribes to a topic
    client.subscribe(topic)

    #Binds the callback
    client.on_message = on_message


def run():
    client = connect_mqtt()
    subscribe(client)
    client.loop_forever()
</code></pre>

### Test -
Save both the codes in seperate files and run them 
<pre><code>
python publisher_mqtt.py
python subscriber_mqtt.py
</code></pre>
