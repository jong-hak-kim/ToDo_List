package com.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//SSR -> jsp, thymeleaf, mustache, freemarker
//SPA -> vue
// -> javascript + <-> API (JSON)
// vue -> vue + SSR = nuxt
// react -> react + SSR = next

@RestController
public class ToDoController {

    @GetMapping("/todos")
    public String get() {
        return "Hello World";
    }
}
