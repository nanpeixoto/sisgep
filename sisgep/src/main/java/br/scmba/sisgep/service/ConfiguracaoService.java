package br.scmba.sisgep.service;

import javax.ejb.Stateless;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Configuracao;

@Stateless
public class ConfiguracaoService extends AbstractService<Configuracao>  {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(Configuracao entity) {	}

	@Override
	protected void validaRegras(Configuracao entity) { }

	@Override
	protected void validaRegrasExcluir(Configuracao entity) throws AppException { 
		
	}
	

}
