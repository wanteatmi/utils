package com.dog.tipspushutil.controller;

import com.dog.tipspushutil.service.BondInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private BondInfoService bondInfoService;

    @GetMapping("/test")
    public void test() throws InterruptedException {
      //  bondInfoService.getBondInfo();
    }
}
