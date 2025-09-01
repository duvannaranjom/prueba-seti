package com.pactual.btg.pactual.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
class DynamoConfig {

    @Bean
    DynamoDbClient dynamoDb(DynamoProps props, Environment env) {
        var builder = DynamoDbClient.builder()
                .region(Region.of(props.getRegion() != null ? props.getRegion() : "us-east-1"));

        if (env.acceptsProfiles(Profiles.of("local")) && props.getEndpoint() != null) {
            builder = builder
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create("local", "local")))
                    .endpointOverride(URI.create(props.getEndpoint()));
        }
        return builder.build();
    }

    @Bean
    DynamoDbEnhancedClient enhanced(DynamoDbClient client) {
        return DynamoDbEnhancedClient.builder().dynamoDbClient(client).build();
    }
}
