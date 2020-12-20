package com.search.controller;


import com.search.common.controller.BaseController;
import com.search.service.ISysContentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.search.common.utils.R;

/**
 *
 * @date 2020-12-20 11:36:10
 */
@RestController
@RequestMapping("SysContentType")
public class SysContentTypeController extends BaseController {
//
	@Autowired
	private ISysContentTypeService sysContentTypeService;
//
	/**
	 * 查询${functionName}
	 */
//	@GetMapping("get/contentType")
//	public List<> get() {
//		return sysContentTypeService.select${ClassName}ById(${pkColumn.javaField});
//
//	}
//
//	/**
//	 * 查询${functionName}列表
//	 */
//	@GetMapping("list")
//	public R list(${ClassName} SysContentType) {
//		startPage();
//        return R.ok(SysContentTypeService.select${ClassName}List(SysContentType));
//	}
//
//
//	/**
//	 * 新增保存${functionName}
//	 */
//	@PostMapping("save")
//	public R addSave(@RequestBody ${ClassName} SysContentType) {
//		return R.ok(SysContentTypeService.insert${ClassName}(SysContentType));
//	}
//
//	/**
//	 * 修改保存${functionName}
//	 */
//	@PostMapping("update")
//	public R editSave(@RequestBody ${ClassName} SysContentType) {
//		return R.ok(SysContentTypeService.update${ClassName}(SysContentType));
//	}
//
//	/**
//	 * 删除${functionName},默认不启用
//	 */
//	// @PostMapping("remove")
//	public R remove(String ids) {
//		return R.ok(SysContentTypeService.delete${ClassName}ByIds(ids));
//	}

}
