package com.task.demo.repositories;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.task.demo.models.Numbers;

@Repository("com.task.demo.repositories.TaskRepository")
public interface TaskRepository extends CrudRepository<Numbers, Long> {

	@Modifying
    @Query("update Numbers n set n.number = n.number + 1 where n.id = :id")
    void incrementNumbersInDB(Long id);
	
	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select n from Numbers n where id = :id")
    Optional<Numbers> getNumbersUsingPessimisticLock(Long id);

}
