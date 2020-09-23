package com.proptechos.model.auth;

import com.proptechos.utils.ObservationDeserializer;
import java.util.Objects;
import java.util.Properties;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

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

  public static ConfigBuilder builder(
      String topic,
      String bootstrapServer,
      String connectionString) {
    return new ConfigBuilder(topic, bootstrapServer, connectionString);
  }

  public static class ConfigBuilder {

    private final String topic;

    private final String bootstrapServer;

    private final String saslJaasConfig;

    private String groupId;

    private long requestTimeout;

    private String securityProtocol;

    private String saslMechanism;

    private KafkaRetryConfig retryConfig;

    private ConfigBuilder(
        String topic,
        String bootstrapServer,
        String connectionString) {
      this.topic = topic;
      this.bootstrapServer = bootstrapServer;
      this.saslJaasConfig = String.format(
          "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$ConnectionString\" password=\"%s\";",
          connectionString);
    }

    public ConfigBuilder groupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public ConfigBuilder requestTimeout(long timeout) {
      this.requestTimeout = timeout;
      return this;
    }

    public ConfigBuilder securityProtocol(String securityProtocol) {
      this.securityProtocol = securityProtocol;
      return this;
    }

    public ConfigBuilder saslMechanism(String saslMechanism) {
      this.saslMechanism = saslMechanism;
      return this;
    }

    public ConfigBuilder retryConfig(KafkaRetryConfig retryConfig) {
      this.retryConfig = retryConfig;
      return this;
    }

    public KafkaConfig build() {
      if (Objects.isNull(groupId)) {
        groupId = "$Default";
      }
      if (Objects.isNull(securityProtocol)) {
        securityProtocol = "SASL_SSL";
      }
      if (Objects.isNull(saslMechanism)) {
        saslMechanism = "PLAIN";
      }
      if (requestTimeout <= 0) {
        requestTimeout = 60_000;
      }
      if (Objects.isNull(retryConfig)) {
        retryConfig = KafkaRetryConfig.builder().build();
      }
      return new KafkaConfig(this);
    }
  }

}
