package com.example.apibank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("health")
public class HealthController {

    @GetMapping("")
    public HttpStatus teste(){

        return HttpStatus.OK;
    }

}
