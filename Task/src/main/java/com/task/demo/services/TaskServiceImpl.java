package com.task.demo.services;

import java.sql.SQLTransientConnectionException;
import java.util.Optional;
import javax.persistence.LockTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
	 * Method to increment value of number in the database using Optimistic locking,
	 * Pessimistic locking and Using combination of update query and @Transactional
	 * based on the below method just change the bellow method.
	 * 
	 * @return boolean
	 */
	@Override
//	@Transactional
	@Retryable(value = { LockTimeoutException.class, StaleObjectStateException.class,
			SQLTransientConnectionException.class }, maxAttempts = 10000)
	public boolean incrementNumbers() {
//		Optional<Numbers> num = taskRepo.getNumbersUsingPessimisticLock((long) 1);
		Optional<Numbers> num = taskRepo.findById((long) 1);
		if (num.isPresent()) {
			num.get().setNumber(num.get().getNumber() + 1);
			taskRepo.save(num.get());
		} else {
			Numbers n = new Numbers();
			n.setNumber((long) 1);
			taskRepo.save(n);
		}
//		taskRepo.incrementNumbersInDB((long) 1);
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
