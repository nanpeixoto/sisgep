package br.scmba.sisgep.service;

import javax.ejb.Stateless;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Papel;


@Stateless
public class PapelService extends AbstractService<Papel> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(Papel entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Papel entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Papel entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

	

}
