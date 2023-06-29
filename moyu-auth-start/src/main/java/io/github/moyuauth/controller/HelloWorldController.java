package io.github.moyuauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2022/05/12.
 */
@RestController
public class HelloWorldController {

    @GetMapping("hello")
    public String helleWorld() {
        return "hello world !!!";
    }
}
