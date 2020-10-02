package com.proptechos.model.auth;

import com.proptechos.utils.ObservationDeserializer;
import java.util.Objects;
import java.util.Properties;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * KafkaConfig represents configuration data used to coonect kafka enabled Azure eventhub
 */
public class KafkaConfig {

  private final ConfigBuilder builder;

  private KafkaConfig(ConfigBuilder builder) {
    this.builder = builder;
  }

  public String getTopic() {
    return builder.topic;
  }

  public KafkaRetryConfig retryConfig() {
    return builder.retryConfig;
  }

  public Properties getConfigMap() {
    Properties props = new Properties();
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, builder.bootstrapServer);
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, builder.groupId);
    props.setProperty(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, String.valueOf(builder.requestTimeout));
    props.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, builder.securityProtocol);
    props.setProperty(SaslConfigs.SASL_MECHANISM, builder.saslMechanism);
    props.setProperty(SaslConfigs.SASL_JAAS_CONFIG, builder.saslJaasConfig);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObservationDeserializer.class.getName());
    return props;
  }

  /**
   * @return KafkaConfig builder
   */
  public static ConfigBuilder builder() {
    return new ConfigBuilder();
  }

  public static class ConfigBuilder {

    protected static final String DEFAULT_GROUP_ID = "$Default";
    protected static final String DEFAULT_SECURITY_PROTOCOL = "SASL_SSL";
    protected static final String DEFAULT_SASL_MECHANISM = "PLAIN";
    protected static final long DEFAULT_REQUEST_TIMEOUT = 60_000;

    private String topic;

    private String eventHubNamespace;

    private String sharedAccessKey;

    private String bootstrapServer;

    private String saslJaasConfig;

    private String groupId;

    private long requestTimeout;

    private String securityProtocol;

    private String saslMechanism;

    private KafkaRetryConfig retryConfig;

    private ConfigBuilder() {}

    /**
     * @param topic eventhub name to connect to
     * @return builder
     */
    public ConfigBuilder eventHub(String topic) {
      this.topic = topic;
      return this;
    }

    /**
     * @param eventHubNamespace eventhub namespace
     * @return builder
     */
    public ConfigBuilder eventHubNamespace(String eventHubNamespace) {
      this.eventHubNamespace = eventHubNamespace;
      return this;
    }

    /**
     * @param sharedAccessKey shared eventhub namespace access key
     * @return builder
     */
    public ConfigBuilder sharedAccessKey(String sharedAccessKey) {
      this.sharedAccessKey = sharedAccessKey;
      return this;
    }

    /**
     * @param groupId listen group id
     * @return builder
     *
     * By default is set to  {@value ConfigBuilder#DEFAULT_GROUP_ID}.
     */
    public ConfigBuilder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    /**
     * @param timeout request timeout
     * @return builder
     *
     *  By default is set to  {@value ConfigBuilder#DEFAULT_REQUEST_TIMEOUT}.
     */
    public ConfigBuilder requestTimeout(long timeout) {
      this.requestTimeout = timeout;
      return this;
    }

    /**
     * @param securityProtocol security protocol for connecting to eventhub
     * @return builder
     *
     * By default is set to  {@value ConfigBuilder#DEFAULT_SECURITY_PROTOCOL}.
     */
    public ConfigBuilder securityProtocol(String securityProtocol) {
      this.securityProtocol = securityProtocol;
      return this;
    }

    /**
     * @param saslMechanism sasl mechanism
     * @return builder
     *
     * y default is set to  {@value ConfigBuilder#DEFAULT_SASL_MECHANISM}.
     */
    public ConfigBuilder saslMechanism(String saslMechanism) {
      this.saslMechanism = saslMechanism;
      return this;
    }

    /**
     * @param retryConfig configuration retry data
     * @return builder
     */
    public ConfigBuilder retryConfig(KafkaRetryConfig retryConfig) {
      this.retryConfig = retryConfig;
      return this;
    }

    /**
     * @return KafkaConfig instance
     */
    public KafkaConfig build() {
      if (Objects.isNull(groupId)) {
        groupId = DEFAULT_GROUP_ID;
      }
      if (Objects.isNull(securityProtocol)) {
        securityProtocol = DEFAULT_SECURITY_PROTOCOL;
      }
      if (Objects.isNull(saslMechanism)) {
        saslMechanism = DEFAULT_SASL_MECHANISM;
      }
      if (requestTimeout <= 0) {
        requestTimeout = DEFAULT_REQUEST_TIMEOUT;
      }
      if (Objects.isNull(retryConfig)) {
        retryConfig = KafkaRetryConfig.builder().build();
      }
      this.bootstrapServer = String.format("%s.servicebus.windows.net:9093", eventHubNamespace);
      String connectionString = String.format(
          "Endpoint=sb://%s.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=%s",
          eventHubNamespace, sharedAccessKey);
      this.saslJaasConfig = String.format(
          "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"%s\";",
          connectionString);
      return new KafkaConfig(this);
    }
  }

}
