package ru.savin.minicrm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class EmployeeExceptionHandler {

	// todo они специально называются одинаково?
	@ExceptionHandler
	public ModelAndView handleException(EmployeeNotFoundException exc) {

		EmployeeErrorResponse error = new EmployeeErrorResponse();

		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(exc.getMessage());

		return new ModelAndView("error/exception", "exceptionMsg", "Details: " + error.getMessage());
	}

	@ExceptionHandler
	public ModelAndView handleException(Exception exc) {

		EmployeeErrorResponse error = new EmployeeErrorResponse();

		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(exc.getMessage());

		return new ModelAndView("error/exception", "exceptionMsg", "Details: " + error.getMessage());
	}
	
}
