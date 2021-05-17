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
 * Config object to establish an MQTT connection with the Broker Use
 * {@link MQTTConnectionConfigBuilder} to create the config object
 * 
 * @author vkashyap
 *
 */
public class MQTTConnectionConfig {

  private String host;
  private String username;
  private String password;
  private boolean cleanSession;
  private int port;
  private MQTTMessage willMessage;
  private int keepAliveInterval;
  private boolean autoReconnect;
  private int connectionTimeOut;

  private MQTTConnectionConfig(MQTTConnectionConfigBuilder configBuilder) {
    this.host = configBuilder.host;
    this.port = configBuilder.port;
    this.username = configBuilder.username;
    this.password = configBuilder.password;
    this.cleanSession = configBuilder.cleanSession;
    this.willMessage = configBuilder.willMessage;
    this.keepAliveInterval = configBuilder.keepAliveInterval;
    this.autoReconnect = configBuilder.autoReconnect;
    this.connectionTimeOut = configBuilder.connectionTimeOut;
  }

  public static class MQTTConnectionConfigBuilder {

    private String host;
    private String username;
    private String password;
    private boolean cleanSession;
    private int port;
    private MQTTMessage willMessage;
    private int keepAliveInterval;
    private boolean autoReconnect;
    private int connectionTimeOut;

    public MQTTConnectionConfigBuilder(String host, String username, String password) {
      this.host = host;
      this.username = username;
      this.password = password;
    }

    public MQTTConnectionConfigBuilder cleanSession(boolean clearSession) {
      this.cleanSession = clearSession;
      return this;
    }

    public MQTTConnectionConfigBuilder port(int port) {
      this.port = port;
      return this;
    }

    public MQTTConnectionConfigBuilder willMessage(MQTTMessage willMessage) {
      this.willMessage = willMessage;
      return this;
    }

    public MQTTConnectionConfigBuilder willMessage(String topic, String payload, int qos,
        boolean retained) {
      this.willMessage = new MQTTMessage(topic, payload, QosType.getValueOf(qos), retained);
      return this;
    }

    public MQTTConnectionConfigBuilder keepAliveInterval(int keepAliveInterval) {
      this.keepAliveInterval = keepAliveInterval;
      return this;
    }

    public MQTTConnectionConfigBuilder autoReconnect(boolean autoReconnect) {
      this.autoReconnect = autoReconnect;
      return this;
    }

    public MQTTConnectionConfig build() {
      return new MQTTConnectionConfig(this);
    }

    public MQTTConnectionConfigBuilder connectionTimeOut(int timeOut) {
      this.connectionTimeOut = timeOut;
      return this;
    }

  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return the cleanSession
   */
  public boolean isCleanSession() {
    return cleanSession;
  }

  /**
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * @return the will message
   */
  public MQTTMessage getWillmessage() {
    return willMessage;
  }

  /**
   * @return the keep alive interval
   */
  public int getKeepAliveInterval() {
    return keepAliveInterval;
  }

  /**
   * @return is auto reconnection enabled
   */
  public boolean isAutoReconnect() {
    return autoReconnect;
  }

  /**
   * @return the connection timeOut
   */
  public int getConnectionTimeOut() {
    return connectionTimeOut;
  }
}
