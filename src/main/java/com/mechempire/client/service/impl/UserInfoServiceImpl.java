package com.mechempire.client.service.impl;

import com.mechempire.client.model.UserInfo;
import com.mechempire.client.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * package: com.mechempire.client.service.impl
 *
 * @author <tairy> tairyguo@gmail.com
 * @date 2020/12/18 下午2:54
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public UserInfo getUserInfo() {
        return new UserInfo(1L);
    }
}