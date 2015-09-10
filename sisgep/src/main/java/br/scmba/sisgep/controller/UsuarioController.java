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
import br.scmba.sisgep.service.PapelService;
import br.scmba.sisgep.service.UsuarioService;
import br.scmba.sisgep.vo.Papel;
import br.scmba.sisgep.vo.Usuario;
import br.scmba.util.JSFUtil;
import br.scmba.util.MensagemUtil;
import br.scmba.util.RelatorioSiredUtil;
import br.scmba.util.StringUtil;
import br.scmba.util.Util;

@Named
@ViewScoped
public class UsuarioController extends BaseConsultaCRUD<Usuario> {

	private static final long serialVersionUID = -1153421337230811748L;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private PapelService papelService;

	private static String NOME_RELATORIO = "CADASTRO DE UNIDADE DE NEGÓCIO";

	private boolean faseEditar;

	private List<Usuario> listaFiltro;
	private List<Papel> listaPapeis;

	@Override
	protected Usuario newInstance() {
		return new Usuario();

	}

	@PostConstruct
	protected void init() throws AppException {
		obterLista();
		verificaFaseFormulario();
		obterListaCombos();
	}

	private void obterListaCombos() {
		Papel filter = new Papel();
		filter.setAtivo("S");
		try {
			listaPapeis = papelService.findByParameters(filter);
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
		}
	}

	private void verificaFaseFormulario() {
		if (!Util.isNullOuVazio(getInstance().getId()))
			faseEditar = true;
		else
			faseEditar = false;
	}

	private void obterLista() {
		setLista(usuarioService.findAll());
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
		if (getInstance().getGestorUnidade() == Boolean.TRUE) {
			getInstance().setStsGestorUnidade("S");
		} else
			getInstance().setStsGestorUnidade("N");

		if (faseEditar) {
			getInstance().setDataAlteracao(new Date());
			getInstance().setUsuarioAlteracao((Usuario) JSFUtil.getSessionMapValue("usuario"));
			if (getInstance().getSenha() != null) {
				getInstance().setSenha(StringUtil.criptografa(getInstance().getSenha()));
			} else {
				Usuario senha = usuarioService.getById(getInstance().getId());
				getInstance().setSenha(senha.getSenha());
			}
		} else {
			getInstance().setSenha(StringUtil.criptografa("123456"));
			getInstance().setDataCadastro(new Date());
			getInstance().setUsuarioCadastro((Usuario) JSFUtil.getSessionMapValue("usuario"));
		}

		super.save();
		hideDialog("modalCadastro");
		setInstanceFilter(null);
		reConsultar();

		// //REFAZ A CONSULTA
		obterLista();

	}

	@Override
	public void edita(Usuario vo) throws AppException {
		super.edita(vo);
		if (vo.getStsGestorUnidade().equalsIgnoreCase("S")) {
			getInstance().setGestorUnidade(true);
		} else
			getInstance().setGestorUnidade(false);
		getInstance().setStsGestorUnidade(null);
		faseEditar = true;

	}

	public void preExportar(Object document) {
		RelatorioSiredUtil relatorio = new RelatorioSiredUtil(NOME_RELATORIO, document);
		relatorio.preExportar();
	}

	public void excluir() {
		super.delete();

		// REFAZ A CONSULTA
		obterLista();
		// ATUALIZA A DATATABLE PARA EXIBIR O QUE FOI CADASTRADO
		updateComponentes(DATA_TABLE_CRUD);

	}

	private void limparForm() {
		setInstance(new Usuario());
	}

	public void novo() {
		limparForm();
		getInstance().setAtivo("S");
		getInstance().setEmail("@SANTACASABA.ORG.BR");

		getInstance().setPapel(papelService.getById(1));
		faseEditar = false;
	}

	@Override
	protected AbstractService<Usuario> getService() {
		return usuarioService;
	}

	public boolean isFaseEditar() {
		return faseEditar;
	}

	public void setFaseEditar(boolean faseEditar) {
		this.faseEditar = faseEditar;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List<Usuario> getListaFiltro() {
		return listaFiltro;
	}

	public List<Papel> getListaPapeis() {
		return listaPapeis;
	}

	public void setListaPapeis(List<Papel> listaPapeis) {
		this.listaPapeis = listaPapeis;
	}

	public void setListaFiltro(List<Usuario> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

}
