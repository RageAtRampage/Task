package com.task.demo.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.task.demo.models.Message;
import com.task.demo.services.TaskService;

@RestController
@RequestMapping(value = "/task")
public class TaskController {

	Logger logger = LogManager.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("com.task.demo.services.TaskServiceImpl")
	private TaskService taskService;

	/**
	 * Endpoint to retrieve number from database.
	 * 
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> getNumbers() {
		logger.info("Getting number..");
		return new ResponseEntity<Object>(taskService.getNumbers(), HttpStatus.OK);
	}

	/**
	 * Endpoint to increment number in database.
	 * 
	 * @return ResponseEntity
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> incrementNumber() {
		logger.info("Incrementing number..");
		taskService.incrementNumbers();
		return new ResponseEntity<Object>(new Message("Successfully Icremented the number"), HttpStatus.ACCEPTED);
	}

}
