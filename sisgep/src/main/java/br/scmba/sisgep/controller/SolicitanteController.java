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
import br.scmba.sisgep.service.SolicitanteService;
import br.scmba.sisgep.vo.Solicitante;
import br.scmba.sisgep.vo.Usuario;
import br.scmba.util.JSFUtil;
import br.scmba.util.RelatorioSiredUtil;
import br.scmba.util.Util;

@Named
@ViewScoped
public class SolicitanteController extends BaseConsultaCRUD<Solicitante> {

	private static final long serialVersionUID = -1153421337230811748L;

	@Inject
	private SolicitanteService solicitanteServive;

	private static String NOME_RELATORIO = "CADASTRO DE SOLICITANTES";

	private boolean faseEditar;

	private List<Solicitante> listaFiltro;
	
	

	@Override
	protected Solicitante newInstance() {
		return new Solicitante();

	}

	@PostConstruct
	protected void init() throws AppException {
		obterListaSolicitantes();
		verificaFaseFormulario();
	}

	private void verificaFaseFormulario() {
		if (!Util.isNullOuVazio(getInstance().getId()))
			faseEditar = true;
		else
			faseEditar = false;
	}

	private void obterListaSolicitantes() {
		setLista(solicitanteServive.findAll());
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

		
		if(faseEditar){
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
		obterListaSolicitantes();

	}
	
	
	

	@Override
	public void edita(Solicitante vo) throws AppException {
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
		setInstance(new Solicitante());
	}

	public void novo() {
		limparForm();
		getInstance().setAtivo("S");
		faseEditar = false;
	}

	@Override
	protected AbstractService<Solicitante> getService() {
		return solicitanteServive;
	}

	public SolicitanteService getSolicitanteServive() {
		return solicitanteServive;
	}

	public void setSolicitanteServive(SolicitanteService solicitanteServive) {
		this.solicitanteServive = solicitanteServive;
	}

	public boolean isFaseEditar() {
		return faseEditar;
	}

	public void setFaseEditar(boolean faseEditar) {
		this.faseEditar = faseEditar;
	}

	public List<Solicitante> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Solicitante> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

}
