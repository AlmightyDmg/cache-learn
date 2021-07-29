package com.haizhi.databridge.service.common;

import static com.haizhi.databridge.constants.FrontConstants.COMMON_YES;
import static com.haizhi.databridge.constants.FrontConstants.PARAM_LIMIT;
import static com.haizhi.databridge.constants.FrontConstants.PARAM_OFFSET;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.haizhi.databridge.client.behemoth.BehemothClient;
import com.haizhi.databridge.client.behemoth.request.ChatListReq;
import com.haizhi.databridge.client.behemoth.request.RoleListReq;
import com.haizhi.databridge.client.behemoth.response.BehemothResult;
import com.haizhi.databridge.client.behemoth.response.ChatListResp;
import com.haizhi.databridge.client.behemoth.response.RoleListResp;
import com.haizhi.databridge.client.overlord.OverlordClient;
import com.haizhi.databridge.client.overlord.dto.OverlordResult;
import com.haizhi.databridge.client.overlord.request.GroupInfosReq;
import com.haizhi.databridge.client.overlord.request.ListReq;
import com.haizhi.databridge.client.overlord.response.GroupInfosResp;
import com.haizhi.databridge.client.overlord.response.ListResp;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月25日 11:52:51
 */

@Component
@Service
@Log4j2
public class PermissionService extends RequestCommonData {
    @Autowired
    private OverlordClient overlordClient;

    @Autowired
    private BehemothClient behemothClient;

    public void getUserInfoByIds(Map<String, String> roleId2Name, List<String> userIds, String roleId) {
        if (!userIds.isEmpty()) {
            Map<String, Object> queryuserInfo = new HashMap<>();
            queryuserInfo.put("user_id", userIds);
            OverlordResult<List<ListResp>> userListQuery = overlordClient.list(
                    ListReq.builder().userId(roleId).offset(PARAM_OFFSET).limit(PARAM_LIMIT)
                            .anonymous(COMMON_YES).filters(JsonUtils.toJson(queryuserInfo)).build());
            List<ListResp> userList = userListQuery.getResult();
            roleId2Name.putAll(userList.stream().collect(Collectors.toMap(ListResp::getUserId, ListResp::getName)));
        }
    }

    public void getGroupInfoByIds(Map<String, String> roleId2Name, List<String> groupIds, String roleId) {
        if (!groupIds.isEmpty()) {
            OverlordResult<List<GroupInfosResp>> groupListResult = overlordClient.groupInfos(
                    GroupInfosReq.builder().userId(roleId).groupIdList(JsonUtils.list2Json(groupIds)).build());
            List<GroupInfosResp> groupList = groupListResult.getResult();
            roleId2Name.putAll(groupList.stream().collect(Collectors.toMap(GroupInfosResp::getGroupId, GroupInfosResp::getGroupName)));
        }
    }

    public void getRoleInfoByIds(Map<String, String> roleId2Name, List<String> roleIds) {
        if (!roleIds.isEmpty()) {
            List<RoleListResp> userList = behemothClient.innerRoleList(this.getUserId()).getResult()
                    .stream().filter(x -> roleIds.contains(x.getRoleId())).collect(Collectors.toList());

            roleId2Name.putAll(userList.stream().collect(Collectors.toMap(RoleListResp::getRoleId, RoleListResp::getRoleName)));
        }
    }
    public void getChatInfoByIds(Map<String, String> roleId2Name, List<String> chatIds) {
        if (!chatIds.isEmpty()) {
            List<ChatListResp> userList = behemothClient.innerChatList(this.getUserId()).getResult()
                    .stream().filter(x -> chatIds.contains(x.getChatId())).collect(Collectors.toList());

            roleId2Name.putAll(userList.stream().collect(Collectors.toMap(ChatListResp::getChatId, ChatListResp::getChatName)));
        }
    }
}
