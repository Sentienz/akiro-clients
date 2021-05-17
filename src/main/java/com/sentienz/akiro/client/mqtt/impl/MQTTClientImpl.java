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
package com.sentienz.akiro.client.mqtt.impl;

import java.util.Set;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import com.sentienz.akiro.client.mqtt.CallBackHandler;
import com.sentienz.akiro.client.mqtt.MQTTClient;
import com.sentienz.akiro.client.mqtt.exception.MQTTException;
import com.sentienz.akiro.client.mqtt.model.MQTTConnectionConfig;
import com.sentienz.akiro.client.mqtt.model.MQTTMessage;

public class MQTTClientImpl implements MQTTClient {

  private CallBackHandler callback;
  private MqttClient mqttclient = null;
  private MQTTConnectionConfig clientConfig;

  public MQTTClientImpl(String deviceId, CallBackHandler callback,
      MQTTConnectionConfig clientConfig) throws MQTTException {

    try {
      this.callback = callback;
      this.clientConfig = clientConfig;

      if (this.mqttclient == null) {
        mqttclient = new MqttClient(constructbroker(clientConfig.getHost(), clientConfig.getPort()),
            deviceId, new MemoryPersistence());
        mqttclient.setCallback(new MqttCallback() {

          @Override
          public void messageArrived(String topic, MqttMessage message) throws Exception {
            MQTTMessage akiromsg = new MQTTMessage();
            akiromsg.setPayload(new String(message.getPayload()));
            akiromsg.setQos(message.getQos());
            callback.messageReceived(akiromsg);
          }

          @Override
          public void deliveryComplete(IMqttDeliveryToken token) {
            // TODO Auto-generated method stub
          }

          @Override
          public void connectionLost(Throwable cause) {
            // TODO Auto-generated method stub

          }
        });
      }
    } catch (MqttException e) {
      throw new MQTTException(e.getMessage());
    }
  }

  private String constructbroker(String host, int port) {
    return new String("tcp://" + host + ":" + port);
  }

  public void connect() throws MQTTException {
    try {
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(clientConfig.isCleanSession());
      connOpts.setUserName(clientConfig.getUsername());
      connOpts.setPassword(clientConfig.getPassword().toCharArray());

      MQTTMessage willMessage = clientConfig.getWillmessage();
      if (null != willMessage) {
        connOpts.setWill(willMessage.getTopic(), willMessage.getPayload().getBytes(),
            willMessage.getQos(), willMessage.isRetained());
      }
      connOpts.setKeepAliveInterval(clientConfig.getKeepAliveInterval());
      connOpts.setAutomaticReconnect(clientConfig.isAutoReconnect());
      connOpts.setConnectionTimeout(clientConfig.getConnectionTimeOut());
      mqttclient.connect(connOpts);
      callback.connectionEstablished();

    } catch (MqttException e) {
      callback.connectionFailure();
      throw new MQTTException(
          "Could not establish a connection to the broker. Reason: " + e.getMessage(), e);
    }
  }

  public void publish(String topic, String payload, int qos, boolean retained)
      throws MQTTException {
    try {
      mqttclient.publish(topic, payload.getBytes(), qos, retained);
      MQTTMessage message = new MQTTMessage();
      message.setPayload(payload);
      message.setQos(qos);
      callback.messageDelivered(message);
    } catch (MqttException e) {
      throw new MQTTException("Could not publish the message. Reason: " + e.getMessage(), e);
    }
  }

  public void publish(MQTTMessage message, String topic) throws MQTTException {
    MqttMessage mqttmessage = new MqttMessage(message.getPayload().getBytes());
    mqttmessage.setQos(message.getQos());
    try {
      mqttclient.publish(topic, mqttmessage);
      callback.messageDelivered(message);
    } catch (MqttException e) {
      throw new MQTTException("Could not publish the message. Reason: " + e.getMessage(), e);
    }
  }

  public void subscribe(String topic) throws MQTTException {
    try {
      mqttclient.subscribe(topic);
      callback.subscriptionComplete(topic);
    } catch (MqttException e) {
      throw new MQTTException("Could not subscribe to the topic. Reason: " + e.getMessage(), e);
    }
  }

  public void subscribe(Set<String> topics) throws MQTTException {
    try {
      mqttclient.subscribe(topics.stream().toArray(String[]::new));
      for (String topic : topics)
        callback.subscriptionComplete(topic);
    } catch (MqttException e) {
      throw new MQTTException("Could not subscribe to the topics. Reason: " + e.getMessage(), e);
    }
  }

  public void subscribe(String topic, int qos) throws MQTTException {
    try {
      mqttclient.subscribe(topic, qos);
    } catch (MqttException e) {
      throw new MQTTException("Could not subscribe to the topic. Reason: " + e.getMessage(), e);
    }
  }

  public void subscribe(Set<String> topics, int[] qos) throws MQTTException {
    try {
      mqttclient.subscribe(topics.stream().toArray(String[]::new), qos);
    } catch (MqttException e) {
      throw new MQTTException("Could not subscribe to the topics. Reason: " + e.getMessage(), e);
    }
  }

  public void unsubscribe(String topic) throws MQTTException {
    try {
      mqttclient.unsubscribe(topic);
    } catch (MqttException e) {
      throw new MQTTException("Could not unsubscribe from the topic. Reason: " + e.getMessage(), e);
    }
  }

  public void unsubscribe(Set<String> topics) throws MQTTException {
    try {
      mqttclient.unsubscribe(topics.stream().toArray(String[]::new));
    } catch (MqttException e) {
      throw new MQTTException("Could not unsubscribe from the topic. Reason: " + e.getMessage(), e);
    }
  }

  public void disconnect() throws MQTTException {
    try {
      mqttclient.disconnect();
    } catch (MqttException e) {
      throw new MQTTException("Could not disconnect from the broker" + e.getMessage(), e);
    }
  }
}
