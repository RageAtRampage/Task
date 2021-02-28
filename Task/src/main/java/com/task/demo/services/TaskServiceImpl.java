package com.task.demo.services;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import org.hibernate.StaleObjectStateException;
import com.task.demo.models.Numbers;
import com.task.demo.repositories.TaskRepository;

@Service("com.task.demo.services.TaskServiceImpl")
public class TaskServiceImpl implements TaskService {

	@Autowired
	@Qualifier("com.task.demo.repositories.TaskRepository")
	private TaskRepository taskRepo;

	Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Method to retrieve value of number from database.
	 * 
	 * @return Numbers 
	 */
	@Override
	public Numbers getNumbers() {
		Optional<Numbers> num = taskRepo.findById((long) 1);
		if (num.isPresent()) {
			return num.get();
		}
		return null;
	}

	/**
	 * Method to increment value of number in the database with a retry policy based on optimistic locking of Numbers object. 
	 * 
	 * @return boolean
	 */
	@Override
	@Retryable(value = StaleObjectStateException.class , maxAttempts = 10000)
	public boolean incrementNumbers() throws StaleObjectStateException {
		Optional<Numbers> num = taskRepo.findById((long) 1);
		if (num.isPresent()) {
			num.get().setNumber(num.get().getNumber() + 1);
			taskRepo.save(num.get());
		} else {
			Numbers n = new Numbers();
			n.setNumber((long) 1);	
			taskRepo.save(n);
		}	
		return true;
	}
	
	
	/**
	 * Recovery policy if the retry policy fails.
	 * 
	 * @param throwable
	 */
	@Recover
    public void recover(Throwable throwable) {
        logger.info(throwable.getCause());
    }
}
