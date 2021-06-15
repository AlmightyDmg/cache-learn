package com.haizhi.databridge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.haizhi.databridge.bean.dto.UserBelongDto;
import com.haizhi.databridge.client.overlord.response.InfoResp;
import com.haizhi.databridge.constants.DatabridgeConstants;
import com.haizhi.databridge.service.common.UserService;



/**
 * 请求上线文中的公共数据
 * 依赖Spring容器，主要给Service和Controller用
 */
public abstract class RequestCommonData {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestCommonData.class);

    @Autowired
    private UserService userService;

    /**
     * 请求是否来自DMC模块
     *
     * @return
     */
    public boolean isDmcRequest() {
        String dmcRequest = MDC.get(DatabridgeConstants.DMC_REQUEST);
        return "1".equals(dmcRequest);
    }

    /**
     * 访问系统的真实用户id
     */
    public String getUserId() {
        return MDC.get(DatabridgeConstants.USER_ID);
    }

    /**
     * 业务用户id，如果在DMC模块，则为ENTERPRISE_USER = domain
     */
    public String getOwner() {
//    	return "domain";
        return isDmcRequest() ? DatabridgeConstants.ENTERPRISE_USER : getUserId();
    }

    /**
     * 当前请求所属企业域ID
     */
    public String getEnterpriseId() {
		String entId = null;
		entId = MDC.get(DatabridgeConstants.ENTERPRISE_ID);
        if (!StringUtils.isEmpty(entId)) {
            return entId;
        }

		if ("1".equals(MDC.get(DatabridgeConstants.INNER_CALL))) {
			entId = MDC.get(DatabridgeConstants.DMC_ENT_ID);
		}
		if (!StringUtils.isEmpty(entId)) {
			return entId;
		}

        String sessionId = MDC.get(DatabridgeConstants.SESSION_ID);
		if (!StringUtils.isEmpty(sessionId)) {
			entId = SessionUtils.getEnterpriseId(sessionId);
		}
		if (!StringUtils.isEmpty(entId)) {
			return entId;
		}

		InfoResp ui = userService.getUserInfo(getUserId());
		entId = ui.getEnterpriseId();

        MDC.put(DatabridgeConstants.ENTERPRISE_ID, entId);
        return entId;
    }

    public String getRole() {
        String cacheRole = MDC.get(DatabridgeConstants.ROLE);
        if (!StringUtils.isEmpty(cacheRole)) {
            return cacheRole;
        }
        String role = null;
        InfoResp ui = userService.getUserInfo(getUserId());
        role = String.valueOf(ui.getRole());
        MDC.put(DatabridgeConstants.ROLE, role);
        return role;
    }

    /**
     * 获取当前请求用户的UserBelong信息
     *
     * @return
     */
    public UserBelongDto getUserBelong() {
//        return UserBelongDto.builder().build();
        String cachedUserBelong = MDC.get(DatabridgeConstants.USER_BELONG);
        if (!StringUtils.isEmpty(cachedUserBelong)) {
            return JsonUtils.toObject(cachedUserBelong, UserBelongDto.class);
        }

        UserBelongDto userBelongDto = userService.getUserBelong(getUserId());
        MDC.put(DatabridgeConstants.USER_BELONG, JsonUtils.toJson(userBelongDto));
        return userBelongDto;
    }

    public String getSessionId() {
        return MDC.get(DatabridgeConstants.SESSION_ID);
    }
    
	/**
	 * 获取本次请求的 traceId
	 */
	public String getTraceId() {
		return MDC.get(DatabridgeConstants.TRACE_ID);
	}
	
}
