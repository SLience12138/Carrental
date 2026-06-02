package org.example.service;

import jakarta.annotation.Resource;
import org.example.mappers.AdminMapper;
import org.example.util.ResultJson;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;
    public ResultJson login(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            String username = map.get("username");
            String password = map.get("password");
            if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
                resultJson.fail("用户名和密码不能为空");
                return resultJson;
            }
            Map<String, Object> admin = adminMapper.login(map);
            if (admin != null && !admin.isEmpty()) {
                admin.remove("password");
                resultJson.success(admin);
            } else {
                resultJson.fail("用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
    public ResultJson findAll() {
        ResultJson resultJson = new ResultJson();
        try {
            List<Map<String, Object>> users = adminMapper.findAll();
            resultJson.success(users);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
    public ResultJson updateUserLevel(Map<String, Object> params) {
        ResultJson resultJson = new ResultJson();
        try {
            String userIdStr = String.valueOf(params.get("user_id"));
            String userLevel = String.valueOf(params.get("user_level"));
            if (!StringUtils.hasText(userIdStr) || !StringUtils.hasText(userLevel)) {
                resultJson.fail("参数错误");
                return resultJson;
            }
            int row = adminMapper.updateUserLevel(params);
            if (row > 0) {
                resultJson.success("用户等级更新成功");
            } else {
                resultJson.fail("用户等级更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
    public ResultJson updateStatus(Map<String, Object> params) {
        ResultJson resultJson = new ResultJson();
        try {
            String userIdStr = String.valueOf(params.get("user_id"));
            String status = String.valueOf(params.get("status"));
            if (!StringUtils.hasText(userIdStr) || !StringUtils.hasText(status)) {
                resultJson.fail("参数错误");
                return resultJson;
            }
            int row = adminMapper.updateStatus(params);
            if (row > 0) {
                resultJson.success("用户状态更新成功");
            } else {
                resultJson.fail("用户状态更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
    public ResultJson delete(Long id) {
        ResultJson resultJson = new ResultJson();
        try {
            if (id == null || id <= 0) {
                resultJson.fail("用户ID不能为空");
                return resultJson;
            }
            int row = adminMapper.delete(id);
            if (row > 0) {
                resultJson.success("用户删除成功");
            } else {
                resultJson.fail("用户删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
}