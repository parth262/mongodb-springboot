package com.example.mongospringboottest.controller;

import javax.servlet.http.HttpServletRequest;

import com.example.mongospringboottest.config.SwaggerJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SwaggerController {

    private SwaggerJson swaggerJson;

    @Autowired
    public SwaggerController(SwaggerJson swaggerJson) {
        this.swaggerJson = swaggerJson;
    }

    @RequestMapping(
            value = {"/api-docs"},
            method = {RequestMethod.GET},
            produces = {"application/json", "application/hal+json"}
    )
    @ResponseBody
    public ResponseEntity<String> getDocumentation(
            @RequestParam(value = "group", required = false) String swaggerGroup,
            HttpServletRequest servletRequest) {

        return ResponseEntity.ok().body(this.swaggerJson.getJson());
    }
    
}