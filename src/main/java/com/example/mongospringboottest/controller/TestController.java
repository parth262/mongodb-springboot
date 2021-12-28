package com.example.mongospringboottest.controller;

import com.example.mongospringboottest.util.KeyVaultUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KeyVaultUtil keyVaultUtil;

    @GetMapping("/secret/{secretName}")
    public String getSecret(@PathVariable String secretName) {
        return keyVaultUtil.getSecret(secretName);
    }
    
}
