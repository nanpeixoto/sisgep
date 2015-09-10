package br.scmba.sisgep.service;

import javax.ejb.Stateless;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Pleito;

@Stateless
public class PleitoService extends AbstractService<Pleito>  {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(Pleito entity) {	}

	@Override
	protected void validaRegras(Pleito entity) { }

	@Override
	protected void validaRegrasExcluir(Pleito entity) throws AppException { 
		
	}
	

}
