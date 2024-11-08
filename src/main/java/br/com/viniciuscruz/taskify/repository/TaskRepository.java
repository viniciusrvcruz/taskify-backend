package br.com.viniciuscruz.taskify.repository;

import br.com.viniciuscruz.taskify.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    public List<TaskModel> findByTitleContainsIgnoreCaseOrderByTitle(String title);
}
