package com.search.controller;

import com.search.biz.SelectValueService;
import com.search.common.controller.BaseController;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysTopicService;
import com.search.vo.QueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.search.common.utils.R;

/**
 * ${functionName} 提供者
 *
 * @author chenshun
 * @date 2020-12-20 11:36:10
 */
@RestController
@RequestMapping("/getSelect")
public class SelectValueController extends BaseController {

	@Autowired
	SelectValueService selectValueService;



	@PostMapping("/getAllData")
	@ResponseBody
	public R getSelectValueService(){
		return selectValueService.getSelectValue();
	}

	@PostMapping("/getOverviewData")
	@ResponseBody
	public R getOverviewData(@RequestBody QueryVO queryVO){
		return selectValueService.getOverviewData(queryVO);
	}

}
