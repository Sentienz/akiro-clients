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
package com.sentienz.akiro.client.mqtt;

import com.sentienz.akiro.client.mqtt.model.MQTTMessage;

/**
 * Implement this interface to receive callback for the Mqtt operations with the broker.
 * 
 * @author vkashyap
 *
 */
public interface CallBackHandler {

  /**
   * Called when a message is received for a topic.
   * 
   * @param message the received message object
   */
  void messageReceived(MQTTMessage message);

  /**
   * Called when a message is sent to a topic.
   * 
   * @param message the message object sent
   */
  void messageDelivered(MQTTMessage message);

  /**
   * Called upon successfully subscribing to a topic
   * 
   * @param topic
   */
  void subscriptionComplete(String topic);

  /**
   * Called when the connection is established with the broker.
   */
  void connectionEstablished();

  /**
   * Called when the connection with the broker is disconnected.
   */
  void connectionLost();

  /**
   * Called when there is a failure connecting with the broker
   */
  void connectionFailure();


}
