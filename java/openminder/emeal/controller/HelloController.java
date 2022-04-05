package openminder.emeal.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class HelloController {

    @RequestMapping({ "/hello" })
    public String firstPage() {
        return "Hello. you have valid JWT (JSon Web Token)!";
    }

}
