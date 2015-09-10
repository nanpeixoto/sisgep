package br.scmba.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import br.scmba.exception.BusinessException;
import br.scmba.exception.RequiredException;
import br.scmba.util.LogUtil;
import br.scmba.util.MensagemUtil;

public class AppLogInterceptor {

	@Inject
	private transient Logger logger = Logger.getLogger(AppLogInterceptor.class);

	@AroundInvoke
	public Object intercept(InvocationContext context) throws Exception {
		context.getParameters();
		Object returnObject = null;

		try {

			returnObject = context.proceed();

		} catch (BusinessException e) {
			throw e;
		} catch (RequiredException e) {
			throw e;
		} catch (Exception e) {

			String mensagem = "Erro não Identificado";
			try {
				mensagem = e.getMessage() == null ? e.getClass().toString() : e.getMessage();
			} catch (Exception e2) {

			}
			logger.error(LogUtil.getMensagemPadraoLog(mensagem, LogUtil.getNomeFuncionalidade(context.getTarget().getClass().getSimpleName()), LogUtil.getDescricaoOperacao(context.getMethod().getName())));
			List<String> error = new ArrayList<String>();
			error.add(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
			throw new BusinessException(error);
		}

		String nomeFuncao = context.getMethod().getName();
		if (nomeFuncao.startsWith("save") || nomeFuncao.startsWith("salvar") || nomeFuncao.startsWith("update") || nomeFuncao.startsWith("delete") || nomeFuncao.startsWith("saveOrUpdade")) {
			// String Audiora = ((BaseEntity)context.getParameters()[0]
			// ).getAuditoria();
			logger.info(LogUtil.getMensagemPadraoLogManutencao(LogUtil.getNomeFuncionalidade(context.getTarget().getClass().getSimpleName()), LogUtil.getDescricaoOperacao(context.getMethod().getName())));
		}

		return returnObject;
	}

}
