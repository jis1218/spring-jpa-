package com.insup.inflearn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    @PostMapping("/api/temp")
    public void temp(@RequestBody) {
        System.out.println();
    }

}
