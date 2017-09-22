package pl.balutek.common.contract.auth;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth")
public interface AuthClient {

    @PostMapping("/parseToken")
    AuthData parseToken(@RequestParam("token") String token);

}
