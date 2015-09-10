package br.scmba.arquitetura.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.exception.BusinessException;
import br.scmba.exception.RequiredException;
import br.scmba.util.MensagemUtil;
import br.scmba.util.Util;

/**
 * Classe que concentra o fluxo de uma tela de consulta que pode redirecionar
 * para cadastro,edição. Sendo possível excluir e filtrar
 * 
 * @author wfjesus
 * 
 * @param <T>
 */
public abstract class BaseConsultaCRUD<T extends BaseEntity> extends AppBaseController {

	protected static final String CADASTRAR_MESSAGES = "cadastrarForm:messagesCadastro";

	protected static final String CONSULTA_MESSAGES = "formConsulta:messages";

	protected static final String DATA_TABLE_CRUD = "formConsulta:dataTableCrud";

	protected static boolean FECHA_MODAL = true;

	protected static String MODAL = "modalCadastro";
	protected static String MODAL_EXCLUIR = "modalExcluir";
	
	protected static String DIALOG_CADASTRAR = "cadastrarForm";
	
	private static final long serialVersionUID = 4674809733896204945L;

	private String nomeRotina;

	protected String mensagemPersonalizada;

	private T instanceFilter;

	private T instanceExcluir;

	private T instance;

	private List<T> lista;

	protected boolean disableAssociacao;
	protected boolean desabilitarPesquisarAssociacao;

	@Inject
	protected ContextoController contextoController;
	
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	/**
	 * Método responsável por criar a instancia que será utilzado pelo filtro da
	 * consulta
	 * 
	 * @return
	 */
	protected abstract T newInstance();

	/**
	 * Método responsável por vincular o service da Entidade parametrizada
	 * 
	 * @return
	 */
	protected abstract AbstractService<T> getService();

	@PostConstruct
	public void inicializar() {
		lista = new ArrayList<T>();
		instanceFilter = newInstance();
	}

	/**
	 * 
	 * @return Retorna a instância utilizada para filtrar a consulta
	 */
	public T getInstanceFilter() {
		if (instanceFilter == null) {
			instanceFilter = newInstance();
		}
		return instanceFilter;
	}

	public void setInstanceFilter(T instanceFilter) {
		this.instanceFilter = instanceFilter;
	}

	/**
	 * Método responsável por limpar os campos de filtro
	 */
	public void limparFiltro() {
		instanceFilter = newInstance();
		lista = new ArrayList<T>();
	}

	public void novo() {
		instance = newInstance();
	}

	/**
	 * Busca o objeto para a edição
	 * 
	 * @param mensagem
	 * @throws AppException
	 */
	public void edita(T t) throws AppException {
		instance = getService().getById(t.getId());
	}

	/**
	 * Método responsável por consultar os registros de acordo aos filtros
	 * informados
	 * 
	 * @throws Exception
	 */
	public void consultar() {
		try {
			lista = getService().findByParameters(instanceFilter);
			contextoController.setObjectFilter(instanceFilter);
			if (Util.isNullOuVazio(lista)) {
				facesMessager.addMessageError("geral.crud.noItemFound");
			}
		} catch (RequiredException re) {
			for (String message : re.getErroList())
				facesMessager.addMessageError(message);
		} catch (BusinessException be) {
			for (String message : be.getErroList())
				facesMessager.addMessageError(message);
		} catch (AppException ae) {
			facesMessager.addMessageError(ae.getMessage());
		}
	}

	/**
	 * Usado para refazer a consulta após a inclusão/alteração de um registro
	 */
	public void reConsultar() {
		try {
			setLista(getService().findByParameters(getInstanceFilter()));
			contextoController.setObjectFilter(getInstanceFilter());
		} catch (AppException e) {
			logger.error(e);
		}
	}

	/**
	 * Método responsável por excluir o registro selecionado
	 * 
	 * @param t
	 *            - Objeto que repesenta a linha selecionada
	 * @throws BusinessException
	 * @throws Exception
	 */
	public Boolean delete() {
		try {
			getService().delete(instanceExcluir);
			lista.remove(instanceExcluir);

			if (nomeRotina != null)
				facesMessager.info(MensagemUtil.obterMensagem("geral.crud.excluido", nomeRotina));
			else
				facesMessager.info(MensagemUtil.obterMensagem("geral.crud.excluido", "Registro"));
			return true;
		} catch (BusinessException be) {
			hideDialog(MODAL_EXCLUIR);
			for (String message : be.getErroList())
				facesMessager.addMessageError(message);
			return false;
		} catch (AppException ae) {
			facesMessager.addMessageError(ae.getMessage());
			return false;
		}

	}



	/**
	 * Persiste ou atualiza uma instancia na base de dados.
	 * 
	 * @throws AppException
	 */
	public void save() throws AppException {
		try {
			if (Util.isNullOuVazio(instance.getId())) {
				saveImpl(instance);
			} else {
				updateImpl(instance);
			}
			if (FECHA_MODAL) {
				hideDialog(MODAL);
				updateComponentes(CONSULTA_MESSAGES);
				updateComponentes(DATA_TABLE_CRUD);
			} else {
				updateComponentes(CADASTRAR_MESSAGES);
				updateComponentes(DATA_TABLE_CRUD);
				this.novo();
			}
		} catch (RequiredException re) {
			for (String message : re.getErroList())
				facesMessager.addMessageError(message);
		} catch (BusinessException be) {
			for (String message : be.getErroList())
				facesMessager.addMessageError(message);
		}
	}

	/**
	 * Finaliza a edicao de um registro Normalmente esse metodo deve ser
	 * invocado chamando um metodo de atualizacao na entidade(update)
	 * 
	 * @return
	 * @throws AppException
	 */
	protected void updateImpl(T referenceValue) throws AppException {
		getService().update(referenceValue);
		if (mensagemPersonalizada != null)
			facesMessager.info(MensagemUtil.obterMensagem(mensagemPersonalizada));
		else if (nomeRotina != null)
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.atualizado", nomeRotina));
		else
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.atualizado", "Registro"));
	}

	/**
	 * Finaliza a criacao de um registro Normalmente esse metodo deve ser
	 * invocado chamando um metodo de criacao na entidade(insert)
	 * 
	 * @return
	 * @throws BusinessException
	 * @throws AppException
	 */
	protected void saveImpl(T referenceValue) throws AppException {

		getService().save(referenceValue);

		if (mensagemPersonalizada != null)
			facesMessager.info(MensagemUtil.obterMensagem(mensagemPersonalizada));
		else if (nomeRotina != null)
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.salvo", nomeRotina));
		else
			facesMessager.info(MensagemUtil.obterMensagem("geral.crud.salvo", "Registro"));

	}

	/**
	 * limpa a ordenação e paginação da data table
	 */
	public void resetDatatable() {
		final DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(getIdDataTableCrud());
		if (table != null) {
			table.reset();
			table.setSortBy(null);
		}
	}

	/**
	 * Lista exibida na grid de resultados da consulta
	 * 
	 * @return List<T>
	 */
	public List<T> getLista() {
		return lista;
	}

	/**
	 * Guarda a lista que será exibida na grid de resultados da consulta
	 */
	public void setLista(List<T> lista) {
		this.lista = lista;
	}

	/**
	 * Retorna a instância do objeto que será excluído
	 * 
	 * @return T
	 */
	public T getInstanceExcluir() {
		return instanceExcluir;
	}

	/**
	 * Guarda uma instância do objeto que será excluído
	 */
	public void setInstanceExcluir(T instanceExcluir) {
		this.instanceExcluir = instanceExcluir;
	}

	/**
	 * Retorna a instância do objeto que será incluido/alterado
	 * 
	 * @return T
	 */
	public T getInstance() {
		if (instance == null) {
			instance = newInstance();
		}
		return instance;
	}

	/**
	 * Guarda uma instância do objeto que será incluido/alterado
	 */
	public void setInstance(T instance) {
		this.instance = instance;
	}

	/**
	 * Guarda uma instância do objeto que será incluido/alterado
	 */
	public String getRequiredMessage(String label) {
		return MensagemUtil.obterMensagem("geral.required.field", MensagemUtil.obterMensagem(label));
	}

	public void giveFocus(String pIdComponente) {
		RequestContext.getCurrentInstance().execute("giveFocus('" + pIdComponente + "')");
	}
	
	public String getUsuarioAlteracao(){
		String textoUsuario= "";
		if(getInstance().getUsuarioAlteracao()!=null){
			textoUsuario =  getInstance().getUsuarioAlteracao().getNomeAbreviado() + " - "+
					(getInstance().getDataAlteracao()  == null ? "":format.format(getInstance().getDataAlteracao()));
		}
		
		return textoUsuario;
	}
	
	public String getUsuarioCadastro(){
		String textoUsuario= "";
		if(getInstance().getUsuarioCadastro()!=null){
			textoUsuario =  getInstance().getUsuarioCadastro().getNomeAbreviado() + " - "+
					(getInstance().getDataCadastro()  == null ? "":format.format(getInstance().getDataCadastro()));
		}
		
		return textoUsuario;
	}

	/**
	 * Método responsável por retornar o id do data table que guarda o resultado
	 * da consulta
	 * 
	 * @return
	 */
	protected String getIdDataTableCrud() {
		return DATA_TABLE_CRUD;
	}

	public String getNomeRotina() {
		return nomeRotina;
	}

	public void setNomeRotina(String nomeRotina) {
		this.nomeRotina = nomeRotina;
	}

	public String getMensagemPersonalizada() {
		return mensagemPersonalizada;
	}

	public void setMensagemPersonalizada(String mensagemPersonalizada) {
		this.mensagemPersonalizada = mensagemPersonalizada;
	}

	public boolean isDisableAssociacao() {
		return disableAssociacao;
	}

	public void setDisableAssociacao(boolean disableAssociacao) {
		this.disableAssociacao = disableAssociacao;
	}

	public boolean isDesabilitarPesquisarAssociacao() {
		return desabilitarPesquisarAssociacao;
	}

	public void setDesabilitarPesquisarAssociacao(boolean desabilitarPesquisarAssociacao) {
		this.desabilitarPesquisarAssociacao = desabilitarPesquisarAssociacao;
	}

	public Boolean getPerfilConsulta() {
		return contextoController.getPerfilConsulta();
	}
	
	public void reset() {
        RequestContext.getCurrentInstance().reset(DIALOG_CADASTRAR);
    }

}
