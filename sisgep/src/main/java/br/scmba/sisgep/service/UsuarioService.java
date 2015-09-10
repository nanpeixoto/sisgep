package br.scmba.sisgep.service;

import javax.ejb.Stateless;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Usuario;


@Stateless
public class UsuarioService extends AbstractService<Usuario> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(Usuario entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegras(Usuario entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validaRegrasExcluir(Usuario entity) throws AppException {
		// TODO Auto-generated method stub
		
	}

	

}
