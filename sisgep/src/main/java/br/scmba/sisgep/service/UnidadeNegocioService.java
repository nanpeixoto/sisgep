package br.scmba.sisgep.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Pleito;
import br.scmba.sisgep.vo.PleitoMovimentacao;
import br.scmba.sisgep.vo.UnidadeNegocio;
import br.scmba.util.MensagemUtil;

@Stateless
public class UnidadeNegocioService extends AbstractService<UnidadeNegocio> {

	private static final long serialVersionUID = 1L;

	@Inject
	private PleitoMovimentacaoService pleitoMovimentacaoService;

	@Inject
	private PleitoService pleitoService;

	@Override
	protected void validaCampos(UnidadeNegocio entity) {
	}

	@Override
	protected void validaRegras(UnidadeNegocio entity) {
	}

	@Override
	protected void validaRegrasExcluir(UnidadeNegocio entity) throws AppException {
		PleitoMovimentacao filter = new PleitoMovimentacao();
		filter.setUnidadeNegocio(entity);
		List<PleitoMovimentacao> pleitosParaUnidadeDeNegocio = pleitoMovimentacaoService.findByParameters(filter);
		if (pleitosParaUnidadeDeNegocio != null && pleitosParaUnidadeDeNegocio.size() > 0) {
			super.mensagens.add(MensagemUtil.obterMensagem("geral.message.erro.relacionamentoExistente"));
		} else {
			Pleito filterPleito = new Pleito();
			filterPleito.setUltimaUnidadeNegocio(entity);
			List<Pleito> pleitosUnidadeNegocio = pleitoService.findByParameters(filterPleito);
			if (pleitosUnidadeNegocio != null && pleitosUnidadeNegocio.size() > 0) {
				mensagens.add(MensagemUtil.obterMensagem("geral.message.erro.relacionamentoExistente"));
			}
		}
	}

}
