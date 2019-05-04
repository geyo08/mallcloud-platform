package com.mallplus.marking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.central.common.annotation.SysLog;
import com.central.common.utils.CommonResult;
import com.mallplus.marking.entity.SmsRedPacket;
import com.mallplus.marking.entity.SmsUserRedPacket;
import com.mallplus.marking.service.ISmsCouponService;
import com.mallplus.marking.service.ISmsRedPacketService;
import com.mallplus.marking.service.ISmsUserRedPacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 优惠卷表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "MarkingController", description = "")
@RequestMapping("/api/marking")
public class MarkingController {
    @Resource
    private ISmsCouponService couponService;

    @Resource
    private ISmsRedPacketService redPacketService;
    @Resource
    private ISmsUserRedPacketService userRedPacketService;



    @SysLog(MODULE = "marking", REMARK = "根据条件查询所有红包列表")
    @ApiOperation("根据条件查询所有红包列表")
    @GetMapping(value = "/redPacket/list")
    public Object getSmsRedPacketByPage(SmsRedPacket entity) {
        try {
            List<SmsRedPacket> redPacketList = redPacketService.list(new QueryWrapper<>());
            SmsUserRedPacket userRedPacket = new SmsUserRedPacket();
            userRedPacket.setUserId(entity.getUserId());
            List<SmsUserRedPacket> list = userRedPacketService.list(new QueryWrapper<>(userRedPacket));
            for(SmsRedPacket vo : redPacketList){
                if (list!=null && list.size()>0){
                    for (SmsUserRedPacket vo1 : list){
                        if(vo.getId().equals(vo1.getRedPacketId())){
                            vo.setStatus(1);
                            vo.setReciveAmount(vo1.getAmount());
                            break;
                        }
                    }
                }
            }
            return new CommonResult().success(redPacketList);
        } catch (Exception e) {
            log.error("根据条件查询所有红包列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
    @SysLog(MODULE = "marking", REMARK = "根据条件查询所有红包列表")
    @ApiOperation("根据条件查询所有红包列表")
    @GetMapping(value = "/coupon/list")
    public Object getCouponByPage(SmsRedPacket entity) {
        try {
            return new CommonResult().success(couponService.selectNotRecive());
        } catch (Exception e) {
            log.error("根据条件查询所有红包列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }
}
