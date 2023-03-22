package cn.fch.buffetfood.service;

import cn.fch.buffetentity.entity.Detail;
import cn.fch.buffetentity.entity.Food;
import cn.fch.buffetentity.entity.ResponseBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: BuffetOrder
 * @description: 食物服务类
 * @CreatedBy: fch
 * @create: 2022-10-16 23:21
 **/
public interface FoodService {
    ResponseBean queryAllFoods();

    ResponseBean adminQueryAllFoods();

    boolean isExistsByFoodId(Food food);

    ResponseBean updateFoodImg(Food food, MultipartFile file, HttpServletRequest request);

    ResponseBean updateFood(Food food);

    ResponseBean addFood(Food food);

    ResponseBean queryAllDefault();

    ResponseBean updateDetail(Detail detail);

    ResponseBean addDetail(Detail detail);

    ResponseBean deleteDetail(Detail detail);
}
