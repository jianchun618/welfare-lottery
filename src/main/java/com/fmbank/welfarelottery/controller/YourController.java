package com.fmbank.welfarelottery.controller;

import com.alibaba.fastjson.JSON;
import com.fmbank.welfarelottery.config.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class YourController {

    @Autowired
    private WebSocket webSocket;

    @PostMapping("/sendNotification")
    public void sendNotification() {
        try {
         /*   // 创建业务消息信息
            String message = "测试消息的发送";

            // 全体发送
            webSocket.sendAllMessage(message);
*/
            // 单个用户发送 (userId为用户id)
            String userId = "1";

            String message1 = "【websocket消息】 单点消息:只发送给id为" + userId + "的用户。";
            webSocket.sendOneMessage(userId, message1);

            // 多个用户发送 (userIds为多个用户id，逗号‘,’分隔)
        /*    String[] userIds = {"1", "2"};
            String message2 = "【websocket消息】 单点消息:只发送给id为" + JSON.toJSONString(userIds) + "的用户。";
            webSocket.sendMoreMessage(userIds, message2);*/
        } catch (Exception e) {
            // 输出异常信息
            e.printStackTrace();
        }
    }

}
