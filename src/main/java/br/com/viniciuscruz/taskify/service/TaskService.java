package br.com.viniciuscruz.taskify.service;

import br.com.viniciuscruz.taskify.dto.TaskDto;
import br.com.viniciuscruz.taskify.exception.ResourceNotFoundException;
import br.com.viniciuscruz.taskify.mapper.CustomModelMapper;
import br.com.viniciuscruz.taskify.model.TaskModel;
import br.com.viniciuscruz.taskify.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<TaskDto> findAll() {
        var list = repository.findAll();
        return CustomModelMapper.parseObjectList(list, TaskDto.class);
    }

    public List<TaskDto> findByTitle(String title) {
        var tasks = repository.findByTitleContainsIgnoreCaseOrderByTitle(title);
        return CustomModelMapper.parseObjectList(tasks, TaskDto.class);
    }
}
