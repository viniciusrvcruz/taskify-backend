package br.com.viniciuscruz.taskify.controller;

import br.com.viniciuscruz.taskify.dto.TaskDto;
import br.com.viniciuscruz.taskify.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) {
        TaskDto model = service.create(taskDto);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable(name = "id") long id) {
        TaskDto task = service.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<TaskDto> update(@RequestBody TaskDto taskDto) {
        TaskDto task = service.update(taskDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") long id) {
        service.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll(){
        var list = service.findAll();
        return new ResponseEntity<List<TaskDto>>(list, HttpStatus.OK);
    }
}
