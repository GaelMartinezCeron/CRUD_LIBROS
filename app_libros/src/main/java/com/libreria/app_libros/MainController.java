package com.libreria.app_libros;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";  // Esto busca src/main/resources/templates/index.html
    }
}