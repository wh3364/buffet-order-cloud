package cn.fch.buffetuser.dao;

import cn.fch.buffetentity.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: BuffetOrder
 * @description:
 * @CreatedBy: fch
 * @create: 2022-10-30 23:09
 **/
@Mapper
public interface AddressDao {

    int addAddress(Address address);

    int uploadAddress(Address address);

    Address queryAddressByUserId(Address address);
}
