package com.allsaints.music.http.advice;

import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionAdvice {
	
	@ExceptionHandler(value = ConstraintViolationException.class)
	public Result handlerConstraintViolationException(HttpServletRequest req, HttpServletResponse res, ConstraintViolationException e) {
		if(e.getCause() == null) {
			return new Result(ResultCodeTemplate.INVALID_PARAMS, e.getMessage());
		} else {
			return new Result(ResultCodeTemplate.INVALID_PARAMS, e.getCause().getMessage());
		}
	}

	@ExceptionHandler
	public Result handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
		if(e.getCause() == null) {
			return new Result(ResultCodeTemplate.BAD_REQUEST, e.getMessage());
		} else {
			return new Result(ResultCodeTemplate.BAD_REQUEST, e.getCause().getMessage());
		}
	}

}
