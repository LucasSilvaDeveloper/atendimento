$(function(){
	
	var modal = $('#modalCadastoRapidoEstilo');
	var botaoSalvar = modal.find('.js-modal-cadastro-estilo-salvar-btn');
	var form = modal.find('form');
	form.on('submit', function(event){ event.preventDefault() });
	
	var url = form.attr('action');
	var inputNomeEstilo = $('#nomeEstilo');
	
	var containerMensagemErro = $('.js-mesagem-cadastro-rapido-estilo');
	
	modal.on('shown.bs.modal', onModalShow);
	modal.on('hide.bs.modal', onModalClose);
	botaoSalvar.on('click', onBotaoSalvarClick);
	
	function onModalShow(){
		inputNomeEstilo.focus();		
	}
	
	function onModalClose(){
		inputNomeEstilo.val('');		
	}
	
	function onBotaoSalvarClick(){
		var nomeEstilo = inputNomeEstilo.val().trim();
		$.ajax({
			
			url: url,
			method:'POST',
			contentType: 'application/json',
			data: JSON.stringify({'nome' : nomeEstilo}),
			error: onErroSalvandoEstilo,
			success: onEstiloSalvo
		});
		
		function onErroSalvandoEstilo(obj){
			var erroMensage = obj.responseText;
			containerMensagemErro.removeClass('hidden');
			containerMensagemErro.html('<span>' + erroMensage + '</span>');
			form.find('.form-group').addClass('has-error');
		}
		
		function onEstiloSalvo(estilo){
			var comboEstilo = $('#estilo');
			comboEstilo.append('<option value=' + estilo.id + '>' + estilo.nome + '</option>');
			comboEstilo.val(estilo.id);
			form.find('.form-group').removeClass('has-error');
			modal.modal('hide');
		}
	}
});