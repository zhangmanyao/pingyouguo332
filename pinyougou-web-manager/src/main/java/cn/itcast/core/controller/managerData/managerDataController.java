package cn.itcast.core.controller.managerData;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("managerData")
public class managerDataController {

    @RequestMapping("searchManagerDataVo.do")
    public Map searchManagerData(){
        return null;
    }
}
