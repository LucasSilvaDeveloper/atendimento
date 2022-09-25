package br.com.base.project.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.base.project.model.Atendimento;
import br.com.base.project.service.AtendimentoService;

@RequestMapping("/atendimento")
@Controller
public class AtendimentoController {
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	@RequestMapping("/cadastrar")
	public ModelAndView index1(Atendimento atendimento) {
		ModelAndView modelAndView = new ModelAndView("home/home");
		modelAndView.addObject("atendimento", Atendimento.builder().build());
		return modelAndView;
	}
	
	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrar(Atendimento atendimento, BindingResult result, RedirectAttributes attributes) {
		
		if (atendimento.getId() != null) {
			attributes.addFlashAttribute("mensagem", "Atendimento Atualizado com Sucesso!");
		}else {
			attributes.addFlashAttribute("mensagem", "Atendimento Salvo com Sucesso!");
		}
		atendimentoService.save(atendimento);
		return new ModelAndView("redirect:/atendimento/cadastrar");
	}
	
	@RequestMapping("/listar")
	public ModelAndView listar(@PageableDefault(size = 10, sort = "dataAtendimento", direction = Direction.DESC) Pageable pageable, RedirectAttributes attributes, String pgnumber) {
		ModelAndView modelAndView = new ModelAndView("home/lista-atendimento");
		Page<Atendimento> atendimentos = null;
		if (pgnumber != null && Integer.parseInt(pgnumber) > 1) {
			Pageable withPage = pageable.withPage(Integer.parseInt(pgnumber)-1);
			atendimentos = atendimentoService.findAll(withPage);
			
			if (atendimentos.getTotalPages() > 0 && atendimentos.isEmpty()) {
				Pageable withPageAUX = pageable.withPage(withPage.getPageNumber()-1);
				atendimentos = atendimentoService.findAll(withPageAUX);
			}
			
		}else {
			atendimentos = atendimentoService.findAll(pageable);
		}
		modelAndView.addObject("atendimentos", atendimentos);
		return modelAndView;
	}
	
	@RequestMapping("/deletar/{id}/{pageNumber}")
	public ModelAndView deletar(@PathVariable Long id, RedirectAttributes attributes, @PathVariable Long pageNumber) {
		atendimentoService.deleteById(id);
		attributes.addFlashAttribute("mensagem", "Atendimento Deletado com Sucesso!");
		
		ModelAndView modelAndView = new ModelAndView("redirect:/atendimento/listar");
		modelAndView.addObject("pgnumber", pageNumber);
		return modelAndView;
	}
	
	@RequestMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("home/home");
		Atendimento atendimento = atendimentoService.findById(id);
		modelAndView.addObject("atendimento", atendimento);
		return modelAndView;
	}
	
	@RequestMapping("/relatorio/excel")
	public void exportarExcel(HttpServletResponse response, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dataInicial, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate dataFinal) {
		response.setContentType("application/octet-stream");
		
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Atendimento_" + LocalDate.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("dd_MM_yyyy")) + ".xlsx";
        response.setHeader(headerKey, headerValue);
		atendimentoService.exportarExcel(response, dataInicial, dataFinal);
	}
	
}
