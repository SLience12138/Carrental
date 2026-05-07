package org.example.service;

import jakarta.annotation.Resource;
import org.example.mappers.OrderMapper;
import org.example.util.ResultJson;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;

    public ResultJson findAll() {
        ResultJson resultJson = new ResultJson();
        try {
            List<Map<String, Object>> orders = orderMapper.findAll();
            resultJson.success(orders);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }

    public ResultJson findOrders(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            Map<String, Object> params = new HashMap<>(map);

            // 处理订单ID参数
            if (map.containsKey("order_id")) {
                String orderId = map.get("order_id");
                if (StringUtils.hasText(orderId)) {
                    params.put("order_id", orderId);
                }
            }

            // 处理客户姓名参数
            if (map.containsKey("customer_name")) {
                String customerName = map.get("customer_name");
                if (StringUtils.hasText(customerName)) {
                    params.put("customer_name", customerName);
                }
            }

            // 处理状态参数
            if (map.containsKey("status")) {
                String status = map.get("status");
                if (StringUtils.hasText(status)) {
                    params.put("status", status);
                }
            }

            List<Map<String, Object>> orders = orderMapper.findOrders(params);
            resultJson.success(orders);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }


    public ResultJson findById(String orderId) {
        ResultJson resultJson = new ResultJson();
        try {
            if (!StringUtils.hasText(orderId)) {
                resultJson.fail("订单ID不能为空");
                return resultJson;
            }

            Map<String, Object> order = orderMapper.findById(orderId); // 修复类型不匹配
            if (order != null) {
                resultJson.success(order);
            } else {
                resultJson.fail("订单不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
    public ResultJson saveOrder(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            System.out.println("📝 接收到订单数据：" + map);

            // 验证必填字段
            if (!StringUtils.hasText(map.get("order_id"))) {
                resultJson.fail("订单编号不能为空");
                return resultJson;
            }

            if (!StringUtils.hasText(map.get("user_id"))) {
                resultJson.fail("用户ID不能为空");
                return resultJson;
            }

            if (!StringUtils.hasText(map.get("car_id"))) {
                resultJson.fail("车辆ID不能为空");
                return resultJson;
            }

            if (!StringUtils.hasText(map.get("customer_name"))) {
                resultJson.fail("客户姓名不能为空");
                return resultJson;
            }

            if (!StringUtils.hasText(map.get("customer_phone"))) {
                resultJson.fail("客户电话不能为空");
                return resultJson;
            }

            if (!StringUtils.hasText(map.get("rent_start"))) {
                resultJson.fail("租赁开始时间不能为空");
                return resultJson;
            }

            if (!StringUtils.hasText(map.get("rent_end"))) {
                resultJson.fail("租赁结束时间不能为空");
                return resultJson;
            }

            // 构建数据库参数
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("order_id", map.get("order_id"));
            orderInfo.put("user_id", map.get("user_id"));
            orderInfo.put("car_id", map.get("car_id"));
            orderInfo.put("rent_start", map.get("rent_start"));
            orderInfo.put("rent_end", map.get("rent_end"));
            orderInfo.put("total_rent", map.get("total_rent"));
            orderInfo.put("order_status", map.containsKey("order_status") ?
                    map.get("order_status") : "待处理");
            orderInfo.put("customer_name", map.get("customer_name"));
            orderInfo.put("customer_phone", map.get("customer_phone"));
            orderInfo.put("days", map.get("days") != null ? map.get("days") : "1");

            // 自动设置创建时间
            orderInfo.put("create_time", new java.sql.Timestamp(System.currentTimeMillis()));

            System.out.println("📝 准备插入的订单数据：" + orderInfo);

            int row = orderMapper.saveOrder(orderInfo);
            if (row > 0) {
                Map<String, Object> carUpdate = new HashMap<>();
                carUpdate.put("car_id", map.get("car_id")); // 关联订单的车辆ID
                carUpdate.put("status", "已租赁"); // 对应cars表的status字段

                System.out.println("✅ 订单创建成功，影响行数：" + row);
                resultJson.success("订单创建成功");
            } else {
                System.out.println("❌ 订单创建失败，影响行数：" + row);
                resultJson.fail("订单创建失败");
            }
        } catch (Exception e) {
            System.err.println("❌ 保存订单时发生异常：" + e.getMessage());
            e.printStackTrace();
            resultJson.fail("服务器内部错误：" + e.getMessage());
        }
        return resultJson;
    }
    public ResultJson updateStatus(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            String orderId = map.get("order_id");
            String status = map.get("status");
            if (!StringUtils.hasText(orderId)) {
                resultJson.fail("订单编号不能为空");
                return resultJson;
            }
            if (!StringUtils.hasText(status)) {
                resultJson.fail("订单状态不能为空");
                return resultJson;
            }
            Map<String, Object> params = new HashMap<>(map);
            int row = orderMapper.updateStatus(params);
            if (row > 0) {
                resultJson.success("订单状态更新成功");
            } else {
                resultJson.fail("订单状态更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }

    public ResultJson delete(String id) {
        ResultJson resultJson = new ResultJson();
        try {
            if (!StringUtils.hasText(id)) {
                resultJson.fail("订单ID不能为空");
                return resultJson;
            }
            int row = orderMapper.delete(id);
            if (row > 0) {
                resultJson.success("订单删除成功");
            } else {
                resultJson.fail("订单删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }

}