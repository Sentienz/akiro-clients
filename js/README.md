# Akiro - MQTT Javascript Client Example

The example is based on Eclipse Paho Javascript Client project
## Note on Eclipse Paho
Eclipse Paho is an open source project which provides a number of clients using which one can connect to the server over MQTT messaging protocol. The Paho Javascript client is a browser based library and is based on Websockets to connect to the MQTT Broker. This is one of the most widely used Javascript clients for MQTT.
For complete information on the project : https://www.eclipse.org/paho/index.php?page=clients/js/index.php#

## Installation
Download the latest stable version of the library from the project’s downloads page.
Upon downloading, you will find both full and minified versions. Include the downloaded library in your HTML file.

<script src=”<path to the library mqttws31.js>” type=”text/javascript”>
</script>


## Create a Client instance
Create a Client Instance by providing the required parameters.
<pre><code>
var mqttClient = new Paho.MQTT.Client(mqtt broker ip, broker port, your clientId/deviceId)
</code></pre>


## Connect to the Broker
Connect the client to the broker by passing the connection parameters

<pre><code>
mqttClient.connect({
	// connection parameters
});
</code></pre>

### Connection Parameters
Following connection parameters are supported

<pre><code>
	var connectionParams = {
		userName : “<string>”,
		password  : ”<string>”,
		timeOut     : <number>,
		keepAliveInterval : <number>,
		useSSL     : <boolean>,
		cleanSession : <boolean>
	}
</code></pre>

## Connect 
<pre><code>
mqttClient.connect(connectionParams);
</code></pre>

### Connect with callbacks
MQTT Client can be connected by providing the respective callbacks in order to take control on the actions and perform certain business logic.

<pre><code>
mqttClient.onSuccess = function(){
console.log(“Connection Established Succesfully”);
// your other business logics
}

	
function onMessageArrived(message){
console.log(“Message Received : “ + message.payloadString);
// your business logic code
}

function onConnectionLost(object){
console.log(“Connection Lost : “ + object.errorMessage)
}

mqttClient.onMessageArrived = onMessageArrived

mqttClient.connect({
userName:”<>”,
password : “<>”,
onConnectionLost  : onConnectionLost
});
</code></pre>

## Publish Message to a Topic
Once the connection is successfully established with the Broker, we can now create an MQTT message and publish to the required topic. 

<pre><code>
var mqttMessage = new Paho.MQTT.Message(“ Your message payload here”);
mqttMessage.destinationName = “your topic name”;
mqttMessage.qos                      = Number(0); // Takes 0,1 or 2
mqttMessage.retained               = true or false;

mqttClient.send(mqttMessage);
</code></pre>

## Subscribe to a Topic

<pre><code>
	mqttClient.subscribe(“your topic of interest”)
</code></pre>

Upon subscribing to a topic, you will start receiving the messages at the callback method configured along with the MQTT Message object. onMessageArrived

### Subscribe with callbacks
You can also subscribe to a topic with additional callbacks based on your needs.

<pre><code>
var options = {
		qos : Number (0) // Takes 0,1 or 2
		onSuccess : onSubscribeSuccess,
		onFailure    : onSubscribeFailure,
		timeout       : Number(10)
};

mqttClient.subscribe(“your topic of interest”, options);
</code></pre>

## Unsubscribe from a Topic
Unsubscribing from a topic is very similar and simple to subscribe.

<pre><code>
mqttClient.unsubscribe(“topic name”)
</code></pre>

### Unsubscribe with callbacks

<pre><code>
var unsubscribeOptions = {
		timeout : Number(10),
		onSuccess : onUnsubscribeSuccess,
		onFailure    : onUnsubscribeFailure
};

mqttClient.unsubscribe(“topic name”, unsubscribeOptions);
</code></pre>

## Disconnect
Disconnect the client from the broker using a simple call.

<pre><code>
mqttClient.disconnect();
</code></pre>

## Complete Example Application
The example application to demonstrate the important operations 

	<!DOCTYPE html>
	<html>

	<script src="https://cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.0.1/mqttws31.js" type="text/javascript"></script>

	<script type="text/javascript">

		var brokerIp = "<akiro.broker.ip>"
		var brokerPort = 80
		var deviceUserName = "<username>"
		var devicePassword = "<password>"

		var mqttClient = new Paho.MQTT.Client(brokerIp, brokerPort, "example-mqtt-js-application");

		function onSuccess() {
			console.log("Connection Established Succesfully");
			// your other business logics

			mqttClient.subscribe("ExampleJsClientTopic", {
				onSuccess: function () {
					console.log("Subscription successful.")

					console.log("Sending message...")
					var mqttMessage = new Paho.MQTT.Message("Your message payload here");

					mqttMessage.destinationName = "ExampleJsClientTopic";
					mqttMessage.qos = Number(0); // Takes 0,1 or 2
					mqttMessage.retained = true;

					mqttClient.send(mqttMessage);
				}
			})
		}

		function onFailure(error) {
			console.log("Connection Could not be established." + error)
		}

		function onMessageArrived(message) {
			console.log("Message Received : " + message.payloadString);
			// your business logic code

			mqttClient.unsubscribe("ExampleJsClientTopic")
			// mqttClient.disconnect();
		}

		mqttClient.onConnectionLost = function (object) {
			console.log("Connection Lost : " + object.errorMessage)
		}

		mqttClient.onMessageArrived = onMessageArrived

		var connectionParams = {
			userName: deviceUserName,
			password: devicePassword,
			timeout: 10,
			keepAliveInterval: 10,
			useSSL: true,
			cleanSession: true,
			onSuccess: onSuccess,
			onFailure: onFailure,
		};

		mqttClient.connect(connectionParams);

	</script>

	<body>
	</body>

	</html>
