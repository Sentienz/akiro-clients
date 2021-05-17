/*
 * Copyright 2021 by Sentienz Solutions Private Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
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

    MQTTConnectionConfig config =
        new MQTTConnectionConfigBuilder(host, username, password).cleanSession(false).port(port)
            .willMessage(willmessage).keepAliveInterval(keepAliveInterval)
            .autoReconnect(isAutoReconnect).connectionTimeOut(connectionTimeOut).build();

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
