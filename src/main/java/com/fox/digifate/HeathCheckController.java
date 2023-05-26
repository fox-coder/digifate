package com.fox.digifate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
public class HeathCheckController {

    @RequestMapping(method = RequestMethod.GET)
    public Status isAlive() {
        return new Status("DigiFate is alive");
    }


    @Data
    @AllArgsConstructor
    private static class Status {
        private String status;
    }

}
