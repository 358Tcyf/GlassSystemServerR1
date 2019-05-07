package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.config.Unlimited;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.service.t.impl.AutoProduceServiceImpl;

import javax.annotation.Resource;
import java.util.Map;

import static project.ys.glass_system.constant.HttpConstant.*;

@Unlimited
@RestController
@RequestMapping(GLASS)
public class GlassController {

    @Resource
    AutoProduceServiceImpl produceService;

    @RequestMapping(FACTORY_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> factoryQuery(int page, int limit) {
        return RetResponse.makeOKRsp(produceService.factoryQuery(page, limit));
    }
    @RequestMapping(PRODUCE_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> produceQuery(int page, int limit) {
        return RetResponse.makeOKRsp(produceService.produceQuery(page, limit));
    }
    @RequestMapping(GLASS_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> glassQuery(int page, int limit) {
        return RetResponse.makeOKRsp(produceService.glassQuery(page, limit));
    }
    @RequestMapping(WAREHOUSE_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> wareHouseQuery(int page, int limit) {
        return RetResponse.makeOKRsp(produceService.wareHouseQuery(page, limit));
    }
    @RequestMapping(TEST_QUERY)
    @ResponseBody
    public RetResult<Map<String, Object>> testQuery(int page, int limit) {
        return RetResponse.makeOKRsp(produceService.testQuery(page, limit));
    }
}
