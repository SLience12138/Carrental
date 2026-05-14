package org.example.controller;

import jakarta.annotation.Resource;
import org.example.service.CarService;
import org.example.util.ResultJson;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Resource
    private CarService carService;

    @PostMapping("/saveCar")
    public ResultJson saveCar(@RequestBody Map<String, String> map) {
        return carService.saveCar(map);
    }

    @PostMapping ("/findCars")
    public ResultJson findCars(@RequestBody Map<String, String > map) {
        return carService.findCars(map);
    }


    @PostMapping("/updateCar")
    public ResultJson updateCar(@RequestBody Map<String, String > map) {
        return carService.updateCar(map);
    }

    @PostMapping("/deleteCars")
    public ResultJson deleteCars(@RequestBody List<String> carIds) {
        return carService.deleteCars(carIds);
    }
}