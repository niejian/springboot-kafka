package io.git.nj.message.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @desc: cn.com.bluemoon.message.vo.UserVo
 * @author: niejian9001@163.com
 * @date: 2020/11/28 12:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo implements Serializable {
    private String userName;
    private String userCode;
    private Integer age;
    private String address;
    private String nickName;
}
