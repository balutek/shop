package pl.balutek;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @RequestMapping(value = "/nonAuth", method = RequestMethod.GET)
    public String helloNonAuth() {
        return "nonAuth";
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String helloAuth() {
        return "auth";
    }

}
