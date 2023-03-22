package cn.fch.buffetentity.entity;

import lombok.Data;

/**
 * @program: BuffetOrder
 * @description:
 * @CreatedBy: fch
 * @create: 2022-10-31 17:56
 **/
@Data
public class Admin {
    private Integer adminId;
    private String username;
    private String password;
    private String avatar;
    private String role;
    private Integer isEnable;
}
