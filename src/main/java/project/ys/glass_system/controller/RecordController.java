package project.ys.glass_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.ys.glass_system.config.Unlimited;
import project.ys.glass_system.model.dto.RetResponse;
import project.ys.glass_system.model.dto.RetResult;
import project.ys.glass_system.service.impl.RecordServiceImpl;

import javax.annotation.Resource;
import java.util.Map;

import static project.ys.glass_system.constant.HttpConstant.*;

@Unlimited
@RestController
@RequestMapping(RECORD)
public class RecordController {

    @Resource
    RecordServiceImpl recordService;

    @RequestMapping(LOGIN_RECORDS)
    @ResponseBody
    public RetResult<Map<String, Object>> loginRecords() {
        return RetResponse.makeOKRsp(recordService.topRecord(true));
    }

    @RequestMapping(SYNC_RECORDS)
    @ResponseBody
    public RetResult<Map<String, Object>> syncRecords() {
        return RetResponse.makeOKRsp(recordService.topRecord(false));
    }

}
