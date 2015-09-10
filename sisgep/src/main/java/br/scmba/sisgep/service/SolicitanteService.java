package br.scmba.sisgep.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.vo.Pleito;
import br.scmba.sisgep.vo.Solicitante;
import br.scmba.util.MensagemUtil;

@Stateless
public class SolicitanteService extends AbstractService<Solicitante>  {

	private static final long serialVersionUID = 1L;

	@Inject
	private PleitoService pleitoService	;
	
	@Override
	protected void validaCampos(Solicitante entity) {	}

	@Override
	protected void validaRegras(Solicitante entity) { }

	@Override
	protected void validaRegrasExcluir(Solicitante entity) throws AppException {
		Pleito filter  = new Pleito();
		filter.setSolicitante(entity);
		
		List<Pleito> pleitosParaSolicitante= pleitoService.findByParameters(filter);
		if(pleitosParaSolicitante != null && pleitosParaSolicitante.size() > 0 ){
			mensagens.add(MensagemUtil.obterMensagem("geral.message.erro.relacionamentoExistente"));
		}
	}

}
