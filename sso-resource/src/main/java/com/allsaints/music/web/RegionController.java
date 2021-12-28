package com.allsaints.music.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.entity.SysRegion;
import com.allsaints.music.entity.repository.SysRegionRepository;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@RestController
@RequestMapping("region")
public class RegionController {
	
	@Autowired
	private SysRegionRepository regionRepository;
	
	@RequestMapping("query")
	public Result query(){
		Page<SysRegion> users = regionRepository.findAll(PageRequest.of(0, 100));
		return new Result(ResultCodeTemplate.SUCCESS, users);
	}
	
}
