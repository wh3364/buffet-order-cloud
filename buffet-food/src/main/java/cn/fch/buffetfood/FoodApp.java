package cn.fch.buffetfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author fch
 * @program BuffetOrderCloud
 * @description FoodSpringBoot启动类
 * @create 2023-03-21 17:20
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class FoodApp {
    public static void main(String[] args) {
        SpringApplication.run(FoodApp.class, args);
    }
}
