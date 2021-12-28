package com.example.mongospringboottest.util;

import javax.annotation.PostConstruct;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyVaultUtil {

    @Value("${app.keyVault.uri}")
    private String uri;
    private SecretClient secretClient;
    
    @PostConstruct
    private void init() {
        this.secretClient = new SecretClientBuilder()
            .vaultUrl(uri)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();
    }
    
    public String getSecret(String secretName) {
        return this.secretClient.getSecret(secretName).getValue();
    }
    
}
