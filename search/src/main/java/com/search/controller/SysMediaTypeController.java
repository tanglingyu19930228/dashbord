package com.search.controller;


import com.search.common.controller.BaseController;
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
@RequestMapping("SysMediaType")
public class SysMediaTypeController extends BaseController {
//
//	@Autowired
//	private I${ClassName}Service SysMediaTypeService;
//
//	/**
//	 * 查询${functionName}
//	 */
//	@GetMapping("get/{${pkColumn.javaField}}")
//	public ${ClassName} get(@PathVariable("${pkColumn.javaField}") ${pkColumn.javaType} ${pkColumn.javaField}) {
//		return SysMediaTypeService.select${ClassName}ById(${pkColumn.javaField});
//
//	}
//
//	/**
//	 * 查询${functionName}列表
//	 */
//	@GetMapping("list")
//	public R list(${ClassName} SysMediaType) {
//		startPage();
//        return R.ok(SysMediaTypeService.select${ClassName}List(SysMediaType));
//	}
//
//
//	/**
//	 * 新增保存${functionName}
//	 */
//	@PostMapping("save")
//	public R addSave(@RequestBody ${ClassName} SysMediaType) {
//		return R.ok(SysMediaTypeService.insert${ClassName}(SysMediaType));
//	}
//
//	/**
//	 * 修改保存${functionName}
//	 */
//	@PostMapping("update")
//	public R editSave(@RequestBody ${ClassName} SysMediaType) {
//		return R.ok(SysMediaTypeService.update${ClassName}(SysMediaType));
//	}
//
//	/**
//	 * 删除${functionName},默认不启用
//	 */
//	// @PostMapping("remove")
//	public R remove(String ids) {
//		return R.ok(SysMediaTypeService.delete${ClassName}ByIds(ids));
//	}

}
