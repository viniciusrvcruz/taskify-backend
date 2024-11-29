package br.com.viniciuscruz.taskify.service;

import br.com.viniciuscruz.taskify.dto.TaskDto;
import br.com.viniciuscruz.taskify.exception.ResourceNotFoundException;
import br.com.viniciuscruz.taskify.mapper.CustomModelMapper;
import br.com.viniciuscruz.taskify.model.TaskModel;
import br.com.viniciuscruz.taskify.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    public TaskDto create(TaskDto taskDto) {
        TaskModel task = CustomModelMapper.parseObject(taskDto, TaskModel.class);
        return CustomModelMapper.parseObject(repository.save(task), TaskDto.class);
    }

    public TaskDto findById(long id) {
        TaskModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada!")
        );

        return CustomModelMapper.parseObject(found, TaskDto.class);
    }

    public TaskDto update(TaskDto taskDto) {
        TaskModel found = repository.findById(taskDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada!")
        );

        found.setTitle(taskDto.getTitle());
        found.setDescription(taskDto.getDescription());
        return CustomModelMapper.parseObject(repository.save(found), TaskDto.class);
    }

    public void delete(long id) {
        TaskModel found = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tarefa não encontrada!")
        );

        repository.delete(found);
    }

    public Page<TaskDto> findAll(Pageable pageable) {
        var tasks = repository.findAll(pageable);
        return tasks.map(t -> CustomModelMapper.parseObject(t, TaskDto.class));
    }

    public Page<TaskDto> findByTitle(String title, Pageable pageable) {
        var tasks = repository.findByTitleContainsIgnoreCaseOrderByTitle(title, pageable);
        return tasks.map(t -> CustomModelMapper.parseObject(t, TaskDto.class));
    }
}
