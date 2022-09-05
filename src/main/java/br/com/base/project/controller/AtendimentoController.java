package br.com.base.project.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.base.project.model.Atendimento;
import br.com.base.project.service.AtendimentoService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/atendimento")
@Slf4j
@Controller
public class AtendimentoController {
	
	@Autowired
	private AtendimentoService atendimentoService;
	
	@RequestMapping("/cadastrar")
	public ModelAndView index1(Atendimento atendimento) {
		
		ModelAndView mv = new ModelAndView("home/home");
		
		return mv;
	}
	
	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public ModelAndView cadastrar(Atendimento atendimento, BindingResult result, RedirectAttributes attributes, String dataAtendimento) {
		
		atendimento.setDataAtendimento(LocalDateTime.parse(dataAtendimento));
		atendimentoService.save(atendimento);
		
		attributes.addFlashAttribute("mensagem", "Atendimento Salvo com sucesso!");
		return new ModelAndView("redirect:/atendimento/cadastrar");
	}
	
	@RequestMapping("/listar")
	public ModelAndView listar() {
		ModelAndView modelAndView = new ModelAndView("home/lista-atendimento");
		modelAndView.addObject("atendimentos", atendimentoService.findAll());
		return modelAndView;
	}
	
	@RequestMapping("/deletar/{id}")
	public ModelAndView deletar(@PathVariable Long id) {
		atendimentoService.deleteById(id);
		return new ModelAndView("redirect:/atendimento/listar");
	}
	
	@RequestMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable Long id) {
		ModelAndView modelAndView = new ModelAndView("home/home");
		Atendimento atendimento = atendimentoService.findById(id);
		modelAndView.addObject("atendimento", atendimento);
		modelAndView.addObject("dataAtendimento", atendimento.getDataAtendimento());
		return modelAndView;
	}
	
	
}
