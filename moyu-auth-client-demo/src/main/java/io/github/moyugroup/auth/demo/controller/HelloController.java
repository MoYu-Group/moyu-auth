package io.github.moyugroup.auth.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fanfan on 2023/07/01.
 */
@RestController
public class HelloController {

    @GetMapping("hello")
    public String helleWorld() {
        return "hello Moyu-Auth-Client !!!";
    }

}
