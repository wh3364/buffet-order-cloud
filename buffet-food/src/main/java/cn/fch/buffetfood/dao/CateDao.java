package cn.fch.buffetfood.dao;

import cn.fch.buffetentity.entity.Cate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: BuffetOrder
 * @description: 食物分类Mapper
 * @CreatedBy: fch
 * @create: 2022-10-15 16:39
 **/
@Mapper
public interface CateDao {
    List<Cate> queryAllCates();

    List<Cate> adminQueryAllCates();

    int updateCate(Cate cate);

    int insertCate(Cate cate);
}
