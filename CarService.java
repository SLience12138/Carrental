package org.example.service;

import jakarta.annotation.Resource;
import org.example.mappers.CarMapper;
import org.example.util.ResultJson;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarService {

    @Resource
    private CarMapper carMapper;

    public ResultJson findCars(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            Map<String, Object> params = new HashMap<>(map);
            if (map.containsKey("minPrice")) {
                try {
                    params.put("minPrice", Double.parseDouble(map.get("minPrice")));
                } catch (NumberFormatException e) {
                    resultJson.fail("最低价格格式错误");
                    return resultJson;
                }
            }
            if (map.containsKey("maxPrice")) {
                try {
                    params.put("maxPrice", Double.parseDouble(map.get("maxPrice")));
                } catch (NumberFormatException e) {
                    resultJson.fail("最高价格格式错误");
                    return resultJson;
                }
            }
            List<Map<String, Object>> cars = carMapper.findCars(params);
            for (int i = 0; i < cars.size(); i++) {
                Map<String, Object> car = cars.get(i);
                System.out.println("第 " + (i+1) + " 辆车:");
                System.out.println("  字段列表: " + car.keySet());
                System.out.println("  详细数据: " + car);
            }
            System.out.println("ResultJson对象: code=" + resultJson.getCode()
                    + ", msg=" + resultJson.getMsg()
                    + ", data类型=" + (resultJson.getData() != null ? resultJson.getData().getClass().getName() : "null"));
            resultJson.success(cars);
        } catch (Exception e) {
            e.printStackTrace();
            resultJson.fail("查询失败: " + e.getMessage());
        }
        return resultJson;
    }

    public ResultJson saveCar(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            String brand = map.get("brand");
            if (!StringUtils.hasText(brand)) {
                resultJson.fail("车辆名称不能为空");
                return resultJson;
            }
            Map<String, Object> carInfo = new HashMap<>(map);
            if (map.containsKey("daily_rent")) {
                try {
                    carInfo.put("daily_rent", Double.parseDouble(map.get("daily_rent")));
                } catch (NumberFormatException e) {
                    resultJson.fail("车辆价格格式错误");
                    return resultJson;
                }
            }
            if (!carInfo.containsKey("status")) {
                carInfo.put("status", "0");
            }
            if (!carInfo.containsKey("create_time")) {
                carInfo.put("create_time", new java.util.Date());
            }
            if (!carInfo.containsKey("update_time")) {
                carInfo.put("update_time", new java.util.Date());
            }
            int row = carMapper.saveCar(carInfo);
            if (row > 0) {
                resultJson.success("车辆添加成功");
            } else {
                resultJson.fail("车辆添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultJson.fail("添加车辆失败：" + e.getMessage());
        }
        return resultJson;
    }

    public ResultJson updateCar(Map<String, String> map) {
        ResultJson resultJson = new ResultJson();
        try {
            String carId = map.get("car_id") != null ? map.get("car_id") : map.get("id");
            if (!StringUtils.hasText(carId)) {
                resultJson.fail("车辆ID不能为空");
                return resultJson;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("car_id", carId);
            if (map.containsKey("brand") || map.containsKey("carName")) {
                params.put("brand", map.get("carName") != null ? map.get("carName") : map.get("brand"));
            }
            if (map.containsKey("model") || map.containsKey("carType")) {
                params.put("model", map.get("carType") != null ? map.get("carType") : map.get("model"));
            }
            if (map.containsKey("daily_rent") || map.containsKey("carPrice")) {
                String priceStr = map.get("carPrice") != null ? map.get("carPrice") : map.get("daily_rent");
                try {
                    params.put("daily_rent", Double.parseDouble(priceStr));
                } catch (NumberFormatException e) {
                    resultJson.fail("价格格式错误");
                    return resultJson;
                }
            }
            if (map.containsKey("status") || map.containsKey("carStatus")) {
                params.put("status", map.get("carStatus") != null ? map.get("carStatus") : map.get("status"));
            }

            int row = carMapper.updateCar(params);
            if (row > 0) {
                resultJson.success("车辆信息更新成功");
            } else {
                resultJson.fail("车辆信息更新失败，可能车辆不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultJson.fail("更新车辆失败：" + e.getMessage());
        }
        return resultJson;
    }

    public ResultJson deleteCars(List<String> carIds) {
        ResultJson resultJson = new ResultJson();
        try {
            if (carIds == null || carIds.isEmpty()) {
                resultJson.fail("请选择要删除的车辆");
                return resultJson;
            }

            int row = carMapper.deleteCars(carIds);
            if (row > 0) {
                resultJson.success("成功删除 " + row + " 辆车");
            } else {
                resultJson.fail("删除车辆失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultJson;
    }
}



