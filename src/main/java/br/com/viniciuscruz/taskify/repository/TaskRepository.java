package br.com.viniciuscruz.taskify.repository;

import br.com.viniciuscruz.taskify.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
}
