package lzlz.boardgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/400")
    public String badRequest() {

        return "forward:/static/html/error/400.html";
    }

    @GetMapping("/404")
    public String notFound() {

        return "forward:/static/html/error/404.html";
    }

    @GetMapping("/500")
    public String serverError() {

        return "forward:/static/html/error/500.html";
    }
}
