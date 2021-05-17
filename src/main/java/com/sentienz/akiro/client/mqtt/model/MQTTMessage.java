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
package com.sentienz.akiro.client.mqtt.model;

/**
 * The message object to be exchanged with the devices
 * 
 * @author vkashyap
 *
 */
public class MQTTMessage {

  private String topic;
  private String payload;
  private QosType qostype;
  private boolean retained;

  public MQTTMessage(String topic2, String payload2, QosType qostype, boolean retained2) {
    this.topic = topic2;
    this.payload = payload2;
    this.qostype = qostype;
    this.retained = retained2;
  }

  public MQTTMessage(String topic2, String payload2, int qos, boolean retained2) {
    this.topic = topic2;
    this.payload = payload2;
    this.qostype = QosType.getValueOf(qos);
    this.retained = retained2;
  }

  public MQTTMessage() {}

  public void setQos(int qos) {
    this.qostype = QosType.getValueOf(qos);
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public QosType getQostype() {
    return qostype;
  }

  public void setQostype(QosType qostype) {
    this.qostype = qostype;
  }

  public boolean isRetained() {
    return retained;
  }

  public void setRetained(boolean retained) {
    this.retained = retained;
  }

  public int getQos() {
    return this.qostype.value();
  }
}
