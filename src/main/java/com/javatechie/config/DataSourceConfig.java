package com.javatechie.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.service.SecretManagerService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    @Autowired
    private SecretManagerService secretManagerService;

    @Bean
    @Primary
    public DataSource dataSource() throws Exception {
        String secret = secretManagerService.getSecret();
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode secretJson = objectMapper.readTree(secret);

        String username = secretJson.get("username").asText();
        String password = secretJson.get("password").asText();
        String host = secretJson.get("host").asText();

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://"+host+":3306/"+secretJson.get("dbname").asText());
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
