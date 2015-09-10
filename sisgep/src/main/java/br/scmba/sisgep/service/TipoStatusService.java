package br.scmba.sisgep.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.PleitoMovimentacao;
import br.scmba.sisgep.vo.TipoStatus;
import br.scmba.util.MensagemUtil;

@Stateless
public class TipoStatusService extends AbstractService<TipoStatus> {

	private static final long serialVersionUID = 1L;

	@Inject
	private PleitoMovimentacaoService pleitoMovimentacaoService;

	@Override
	protected void validaCampos(TipoStatus entity) {
	}

	@Override
	protected void validaRegras(TipoStatus entity) {
	}

	@Override
	protected void validaRegrasExcluir(TipoStatus entity) throws AppException {
		PleitoMovimentacao filter = new PleitoMovimentacao();
		filter.setTipoStatus(entity);

		List<PleitoMovimentacao> lista = pleitoMovimentacaoService.findByParameters(filter);
		if (lista != null && lista.size() > 0) {
			mensagens.add(MensagemUtil.obterMensagem("geral.message.erro.relacionamentoExistente"));
		}
	}

}
