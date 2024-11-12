package br.com.viniciuscruz.taskify.service;

import br.com.viniciuscruz.taskify.dto.ChecklistDto;
import br.com.viniciuscruz.taskify.exception.ResourceNotFoundException;
import br.com.viniciuscruz.taskify.mapper.CustomModelMapper;
import br.com.viniciuscruz.taskify.model.ChecklistModel;
import br.com.viniciuscruz.taskify.model.TaskModel;
import br.com.viniciuscruz.taskify.repository.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChecklistService {

    @Autowired
    private ChecklistRepository repository;

    public ChecklistDto create(ChecklistDto checklistDto) {
        ChecklistModel checklistModel = CustomModelMapper.parseObject(checklistDto, ChecklistModel.class);

        return CustomModelMapper.parseObject(repository.save(checklistModel), ChecklistDto.class);
    }

    public ChecklistDto findById(long id) {
        ChecklistModel checklistModel = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Checklist não encontrada!")
        );

        return CustomModelMapper.parseObject(checklistModel, ChecklistDto.class);
    }

    public ChecklistDto update(ChecklistDto checklistDto) {
        ChecklistModel checklistModel = repository.findById(checklistDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Checklist não encontrada!")
        );

        checklistModel.setTitle(checklistDto.getTitle());
        checklistModel.setDescription(checklistDto.getDescription());
        checklistModel.setIsChecked(checklistDto.getIsChecked());

        if (checklistDto.getIsChecked()) {
            checklistModel.setCheckedAt(new Date());  // Set date
        }

        checklistModel.setTask(CustomModelMapper.parseObject(checklistDto.getTask(), TaskModel.class));
        return CustomModelMapper.parseObject(repository.save(checklistModel), ChecklistDto.class);
    }

    public void delete(long id) {
        ChecklistModel checklistModel = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Checklist não encontrada!")
        );

        repository.delete(checklistModel);
    }

    public Page<ChecklistDto> findAll(Pageable pageable) {
        var checklists = repository.findAll(pageable);
        return checklists.map(c -> CustomModelMapper.parseObject(c, ChecklistDto.class));
    }
}
