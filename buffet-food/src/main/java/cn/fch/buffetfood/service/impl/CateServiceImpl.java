package cn.fch.buffetfood.service.impl;

import cn.fch.buffetentity.entity.Cate;
import cn.fch.buffetentity.entity.ResponseBean;
import cn.fch.buffetcommon.aspect.AfterClearCache;
import cn.fch.buffetcommon.aspect.BeforeClearCache;
import cn.fch.buffetcommon.aspect.Cache;
import cn.fch.buffetfood.dao.CateDao;
import cn.fch.buffetfood.service.CateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: BuffetOrder
 * @description: 食物分类服务接口实现
 * @CreatedBy: fch
 * @create: 2022-10-15 16:45
 **/
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CateServiceImpl implements CateService {

    public static final String ALL_CATES_KEY = "buffetorder:cate:allcates";

    private final CateDao cateDao;

    /**
     * 查询所有食物分类
     *
     * @return List<Cate>
     */
    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Cache(key = ALL_CATES_KEY)
    public ResponseBean queryAllCates() {
//        String catesJson = Optional
//                .ofNullable(redisUtil.getStr(ALL_CATES_KEY))
//                .orElseGet(() -> {
//                    String json = JSONObject.toJSONString(cateMapper.queryAllCates());
//                    redisUtil.setStr(ALL_CATES_KEY, json, 1000 * 60 * 60);
//                    return json;
//                });

        return ResponseBean.ok(cateDao.queryAllCates());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ResponseBean adminQueryAllCates() {
        return ResponseBean.ok(cateDao.adminQueryAllCates());
    }

    @Override
    @BeforeClearCache(key = ALL_CATES_KEY)
    @AfterClearCache(key = FoodServiceImpl.ALL_FOODS_KEY)
    public ResponseBean updateCate(Cate cate) {
        return cateDao.updateCate(cate) > 0 ? ResponseBean.ok(cate, "修改成功") : ResponseBean.badRequest("修改失败");
    }

    @Override
    @BeforeClearCache(key = ALL_CATES_KEY)
    @AfterClearCache(key = FoodServiceImpl.ALL_FOODS_KEY)
    public ResponseBean addCate(Cate cate) {
        return cateDao.insertCate(cate) > 0 ? ResponseBean.ok(cate, "添加成功") : ResponseBean.badRequest("添加失败");
    }
}
