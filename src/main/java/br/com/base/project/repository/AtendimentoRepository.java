package br.com.base.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.base.project.model.Atendimento;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>{

	List<Atendimento> findByOrderByIdDesc();
	
	List<Atendimento> findByOrderByDataAtendimentoAsc();

	List<Atendimento> findByDataAtendimentoBetweenOrderByDataAtendimentoAsc(LocalDateTime dataAtendimentoStart, LocalDateTime dataAtendimentoEnd);

	List<Atendimento> findByOrderByIdDesc(Pageable pageable);
	
}
