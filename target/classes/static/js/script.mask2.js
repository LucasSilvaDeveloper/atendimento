  $(function() {
	var money = $('.js-money');
	money.maskMoney({allowNegative: false, thousands:'.', decimal:',', affixesStay: false});
	
	var decimal = $('.js-estoque');
	decimal.maskMoney({precision: 0, thousands:'.', allowNegative: false});
	
	var percentage = $('.js-porcentagem');
	percentage.maskMoney({precision: 0, allowNegative: false});
  });