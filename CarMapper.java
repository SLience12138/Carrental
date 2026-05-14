package org.example.mappers;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CarMapper {
    int saveCar(Map<String, Object> map);
    List<Map<String, Object>> findCars(Map<String, Object> map);
    int updateCar(Map<String, Object> map);
    int deleteCars(List<String> carIds);
}