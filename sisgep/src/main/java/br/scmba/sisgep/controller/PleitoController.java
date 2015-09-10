package br.scmba.sisgep.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.scmba.arquitetura.controller.BaseConsultaCRUD;
import br.scmba.arquitetura.service.AbstractService;
import br.scmba.exception.AppException;
import br.scmba.security.dto.Email;
import br.scmba.sisgep.enumerator.TipoStatusPleitoEnum;
import br.scmba.sisgep.service.PleitoMovimentacaoService;
import br.scmba.sisgep.service.PleitoService;
import br.scmba.sisgep.service.SolicitanteService;
import br.scmba.sisgep.service.TipoStatusService;
import br.scmba.sisgep.service.UnidadeNegocioService;
import br.scmba.sisgep.service.UsuarioService;
import br.scmba.sisgep.util.EmailRetorno;
import br.scmba.sisgep.util.EnvioEmail;
import br.scmba.sisgep.vo.Configuracao;
import br.scmba.sisgep.vo.Pleito;
import br.scmba.sisgep.vo.PleitoMovimentacao;
import br.scmba.sisgep.vo.Solicitante;
import br.scmba.sisgep.vo.TipoStatus;
import br.scmba.sisgep.vo.UnidadeNegocio;
import br.scmba.sisgep.vo.Usuario;
import br.scmba.util.JSFUtil;
import br.scmba.util.MensagemUtil;
import br.scmba.util.OperacoesArquivoUtil;
import br.scmba.util.RelatorioSiredUtil;
import br.scmba.util.Util;

@Named
@ViewScoped
public class PleitoController extends BaseConsultaCRUD<Pleito> {

	private static final long serialVersionUID = -1153421337230811748L;

	@Inject
	private PleitoService pleitoService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private SolicitanteService solicitanteService;

	@Inject
	private PleitoMovimentacaoService pleitoMovimentacaoService;

	@Inject
	private UnidadeNegocioService unidadeNegocioService;

	@Inject
	private TipoStatusService tipoStatusService;

	private static String NOME_RELATORIO = "CADASTRO DE PLEITOS";

	private boolean faseEditar;

	private List<Pleito> listaFiltro;

	private List<PleitoMovimentacao> listaFiltroMovimentacao;

	private PleitoMovimentacao pleitoMovimentacao;

	private Configuracao configuracao;

	private List<Solicitante> listaSolicitantes;
	private List<UnidadeNegocio> listaUnidadesNegocios;
	private List<TipoStatus> listaTipoStatus;
	private List<PleitoMovimentacao> listaMovimentacoes;

	private FileUpload file;

	private UploadedFile uploadedFile;

	private String nomeArquivoAdicionado;

	@Override
	protected Pleito newInstance() {
		return new Pleito();

	}

	@PostConstruct
	protected void init() throws AppException {
		obterLista();
		verificaFaseFormulario();
		obterListaCombos();
		nomeArquivoAdicionado = "";
	}

	public boolean getUnidadeNegocioObrigatorio() {
		try {
			if (pleitoMovimentacao.getTipoStatus().getStsExigeUnidadeNegocio().equalsIgnoreCase("S"))
				return true;
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	private void obterListaCombos() {
		Solicitante filter = new Solicitante();
		filter.setAtivo("S");
		try {
			listaSolicitantes = solicitanteService.findByParameters(filter);
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
		}
	}

	private void obterListaUnidadeNegocio() {
		UnidadeNegocio filter = new UnidadeNegocio();
		filter.setAtivo("S");
		try {
			setListaUnidadesNegocios(unidadeNegocioService.findByParameters(filter));
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
		}
	}

	private void obterListaTipoStatus() {
		TipoStatus filter = new TipoStatus();
		filter.setAtivo("S");
		try {
			setListaTipoStatus(tipoStatusService.findByParameters(filter));
		} catch (Exception e) {
			facesMessager.error(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
		}
	}

	public void obterCombosParaMovimentacao() {
		obterListaUnidadeNegocio();
		obterListaTipoStatus();

	}

	private void verificaFaseFormulario() {
		if (!Util.isNullOuVazio(getInstance().getId()))
			faseEditar = true;
		else
			faseEditar = false;
	}

	private void obterLista() throws AppException {
		Usuario usuario = (Usuario) JSFUtil.getSessionMapValue("usuario");
		UnidadeNegocio unidadeNegocioUsuario = new UnidadeNegocio();
		Pleito filterAtivo = new Pleito();
		// Se o usuario for gestor de unidade o sistema deve filtrar apenas os
		// pleitos para sua unidade de negocio
		if (usuario.getGestorUnidade()) {
			Usuario filterUsuario = new Usuario();
			UnidadeNegocio filter = new UnidadeNegocio();
			filterUsuario.setId(usuario.getId());
			filter.setUsuarioResponsavel(filterUsuario);
			List<UnidadeNegocio> unidades = unidadeNegocioService.findByParameters(filter);
			if (unidades != null && unidades.size() > 0) {
				unidadeNegocioUsuario = unidades.get(0);
			}
			filterAtivo.setUltimaUnidadeNegocio(unidadeNegocioUsuario);
		}

		filterAtivo.setAtivo("S");
		setLista(pleitoService.findByParameters(filterAtivo));

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
			getInstance().setStsPleito("A");
		}

		super.save();
		hideDialog("modalCadastro");
		setInstanceFilter(null);
		reConsultar();

		// //REFAZ A CONSULTA
		obterLista();

	}

	@Override
	public void edita(Pleito vo) throws AppException {
		super.edita(vo);
		faseEditar = true;

	}

	public void incluirMovimentacao(Pleito vo) throws AppException {
		super.edita(vo);
		PleitoMovimentacao filter = new PleitoMovimentacao();
		filter.setPleito(vo);
		listaMovimentacoes = pleitoMovimentacaoService.findByParameters(filter);

		pleitoMovimentacao = new PleitoMovimentacao();
		pleitoMovimentacao.setPleito(vo);

		obterCombosParaMovimentacao();
		faseEditar = false;
	}

	public void resetMovimentacoes() throws AppException {
		RequestContext.getCurrentInstance().reset("movimentacoesForm");
		obterLista();
		RequestContext.getCurrentInstance().update("pnlListaResultados");
		resetArquivo();

	}

	public void salvarMovimentacao() throws AppException {
		if (uploadedFile != null) {
			pleitoMovimentacao.setArquivo(uploadedFile.getContents());
			pleitoMovimentacao.setNomeArquivo(nomeArquivoAdicionado);
		}

		if (!faseEditar) {
			pleitoMovimentacao.setDataCadastro(new Date());
			pleitoMovimentacao.setUsuarioCadastro((Usuario) JSFUtil.getSessionMapValue("usuario"));
		}

		pleitoMovimentacaoService.save(pleitoMovimentacao);

		super.edita(pleitoMovimentacao.getPleito());
		getInstance().setUltimaMovimentacao(pleitoMovimentacao);
		getInstance().setStsPleito(pleitoMovimentacao.getTipoStatus().getStsPleito());
		// SE O STATUS PARA QUAL VAI O PLEITO E DE FINALIZACAO O USUARIO E A
		// DATA DE FINALIZACAO DEVE SER GRAVADO
		if (pleitoMovimentacao.getUnidadeNegocio() != null) {
			getInstance().setUltimaUnidadeNegocio(pleitoMovimentacao.getUnidadeNegocio());
		}
		if (pleitoMovimentacao.getTipoStatus().getStsPleito().equalsIgnoreCase(TipoStatusPleitoEnum.FINALIZADO.getValor())) {
			getInstance().setDataFinalizacao(new Date());
			getInstance().setUsuarioFinalizacao((Usuario) JSFUtil.getSessionMapValue("usuario"));
		} else {
			getInstance().setDataFinalizacao(null);
			getInstance().setUsuarioFinalizacao(null);
		}
		super.save();

		configuracao = (Configuracao) JSFUtil.getSessionMapValue("configuracao");

		if (configuracao.getStsEnviaEmail() != null && configuracao.getStsEnviaEmail().equalsIgnoreCase("S")) {
			EmailRetorno retorno = envioEmail(pleitoMovimentacao);
			pleitoMovimentacao.setEmailDeEnvio(retorno.getEmailEnvio());
			pleitoMovimentacao.setStsEnvioEmail((retorno.isSucesso() == true ? "S" : "E"));
			pleitoMovimentacao.setDescricaoErroEnvioEmail(retorno.getMensagemErro());
		} else {
			pleitoMovimentacao.setDescricaoErroEnvioEmail("Envio de E-mail desabilitado");
		}
		pleitoMovimentacaoService.update(pleitoMovimentacao);

		resetArquivo();

		incluirMovimentacao(pleitoMovimentacao.getPleito());

	}

	@SuppressWarnings("static-access")
	private String criarArquivo() {
		if (nomeArquivoAdicionado != null && !nomeArquivoAdicionado.equalsIgnoreCase("")) {
			String arquivo = FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getRealPath("/temp") + File.separator + nomeArquivoAdicionado;
			criaArquivo(uploadedFile.getContents(), arquivo);
			return arquivo;
		}
		return null;

	}

	@SuppressWarnings("static-access")
	public String carregarArquivo(PleitoMovimentacao movimentacao) {
		String arquivo = FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getRealPath("/temp") + File.separator + movimentacao.getNomeArquivo();
		criaArquivo(movimentacao.getArquivo(), arquivo);
		return arquivo;
	}

	public String download(PleitoMovimentacao movimentacao) {
		String arquivo = FacesContext.getCurrentInstance().getCurrentInstance().getExternalContext().getRealPath("/temp") + File.separator + movimentacao.getNomeArquivo();

		OperacoesArquivoUtil.downloadFile(movimentacao.getNomeArquivo(), arquivo, "pdf", FacesContext.getCurrentInstance());
		
		return "";
	}

	protected void criaArquivo(byte[] bytes, String arquivo) {
		FileOutputStream fos;

		try {
			if (bytes != null) {
				fos = new FileOutputStream(arquivo);
				fos.write(bytes);

				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void resetArquivo() {
		uploadedFile = null;
		nomeArquivoAdicionado = null;
	}

	public void upload(FileUploadEvent event) {
		uploadedFile = event.getFile();
		nomeArquivoAdicionado = uploadedFile.getFileName();
		FacesMessage message = new FacesMessage("Arquivo ", event.getFile().getFileName() + " adicionado ao status.");
		FacesContext.getCurrentInstance().addMessage("mensagemUpload", message);

	}

	public boolean isArquivoAdcionardo() {
		if (nomeArquivoAdicionado != null && !nomeArquivoAdicionado.equalsIgnoreCase(""))
			return true;
		return false;
	}

	private EmailRetorno envioEmail(PleitoMovimentacao pleitoMovimentacao) {
		EmailRetorno retorno = new EmailRetorno();
		Email email = EnvioEmail.converterPleitoConfiguracaoEmail(configuracao, pleitoMovimentacao);
		email.getAssunto();
		email.getCorpo();
		try {
			EnvioEmail envio = new EnvioEmail();
			retorno = envio.enviaEmailSimples(email, criarArquivo());
		} catch (Exception e) {
			retorno.setEmailEnvio(email.getEnderecoDestino());
			retorno.setMensagemErro("Erro no Envio do email");
			retorno.setSucesso(false);
		}
		return retorno;
	}

	public void preExportar(Object document) {
		RelatorioSiredUtil relatorio = new RelatorioSiredUtil(NOME_RELATORIO, document);
		relatorio.preExportar();
	}

	public void excluir() throws AppException {
		// A EXCLUSÃO SO PEITO O INATIVA (EXCLUSAO LOGICA) E GRAVA O USUARIO QUE
		// EXCLUIU
		Pleito vo = super.getInstanceExcluir();
		vo.setAtivo("N");
		vo.setDataExclusao(new Date());
		vo.setUsuarioExclusao((Usuario) JSFUtil.getSessionMapValue("usuario"));

		setInstance(vo);
		super.save();

		// REFAZ A CONSULTA
		obterLista();
		// ATUALIZA A DATATABLE PARA EXIBIR O QUE FOI CADASTRADO
		updateComponentes(DATA_TABLE_CRUD);

	}

	private void limparForm() {
		setInstance(new Pleito());
	}

	public void novo() {
		limparForm();
		getInstance().setAtivo("S");
		faseEditar = false;
	}

	@Override
	protected AbstractService<Pleito> getService() {
		return pleitoService;
	}

	public boolean isFaseEditar() {
		return faseEditar;
	}

	public void setFaseEditar(boolean faseEditar) {
		this.faseEditar = faseEditar;
	}

	public PleitoService getPleitoService() {
		return pleitoService;
	}

	public void setPleitoService(PleitoService pleitoService) {
		this.pleitoService = pleitoService;
	}

	public SolicitanteService getSolicitanteService() {
		return solicitanteService;
	}

	public void setSolicitanteService(SolicitanteService solicitanteService) {
		this.solicitanteService = solicitanteService;
	}

	public List<Solicitante> getListaSolicitantes() {
		return listaSolicitantes;
	}

	public void setListaSolicitantes(List<Solicitante> listaSolicitantes) {
		this.listaSolicitantes = listaSolicitantes;
	}

	public List<Pleito> getListaFiltro() {
		return listaFiltro;
	}

	public void setListaFiltro(List<Pleito> listaFiltro) {
		this.listaFiltro = listaFiltro;
	}

	public List<PleitoMovimentacao> getListaMovimentacoes() {
		return listaMovimentacoes;
	}

	public void setListaMovimentacoes(List<PleitoMovimentacao> listaMovimentacoes) {
		this.listaMovimentacoes = listaMovimentacoes;
	}

	public List<UnidadeNegocio> getListaUnidadesNegocios() {
		return listaUnidadesNegocios;
	}

	public void setListaUnidadesNegocios(List<UnidadeNegocio> listaUnidadesNegocios) {
		this.listaUnidadesNegocios = listaUnidadesNegocios;
	}

	public List<TipoStatus> getListaTipoStatus() {
		return listaTipoStatus;
	}

	public void setListaTipoStatus(List<TipoStatus> listaTipoStatus) {
		this.listaTipoStatus = listaTipoStatus;
	}

	public PleitoMovimentacao getPleitoMovimentacao() {
		return pleitoMovimentacao;
	}

	public void setPleitoMovimentacao(PleitoMovimentacao pleitoMovimentacao) {
		this.pleitoMovimentacao = pleitoMovimentacao;
	}

	public UnidadeNegocioService getUnidadeNegocioService() {
		return unidadeNegocioService;
	}

	public void setUnidadeNegocioService(UnidadeNegocioService unidadeNegocioService) {
		this.unidadeNegocioService = unidadeNegocioService;
	}

	public TipoStatusService getTipoStatusService() {
		return tipoStatusService;
	}

	public void setTipoStatusService(TipoStatusService tipoStatusService) {
		this.tipoStatusService = tipoStatusService;
	}

	public List<PleitoMovimentacao> getListaFiltroMovimentacao() {
		return listaFiltroMovimentacao;
	}

	public void setListaFiltroMovimentacao(List<PleitoMovimentacao> listaFiltroMovimentacao) {
		this.listaFiltroMovimentacao = listaFiltroMovimentacao;
	}

	public Configuracao getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(Configuracao configuracao) {
		this.configuracao = configuracao;
	}

	public FileUpload getFile() {
		return file;
	}

	public void setFile(FileUpload file) {
		this.file = file;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public String getNomeArquivoAdicionado() {
		return nomeArquivoAdicionado;
	}

	public void setNomeArquivoAdicionado(String nomeArquivoAdicionado) {
		this.nomeArquivoAdicionado = nomeArquivoAdicionado;
	}

}
