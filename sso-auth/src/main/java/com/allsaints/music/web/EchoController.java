package com.allsaints.music.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class EchoController {
	
	@RequestMapping("echo/{echo}")
	public String Echo(@PathVariable("echo") String echo){
		return echo;
	}

}
