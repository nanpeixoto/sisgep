package br.scmba.sisgep.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.scmba.arquitetura.controller.BaseConsultaCRUD;
import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.sisgep.service.UnidadeNegocioService;
import br.scmba.sisgep.service.UsuarioService;
import br.scmba.sisgep.vo.UnidadeNegocio;
import br.scmba.sisgep.vo.Usuario;
import br.scmba.util.JSFUtil;
import br.scmba.util.RelatorioSiredUtil;
import br.scmba.util.Util;

@Named
@ViewScoped
public class UnidadeNegocioController extends BaseConsultaCRUD<UnidadeNegocio> {

	private static final long serialVersionUID = -1153421337230811748L;

	@Inject
	private UnidadeNegocioService unidadeNegocioService;

	@Inject
	private UsuarioService usuarioService;

	private static String NOME_RELATORIO = "CADASTRO DE UNIDADE DE NEGÓCIO";

	private boolean faseEditar;

	private List<UnidadeNegocio> listaFiltro;

	private List<Usuario> listaUsuariosGetores;

	@Override
	protected UnidadeNegocio newInstance() {
		return new UnidadeNegocio();

	}

	@PostConstruct
	protected void init() throws AppException {
		obterListaSolicitantes();
		verificaFaseFormulario();
		obterListas();
	}

	private void obterListas() throws AppException {
		obterListaUsuarios();

	}

	private void obterListaUsuarios() throws AppException {
		Usuario filter = new Usuario();
		filter.setStsGestorUnidade("S");
		listaUsuariosGetores = usuarioService.findByParameters(filter);
	}

	private void verificaFaseFormulario() {
		if (!Util.isNullOuVazio(getInstance().getId()))
			faseEditar = true;
		else
			faseEditar = false;
	}

	private void obterListaSolicitantes() {
		setLista(unidadeNegocioService.findAll());
	}

	@Override
	/**
	 * Limpa o filtro de pesquisa
	 */
	public void limparFiltro() {
		super.limparFiltro();
		contextoController.limpar();
		contextoController.setObjectFilter(null);

	}

	/**
	 * Salvar
	 * 
	 * @throws AppException
	 */
	public void salvar() throws AppException {

		if (faseEditar) {
			getInstance().setDataAlteracao(new Date());
			getInstance().setUsuarioAlteracao((Usuario) JSFUtil.getSessionMapValue("usuario"));
		} else {
			getInstance().setDataCadastro(new Date());
			getInstance().setUsuarioCadastro((Usuario) JSFUtil.getSessionMapValue("usuario"));
		}

		super.save();
		setInstanceFilter(null);
		reConsultar();

		// //REFAZ A CONSULTA
		obterListaSolicitantes();

	}

	@Override
	public void edita(UnidadeNegocio vo) throws AppException {
		super.edita(vo);
		faseEditar = true;

	}

	public void preExportar(Object document) {
		RelatorioSiredUtil relatorio = new RelatorioSiredUtil(NOME_RELATORIO, document);
		relatorio.preExportar();
	}

	public void excluir() {
		super.delete();

		// REFAZ A CONSULTA
		obterListaSolicitantes();
		// ATUALIZA A DATATABLE PARA EXIBIR O QUE FOI CADASTRADO
		updateComponentes(DATA_TABLE_CRUD);

	}

	private void limparForm() {
		setInstance(new UnidadeNegocio());
	}

	public void novo() {
		limparForm();
		getInstance().setAtivo("S");
		faseEditar = false;
	}

	@Override
	protected AbstractService<UnidadeNegocio> getService() {
		return unidadeNegocioService;
	}

	public boolean isFaseEditar() {
		return faseEditar;
	}

	public void setFaseEditar(boolean faseEditar) {
		this.faseEditar = faseEditar;
	}

	public List<UnidadeNegocio> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<UnidadeNegocio> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public UnidadeNegocioService getUnidadeNegocioService() {
		return unidadeNegocioService;
	}

	public void setUnidadeNegocioService(UnidadeNegocioService unidadeNegocioService) {
		this.unidadeNegocioService = unidadeNegocioService;
	}

	public List<Usuario> getListaUsuariosGetores() {
		return listaUsuariosGetores;
	}

	public void setListaUsuariosGetores(List<Usuario> listaUsuariosGetores) {
		this.listaUsuariosGetores = listaUsuariosGetores;
	}

}
