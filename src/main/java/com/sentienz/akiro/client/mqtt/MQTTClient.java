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

import java.util.Set;
import com.sentienz.akiro.client.mqtt.exception.MQTTException;
import com.sentienz.akiro.client.mqtt.model.MQTTMessage;

/**
 * Interface for MQTT Client
 * 
 * @author vkashyap
 *
 */
public interface MQTTClient {

  /**
   * Connect to the MQTT Broker
   * 
   * @throws MQTTException
   */
  public void connect() throws MQTTException;

  /**
   * Publish message to the specified topic
   * 
   * @param topic
   * @param payload
   * @param qos
   * @param retained
   * @throws MQTTException
   */
  public void publish(String topic, String payload, int qos, boolean retained) throws MQTTException;

  /**
   * Publish message to the specified topic
   * 
   * @param message
   * @param topic
   * @throws MQTTException
   */
  public void publish(MQTTMessage message, String topic) throws MQTTException;

  /**
   * Subscribe to the specified topic.
   * 
   * @param topic
   * @throws MQTTException
   */
  public void subscribe(String topic) throws MQTTException;

  /**
   * Subscribe to the specified topic with qos filter
   * 
   * @param topic
   * @param qos
   * @throws MQTTException
   */
  public void subscribe(String topic, int qos) throws MQTTException;

  /**
   * Subscribe to the given set of topics.
   * 
   * @param topics
   * @throws MQTTException
   */
  public void subscribe(Set<String> topics) throws MQTTException;

  /**
   * Subscribe to the set of topics with corresponding qos filters
   * 
   * @param topic
   * @param qos
   * @throws MQTTException
   */
  public void subscribe(Set<String> topic, int[] qos) throws MQTTException;

  /**
   * Unsubscribe from a topic.
   * 
   * @param topic
   * @throws MQTTException
   */
  public void unsubscribe(String topic) throws MQTTException;

  /**
   * Unsubscribe from a set of topics.
   * 
   * @param topics
   * @throws MQTTException
   */
  public void unsubscribe(Set<String> topics) throws MQTTException;

  /**
   * Disconnect from the MQTT Broker
   * 
   * @throws MQTTException
   */
  public void disconnect() throws MQTTException;

}
