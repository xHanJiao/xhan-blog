package com.xhan.xhanblog.service.impl;

import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.dao.TUserExample;
import com.xhan.xhanblog.entity.dto.UpdateUserDTO;
import com.xhan.xhanblog.entity.vo.UsernamePasswordToken;
import com.xhan.xhanblog.exception.ErrorCredentialException;
import com.xhan.xhanblog.exception.UpdateFailException;
import com.xhan.xhanblog.repository.TUserMapper;
import com.xhan.xhanblog.service.UserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
public class UserHelperImpl implements UserHelper {

    private final TUserMapper userMapper;

    @Autowired
    public UserHelperImpl(TUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public boolean tokenInvalid(UsernamePasswordToken token) {
        if (!hasText(token.getUsername()) || !hasText(token.getPassword()))
            return true;
        TUserExample example = new TUserExample();
        example.createCriteria()
                .andUserNameEqualTo(token.getUsername())
                .andPasswordEqualTo(encryptPassword(token.getPassword()));
        List<TUser> users = userMapper.selectByExample(example);
        // 因为username上有唯一性约束，所以结果列表只可能有一个或0个
        return users.size() != 1;
    }

    @Override
    public TUser getUserByNameAndModifyIp(@Valid UsernamePasswordToken token, String ip) {
        TUserExample example = new TUserExample();
        example.createCriteria()
                .andUserNameEqualTo(token.getUsername());
        List<TUser> users = userMapper.selectByExample(example);

        if (users.size() != 1)
            throw new ErrorCredentialException();

        users.get(0).setLastRegisterIp(ip);
        userMapper.updateByExampleSelective(users.get(0), example);
        return users.get(0);
    }

    @Override
    @Transactional(rollbackFor = UpdateFailException.class)
    public void updateUser(UpdateUserDTO newInfo, Long uId) {
        TUser user = userMapper.selectByPrimaryKey(uId);
        if (user == null) throw new UpdateFailException("user is null, impossible!");

        TUser toModify = new TUser(user.getUId());

        if (hasText(newInfo.getEmail()))
            toModify.setEmail(newInfo.getEmail());

        if (hasText(newInfo.getPassword())) {
            String encrypt = encryptPassword(newInfo.getPassword());
            toModify.setPassword(encrypt);
        }

        if (hasText(newInfo.getUsername()))
            toModify.setUserName(newInfo.getUsername());

        if (userMapper.updateByPrimaryKeySelective(toModify) != 1)
            throw new UpdateFailException("update user fail");
    }

    private String encryptPassword(String source) {
        return getSHA256StrJava(source);
    }

}
