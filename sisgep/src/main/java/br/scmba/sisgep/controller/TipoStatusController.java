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
import br.scmba.sisgep.enumerator.TipoStatusPleitoEnum;
import br.scmba.sisgep.service.TipoStatusService;
import br.scmba.sisgep.vo.TipoStatus;
import br.scmba.sisgep.vo.Usuario;
import br.scmba.util.JSFUtil;
import br.scmba.util.RelatorioSiredUtil;
import br.scmba.util.Util;

@Named
@ViewScoped
public class TipoStatusController extends BaseConsultaCRUD<TipoStatus> {

	private static final long serialVersionUID = -1153421337230811748L;

	@Inject
	private TipoStatusService tipoStatusService;

	private static String NOME_RELATORIO = "CADASTRO DE TIPO STATUS";

	private boolean faseEditar;

	private List<TipoStatus> listaFiltro;

	private TipoStatusPleitoEnum[] listaStatusPleito;

	@Override
	protected TipoStatus newInstance() {
		return new TipoStatus();

	}

	@PostConstruct
	protected void init() throws AppException {
		obterLista();
		verificaFaseFormulario();
		preencherLista();

	}

	private void preencherLista() {
		listaStatusPleito = TipoStatusPleitoEnum.values();

	}

	private void verificaFaseFormulario() {
		if (!Util.isNullOuVazio(getInstance().getId()))
			faseEditar = true;
		else
			faseEditar = false;
	}

	private void obterLista() {
		setLista(tipoStatusService.findAll());
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

		if (getInstance().getExigeUnidadeNegocio() == Boolean.TRUE) {
			getInstance().setStsExigeUnidadeNegocio("S");
		} else
			getInstance().setStsExigeUnidadeNegocio("N");

		if (faseEditar) {
			getInstance().setDataAlteracao(new Date());
			getInstance().setUsuarioAlteracao((Usuario) JSFUtil.getSessionMapValue("usuario"));

		} else {

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
	public void edita(TipoStatus vo) throws AppException {

		super.edita(vo);
		if (vo.getStsExigeUnidadeNegocio().equalsIgnoreCase("S")) {
			getInstance().setExigeUnidadeNegocio(true);
		} else
			getInstance().setExigeUnidadeNegocio(false);
		getInstance().setStsExigeUnidadeNegocio(null);
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
		setInstance(new TipoStatus());
	}

	public void novo() {
		limparForm();
		getInstance().setAtivo("S");
		faseEditar = false;
	}

	@Override
	protected AbstractService<TipoStatus> getService() {
		return tipoStatusService;
	}

	public boolean isFaseEditar() {
		return faseEditar;
	}

	public void setFaseEditar(boolean faseEditar) {
		this.faseEditar = faseEditar;
	}

	public TipoStatusService getTipoStatusService() {
		return tipoStatusService;
	}

	public void setTipoStatusService(TipoStatusService tipoStatusService) {
		this.tipoStatusService = tipoStatusService;
	}

	public List<TipoStatus> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<TipoStatus> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public TipoStatusPleitoEnum[] getListaStatusPleito() {
		return listaStatusPleito;
	}

	public void setListaStatusPleito(TipoStatusPleitoEnum[] listaStatusPleito) {
		this.listaStatusPleito = listaStatusPleito;
	}

}
