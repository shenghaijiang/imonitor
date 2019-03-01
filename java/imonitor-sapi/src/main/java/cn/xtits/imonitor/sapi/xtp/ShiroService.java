package cn.xtits.imonitor.sapi.xtp;

import cn.xtits.xtf.common.web.AjaxResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @fileName:
 * @author: HaiL
 * @createDate: 2018-04-08 16:26:33
 * @description:
 */
@FeignClient("XTP-API-SERVICE")
@RequestMapping("/shiro")
public interface ShiroService {

    @RequestMapping(value = "listMenuWithOperationByUserId")
    AjaxResult listMenuWithOperationByUserId(
            @RequestParam(value = "userId", required = true) Integer userId,
            @RequestParam("oauth") String oauth);
}
