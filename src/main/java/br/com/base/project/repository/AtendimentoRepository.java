package br.com.base.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.base.project.model.Atendimento;
import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long>{

	List<Atendimento> findByOrderByIdDesc();
	
	List<Atendimento> findByOrderByDataAtendimentoAsc();
	
}
