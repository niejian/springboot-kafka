package io.git.nj.message.service;

/**
 * @desc: cn.com.bluemoon.message.service.MsgService
 * @author: niejian9001@163.com
 * @date: 2020/12/4 16:36
 */
public interface MsgService {
    String sendMsg(String topic, String key, String content);
}
