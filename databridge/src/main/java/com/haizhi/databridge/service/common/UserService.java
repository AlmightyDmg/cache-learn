package com.haizhi.databridge.service.common;

import static com.haizhi.databridge.constants.UserTypeConstants.ENTERPRISE_ROOT_USER_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.haizhi.databridge.bean.dto.UserBelongDto;
import com.haizhi.databridge.client.overlord.OverlordClient;
import com.haizhi.databridge.client.overlord.dto.OverlordResult;
import com.haizhi.databridge.client.overlord.request.InfoReq;
import com.haizhi.databridge.client.overlord.response.InfoResp;


@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private OverlordClient overlordClient;


	public InfoResp getUserInfo(String userId) {
		OverlordResult<InfoResp> info = overlordClient.info(
			InfoReq.builder().userId(userId).build()
		);
		LOGGER.info("get user info: " + userId);
		return info.getResult();
	}

	public boolean isRoot(String userId) {
		InfoResp userInfo = this.getUserInfo(userId);
		return isRoot(userInfo);
	}

	public boolean isRoot(InfoResp userInfo) {
		return userInfo.getRole().equals(ENTERPRISE_ROOT_USER_TYPE);
	}

	public List<String> getAllRoles(String userId) {
		InfoResp userInfo = this.getUserInfo(userId);
		List<String> allRoleIds = new ArrayList<>();
		for (HashMap<String, String> g : userInfo.getGroupList()) {
			allRoleIds.add(g.get("group_id"));
		}
		for (HashMap<String, String> c : userInfo.getChats()) {
			allRoleIds.add(c.get("chat_id"));
		}
		for (HashMap<String, String> r : userInfo.getRoles()) {
			allRoleIds.add(r.get("role_id"));
		}
		return allRoleIds;
	}

	/**
	 * 获取用户所属组织（包含组、角色、临时组）
	 * @param userId
	 * @return
	 */
	public UserBelongDto getUserBelong(String userId) {
		InfoResp userInfo = this.getUserInfo(userId);
		List<String> groups = userInfo.getGroupList().stream().filter(group -> group.containsKey("group_id")
			&& null != group.get("group_id")).map(group -> group.get("group_id")).collect(Collectors.toList());
		List<String> roles = userInfo.getRoles().stream().filter(role -> role.containsKey("role_id")
			&& null != role.get("role_id")).map(role -> role.get("role_id")).collect(Collectors.toList());
		List<String> chats = userInfo.getChats().stream().filter(chat -> chat.containsKey("chat_id")
			&& null != chat.get("chat_id")).map(chat ->  chat.get("chat_id")).collect(Collectors.toList());
		return UserBelongDto
			.builder()
			.userId(userId)
			.groups(groups)
			.roles(roles)
			.chats(chats)
			.build();
	}
}
