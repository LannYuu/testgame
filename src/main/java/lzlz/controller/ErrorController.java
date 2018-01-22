package lzlz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    @GetMapping("/400")
    public String badRequest() {

        return "redirect:/html/error/400.html";
    }

    @GetMapping("/404")
    public String notFound() {

        return "redirect:/html/error/404.html";
    }

    @GetMapping("/500")
    public String serverError() {

        return "redirect:/html/error/500.html";
    }
}
