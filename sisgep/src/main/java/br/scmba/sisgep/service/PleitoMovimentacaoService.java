package br.scmba.sisgep.service;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.PleitoMovimentacao;

@Stateless
public class PleitoMovimentacaoService extends AbstractService<PleitoMovimentacao> {

	private static final long serialVersionUID = 1L;

	@Override
	protected void validaCampos(PleitoMovimentacao entity) {
	}

	@Override
	protected void validaRegras(PleitoMovimentacao entity) {
	}

	@Override
	protected void validaRegrasExcluir(PleitoMovimentacao entity) throws AppException {

	}

	@Override
	public List<PleitoMovimentacao> findByParameters(PleitoMovimentacao object) throws AppException {
		List<PleitoMovimentacao> listaMovimentacoes = super.findByParameters(object);
		Collections.sort(listaMovimentacoes, Collections.reverseOrder());
		return listaMovimentacoes;

	}

}
