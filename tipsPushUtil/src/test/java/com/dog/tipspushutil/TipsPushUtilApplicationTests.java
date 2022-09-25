package com.dog.tipspushutil;

import com.dog.tipspushutil.service.BondInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TipsPushUtilApplicationTests {

    @Autowired
    private BondInfoService bondInfoService;

    @Test
    void contextLoads() {
        try {
            bondInfoService.handleBondInfo();
          //  bondInfoService.getBondInfo2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
