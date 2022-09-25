package com.dog.tipspushutil.service;

import com.dog.tipspushutil.constant.MessageType;
import com.dog.tipspushutil.domain.Bond;
import com.dog.tipspushutil.service.push.MailPushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class BondInfoService {

    private final Logger logger = LoggerFactory.getLogger(BondInfoService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MailPushService mailPushService;

    @Autowired
    private ObjectMapper mapper;
    //private final String bondUrl = "https://datacenter-web.eastmoney.com/api/data/v1/get?reportName=RPT_BOND_CB_LIST&sortColumns=PUBLIC_START_DATE&sortTypes=-1&columns=ALL&pageSize=10&pageNumber=1";
    private final String bondUrl = "https://datacenter-web.eastmoney.com/api/data/v1/get?reportName=RPT_BOND_CB_LIST&sortColumns=PUBLIC_START_DATE&sortTypes=-1&columns=SECURITY_NAME_ABBR,VALUE_DATE,SECUCODE,RATING&pageSize=10&pageNumber=1";

    private int status;

    private final int retryNum = 2;

    private final int retryGapTime = 2 * 60 * 1000;

    //  private final ObjectMapper mapper = new ObjectMapper();
    //每天10点执行
    @Scheduled(cron = "0 0 10 * * ?")
    // @Scheduled(cron = "0 */1 * * * ?")
    public void handleBondInfo() throws InterruptedException, JsonProcessingException {
        String pushContent = "你好呀，今天加油冲哦 (*^▽^*) \n\n";
        int defalutLength = pushContent.length();
        String bondInfo = getBondInfo();
        logger.warn("respose info = {}", bondInfo);
        String currTime = "\n"  + new Date().toString();
        if (status == HttpStatus.OK.value()) {
            Map bondMap = mapper.readValue(bondInfo, Map.class);
            Map result = (Map) bondMap.get("result");
            String data = mapper.writeValueAsString(result.get("data"));
            List<Bond> bonds = mapper.readValue(data, new TypeReference<List<Bond>>() {
            });
            for (int i = 0; i < bonds.size(); i++) {
                Date currDate = new Date();
                Bond bond = bonds.get(i);
                Date issueDate = bond.getIssueDate();
                if (issueDate != null && issueDate.getYear() == currDate.getYear() &&
                        issueDate.getMonth() == currDate.getMonth() && issueDate.getDay() == currDate.getDate()) {
                    pushContent = pushContent+"     "+(i+1)+"   "+ bond.getBondCode() + "  " + bond.getBondName() + "  " + bond.getRating() + "\n";
                }
             //   pushContent = pushContent+"     "+(i+1)+"   "+ bond.getBondCode() + "  " + bond.getBondName() + "  " + bond.getRating() + "\n";
            }
            //发送邮件
            if (pushContent.length() > defalutLength) {
                mailPushService.sendMail(MessageType.bondMail, pushContent + currTime);
            }
        } else {
            mailPushService.sendMail(MessageType.REQUESTEXCEPTION, bondInfo + currTime);
        }

    }


    public String getBondInfo() throws InterruptedException {
        String respose = "";
        for (int i = 0; i < retryNum; i++) {
            ResponseEntity<String> entity = restTemplate.getForEntity(bondUrl, String.class);
            HttpStatus statusCode = entity.getStatusCode();
            status = statusCode.value();
            respose = entity.getBody();
            if (status == HttpStatus.OK.value()) {
                return respose;
            } else {
                logger.warn("respose error = {}", entity.toString());
                Thread.sleep(retryGapTime);
            }
        }
        return respose;

    }


    public void getBondInfo2() throws Exception {
        String url = "https://datacenter-web.eastmoney.com/api/data/v1/get?reportName=RPT_BOND_CB_LIST&sortColumns=PUBLIC_START_DATE&sortTypes=-1&columns=SECURITY_NAME_ABBR,VALUE_DATE&pageSize=10&pageNumber=1";

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);

        // 响应模型
        CloseableHttpResponse response = null;

        // 由客户端执行(发送)Get请求
        response = httpClient.execute(httpGet);
        // 从响应模型中获取响应实体
        HttpEntity entity = response.getEntity();
        System.out.println("结果：" + EntityUtils.toString(entity, "UTF-8"));

    }

}
