package com.search.controller;

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
@RequestMapping("SysTopic")
public class SysTopicController extends BaseController {

	@Autowired
	private ISysTopicService SysTopicService;

//	/**
//	 * 查询${functionName}
//	 */
//	@GetMapping("get/{${pkColumn.javaField}}")
//	public SysTopicEntity get(@PathVariable("${pkColumn.javaField}")${pkColumn.javaType} ${pkColumn.javaField}) {
//		return SysTopicService.select${ClassName}ById(${pkColumn.javaField});
//
//	}
//
//	/**
//	 * 查询${functionName}列表
//	 */
//	@GetMapping("list")
//	public R list(SysTopicEntity sysTopicEntity) {
//		startPage();
//        return R.ok(SysTopicService.select${ClassName}List(SysTopic));
//	}
//
//
//	/**
//	 * 新增保存${functionName}
//	 */
//	@PostMapping("save")
//	public R addSave(@RequestBody ${ClassName} SysTopic) {
//		return R.ok(SysTopicService.insert${ClassName}(SysTopic));
//	}
//
//	/**
//	 * 修改保存${functionName}
//	 */
//	@PostMapping("update")
//	public R editSave(@RequestBody ${ClassName} SysTopic) {
//		return R.ok(SysTopicService.update${ClassName}(SysTopic));
//	}
//
//	/**
//	 * 删除${functionName},默认不启用
//	 */
//	// @PostMapping("remove")
//	public R remove(String ids) {
//		return R.ok(SysTopicService.delete${ClassName}ByIds(ids));
//	}

}
