package com.elmeas.interview.controller;

import com.elmeas.interview.model.ResponseToHello;
import com.elmeas.interview.service.HelloService;
import com.elmeas.interview.service.InMemoryRateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  private final HelloService helloService;
  private final InMemoryRateLimiter rateLimiter;

  public HelloController(HelloService helloService, InMemoryRateLimiter rateLimiter) {
    this.helloService = helloService;
    this.rateLimiter = rateLimiter;
  }

  @GetMapping("/hello")
  public ResponseEntity<ResponseToHello> sayHello(
      @RequestHeader(value = "X-User-Id", defaultValue = "anonymous") String userId) {
    if (!rateLimiter.allowRequest(userId)) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .body(new ResponseToHello("Too many requests"));
    }

    return ResponseEntity.ok(helloService.sayHello());
  }
}
