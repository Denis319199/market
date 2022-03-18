package com.db.client;

import feign.Headers;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth", url = "http://localhost:8081/api")
public interface AuthClient {
  @RequestMapping(method = RequestMethod.POST, value = "/admin/users/existence")
  @Headers({"Content-Type: application/json"})
  List<Boolean> checkExistence(List<Integer> usersList, @RequestParam Boolean onlyEnabled);
}
