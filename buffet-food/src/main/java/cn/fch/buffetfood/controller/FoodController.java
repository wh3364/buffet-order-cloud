package cn.fch.buffetfood.controller;

import cn.fch.buffetentity.entity.ResponseBean;
import cn.fch.buffetfood.service.CateService;
import cn.fch.buffetfood.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: BuffetOrder
 * @description: 顾客请求的食物接口
 * @CreatedBy: fch
 * @create: 2022-10-15 16:47
 **/
@RestController
@RequestMapping("Food")
@RequiredArgsConstructor
public class FoodController {

    private final CateService cateService;

    private final FoodService foodService;

    @GetMapping("GetAllCate")
    public ResponseBean getAllCate() {
        return cateService.queryAllCates();
    }

    @GetMapping("GetAllFood")
    public ResponseBean getAllFoots(){
        return foodService.queryAllFoods();
    }
}