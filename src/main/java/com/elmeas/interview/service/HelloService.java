package com.elmeas.interview.service;

import com.elmeas.interview.model.ResponseToHello;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
      public ResponseToHello sayHello() {
          return new ResponseToHello("Hello, World!");
      }
}
