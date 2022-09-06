package br.com.base.project.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.base.project.model.Atendimento;
import br.com.base.project.repository.AtendimentoRepository;

@Service
public class AtendimentoService {

	@Autowired
	private AtendimentoRepository atendimentoRepository;
	
	@Autowired
	private ExcelService excelService;
	
	public void save(Atendimento atendimento) {
		atendimentoRepository.save(atendimento);
	}

	public List<Atendimento> findAll() {
		return atendimentoRepository.findByOrderByIdDesc();
	}

	public void deleteById(Long id) {
		atendimentoRepository.deleteById(id);
	}

	public Atendimento findById(Long id) {
		return atendimentoRepository.findById(id).get();
	}

	public void exportarExcel(HttpServletResponse response) {
		try {
			excelService.excelAtendimento(response, atendimentoRepository.findByOrderByDataAtendimentoAsc());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
