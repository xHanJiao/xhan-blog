package com.xhan.xhanblog.service;


import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.dto.UpdateUserDTO;
import com.xhan.xhanblog.entity.vo.UsernamePasswordToken;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface UserHelper {
    boolean tokenInvalid(UsernamePasswordToken token);

    TUser getUserByNameAndModifyIp(@Valid UsernamePasswordToken token, String ip);

    /**
     * 因为可以部分更新，所以不能保证所有的域都不为null，所以要全部校验
     * 现在controller里校验过了，至少有一个域不为null
     */
    void updateUser(UpdateUserDTO newInfo, Long uId);

    default String getSHA256StrJava(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 将byte转为16进制
     */
    default String byte2Hex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
