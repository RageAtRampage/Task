package com.task.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.task.demo.models.Numbers;

@Repository("com.task.demo.repositories.TaskRepository")
public interface TaskRepository extends CrudRepository<Numbers, Long> {

}
