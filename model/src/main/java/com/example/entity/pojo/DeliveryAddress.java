package com.example.entity.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("DeliveryAddress")
public class DeliveryAddress {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String tag;

    private String address;

    private String name;

    private String phone;

    private Boolean isDeleted;

}
