package com.covimyn.search;

import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CovimynSearchApplication {

    public static void main(String[] args) {

        SpringApplication.run(CovimynSearchApplication.class, args);
    }

    @Bean(name = "esResourceEndPoint")
    public String getEsResourceEndPoint() {
        return "http://localhost:9200/resource_alias";
    }

    @Bean(name ="jsonParser")
    public JSONParser getJSONParser(){
            JSONParser jsonParser = new JSONParser();
            return jsonParser;
    }

}
