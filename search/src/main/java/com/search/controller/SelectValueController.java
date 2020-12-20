package com.search.controller;

import com.search.biz.SelectValueService;
import com.search.common.controller.BaseController;
import com.search.entity.SysTopicEntity;
import com.search.service.ISysTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public R getSelectValueService(){
		return selectValueService.getSelectValue();
	}

}
