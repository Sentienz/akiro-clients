![Akiro Logo](/images/akiro.png)

# Akiro - MQTT Java Client

Akiro is a High Performance Messaging and Connectivity platform. Akiro's MQTT Broker is proven to handle millions of concurrent connections at production scale.<br/>
For more information on the product and solutions visit our website : [https://www.theakiro.com](https://www.theakiro.com)

This project is intended to offer simple and easy to use MQTT Client for applications which run on JVM based platforms.<br/>
The current implementation of the Java client is a wrapper around Eclipse Paho Java Client and provides.

For information on Javascript based MQTT client please refer to [Akiro MQTT Javascript Client](/js/README.md)

### Note on Eclipse Paho
Eclipse Paho is an open source project which provides a number of clients using which one can connect to the server over MQTT messaging protocol.<br/>
For complete information on the project : https://www.eclipse.org/paho/index.php?page=clients/js/index.php#

## Installation

### Prerequisites

* Java  : 1.8
* Maven : 3.x

Clone the repo into a directory of choice.<br/>
Perform a simple maven build using the command

<pre><code>mvn clean install</code></pre>
This will install the Akiro MQTT Client library to your local M2 Repo.

Plugin the library to your application by adding it as a Maven dependency.<br/>
<br/>

  	<dependency>
  		<groupId>com.sentienz.akiro</groupId>
  		<artifactId>akiro-mqtt-client</artifactId>
  		<version>1.0</version>
  	</dependency>

## Create MQTTClient
Create a Client Instance by providing the required connection configuration parameters.

### Connection Parameters
Following connection parameters are supported

```
{	
	host : "akiro.broker.ip",
	port : "akiro.broker.port",
	userName : “<string>”,
	password  : ”<string>”,
	connectionTimeOut (in seconds) : <number>,
	keepAliveInterval (in seconds) : <boolean>,
	cleanSession : <boolean>,
	autoReconnect : <boolean>,
	willMessage : <topic,payload,qos,retain>
}
```
<pre><code>// Create the connection config
MQTTMessage willmessage = new MQTTMessage("topic", "My Last Will Message", 0, true);

	MQTTConnectionConfig config = new MQTTConnectionConfigBuilder(host, username, password)
            .cleanSession(false)
            .port(port)
            .willMessage(willmessage)
            .keepAliveInterval(keepAliveInterval)
            .autoReconnect(isAutoReconnect)
            .connectionTimeOut(connectionTimeOut)
            .build();
</code></pre>

### CallBackHandler
Implement the interface CallBackHandler, to configure callbacks in order to take control on the actions and perform certain business logic.<br/>
Following callback functions are available<br/>
<br/>

	void messageReceived(MQTTMessage message);
	void messageDelivered(MQTTMessage message);
	void subscriptionComplete(String topic);
	void connectionEstablished();
	void connectionLost();
	void connectionFailure();

<pre><code>new CallBackHandler() {

            @Override
            public void messageReceived(MQTTMessage message) {
              System.out.println("Message received succesfully :: " + message.getPayload());
            }

            @Override
            public void messageDelivered(MQTTMessage message) {
              System.out.println("Message delivered successfully :: " + message.getPayload());
            }

            @Override
            public void connectionLost() {
              System.out.println("Connection lost");
            }

            @Override
            public void connectionEstablished() {
              System.out.println("Connection established successfully");
            }

            @Override
            public void subscriptionComplete(String topic) {
              System.out.println("Subscribed to :: " + topic);
            }

            @Override
            public void connectionFailure() {
              System.out.println("Connection Failed.");
            }
          }
</code></pre>

### Create the Client instance
<pre><code>MQTTClient client = MQTTClientFactory.createMqttClientInstance(<deviceId>,<callbackHandler>, <config>);
</code></pre>

## Connect to the Broker
Once the client is initialized, connect to the MQTTBroker with a simple call.

<pre><code>client.connect();</code></pre>

## Publish Message to a Topic
Once the connection is successfully established with the Broker, we can now create an MQTT message and publish to the required topic. 

<pre><code>MQTTMessage message = new MQTTMessage();
message.setQos(0);
message.setPayload("this is your message payload");
message.setRetained(false);

String topic = "your topic of interest";
client.publish(message, topic);
</code></pre>

## Subscribe to one or more topics

### Subscribe to a topic
<pre><code>client.subscribe("topic");
</code></pre>

### Subscribe to multiple topics
<pre><code>Set<String> topics = new HashSet<String>();
topics.add("topic1");
topics.add("topic2");
client.subscribe(topics);
</code></pre>

Upon subscribing to a topic, you will start receiving the messages at the callback method configured.

## Unsubscribe from one or more topics

### Unsubscribe from a Topic.
Unsubscribing from a topic is very similar and simple to subscribe.

<pre><code>client.unsubscribe(“topic name”);</code></pre>

### Unsubscribe from multiple topics.

<pre><code>Set<String> topics = new HashSet<String>();
topics.add("topic1");
topics.add("topic2");
client.unsubscribe(topics);
</code></pre>

## Disconnect
Disconnect the client from the broker using a simple call.

<pre><code>client.disconnect();
</code></pre>

## Complete Example Application
The example application to demonstrate the important operations

	package com.sentienz.akiro.client.mqtt.examples;

	import java.util.HashSet;
	import java.util.Set;
	import com.sentienz.akiro.client.mqtt.MQTTClient;
	import com.sentienz.akiro.client.mqtt.CallBackHandler;
	import com.sentienz.akiro.client.mqtt.MQTTClientFactory;
	import com.sentienz.akiro.client.mqtt.exception.MQTTException;
	import com.sentienz.akiro.client.mqtt.model.MQTTConnectionConfig;
	import com.sentienz.akiro.client.mqtt.model.MQTTMessage;
	import com.sentienz.akiro.client.mqtt.model.MQTTConnectionConfig.MQTTConnectionConfigBuilder;

	public class MQTTClientExample {

	  public static void main(String args[]) {

		String host = "akiro.broker.ip";
		String username = "deviceUserName";
		String password = "devicePassword";
		String deviceId = "mySampleDeviceId";
		int port = 1883;
		int keepAliveInterval = 2;
		boolean isAutoReconnect = true;
		int connectionTimeOut = 10;

		MQTTMessage willmessage = new MQTTMessage("topic", "My Last Will Message", 0, true);

		MQTTConnectionConfig config = new MQTTConnectionConfigBuilder(host, username, password)
				.cleanSession(false)
				.port(port)
				.willMessage(willmessage)
				.keepAliveInterval(keepAliveInterval)
				.autoReconnect(isAutoReconnect)
				.connectionTimeOut(connectionTimeOut)
				.build();

		try {

		  MQTTClient client =
			  MQTTClientFactory.createMqttClientInstance(deviceId, new CallBackHandler() {

				@Override
				public void messageReceived(MQTTMessage message) {
				  System.out.println("Message received succesfully :: " + message.getPayload());

				}

				@Override
				public void messageDelivered(MQTTMessage message) {
				  System.out.println("Message delivered successfully :: " + message.getPayload());
				}

				@Override
				public void connectionLost() {
				  System.out.println("Connection lost");
				}

				@Override
				public void connectionEstablished() {
				  System.out.println("Connection established successfully");
				}

				@Override
				public void subscriptionComplete(String topic) {
				  System.out.println("subscribed to :: " + topic);

				}

				@Override
				public void connectionFailure() {
				  System.out.println("Connection Failed.");

				}
			  }, config);
		  client.connect();

		  // Subscribe to topic1 and topic2

		  Set<String> topics = new HashSet<String>();
		  topics.add("topic1");
		  topics.add("topic3");
		  client.subscribe(topics);


		  // Publish message to topic1
		  MQTTMessage message = new MQTTMessage();
		  message.setQos(0);
		  message.setPayload("Your message payload");
		  message.setRetained(false);
		  client.publish(message, "topic1");

		  // Unsubscribe from topic1
		  client.unsubscribe("topic1");

		  // Disconnect the client
		  client.disconnect();

		} catch (MQTTException e) {
		  System.out.println("Exception occured " + e.getMessage());
		  e.printStackTrace();
		}
	  }
	}

