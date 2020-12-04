package io.git.nj.message.utils;

import io.git.nj.message.vo.UserVo;

import java.util.Random;

/**
 * @desc: cn.com.bluemoon.message.utils.UserUtils
 * @author: niejian9001@163.com
 * @date: 2020/11/28 12:02
 */
public class UserUtils {

    private static String[] names = {"张三", "李四", "王五", "赵柳", "里思"};
    private static String[] address = {"北京", "上海", "深圳", "广州", "武汉"};


    public UserVo generateUser() {
        Integer age = getRandomInt();
        int namesLen = names.length;
        int addressLen = address.length;
        String name = names[getRandomInt() % namesLen];
        String addres = address[getRandomInt() % addressLen];
        UserVo userVo = UserVo.builder()
                .address(addres)
                .age(age)
                .userCode(System.currentTimeMillis() + "")
                .nickName("a")
                .build();
        return userVo;
    }

    private Integer getRandomInt() {
        Random random = new Random();
        return random.nextInt(30);
    }

}
