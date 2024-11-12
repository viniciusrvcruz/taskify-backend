package br.com.viniciuscruz.taskify.repository;

import br.com.viniciuscruz.taskify.model.ChecklistModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChecklistRepository extends JpaRepository<ChecklistModel, Long> {
    public Page<ChecklistModel> findAll(Pageable pageable);
}
