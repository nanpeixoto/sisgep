package br.scmba.security;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.scmba.arquitetura.controller.ContextoController;
import br.scmba.exception.AppException;
import br.scmba.repository.ConfigurationRepository;
import br.scmba.sisgep.service.ConfiguracaoService;
import br.scmba.sisgep.service.MenuService;
import br.scmba.sisgep.service.UsuarioService;
import br.scmba.sisgep.vo.Configuracao;
import br.scmba.sisgep.vo.Menu;
import br.scmba.sisgep.vo.Papel;
import br.scmba.sisgep.vo.Usuario;
import br.scmba.util.JSFUtil;
import br.scmba.util.LogUtil;
import br.scmba.util.MensagemUtil;
import br.scmba.util.StringUtil;
import br.scmba.util.Util;

@Named
@SessionScoped
public class ControleAcesso implements Serializable {

	private static final long serialVersionUID = 7444956049460916877L;

	@Inject
	@ConfigurationRepository("configuracoes.properties")
	Properties configuracoes;

	private MenuModel model;

	@Inject
	private static Logger logger = Logger.getLogger(ControleAcesso.class);

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private ConfiguracaoService configuracaoService;

	@Inject
	private MenuService menuService;

	@Inject
	private ContextoController contextoController;

	private String matriculaUsuario;

	private String senhaUsuario;

	private Usuario usuario;

	private MenuModel menuBarraHorizontal = new DefaultMenuModel();

	private String linkMenu;
	private Integer perfilConsulta;

	private String nomeMenu;

	/**
	 * Método obtém a data corrente com horas e minutos
	 */
	private void getDiaDaSemana() {
		JsfUtil.setSessionMapValue("dataDiaExtenso", Util.formatData(Calendar.getInstance().getTime(), "EEEE, dd/MM/yyyy HH:mm"));
	}

	public ControleAcesso() {
		getDiaDaSemana();
		model = new DefaultMenuModel();
	}

	/**
	 * Método valida os campos informados pelo usuário
	 */
	public boolean validarCampos() {

		if (Util.isNullOuVazio(matriculaUsuario) || Util.isNullOuVazio(senhaUsuario)) {

			if (Util.isNullOuVazio(matriculaUsuario) || "_______".equals(matriculaUsuario)) {
				JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("geral.required.field", MensagemUtil.obterMensagem("login.label.usuario")));
			}
			if (Util.isNullOuVazio(senhaUsuario)) {
				JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("geral.required.field", MensagemUtil.obterMensagem("login.label.senha")));
			}

			if (!Util.isNullOuVazio(senhaUsuario)) {
				senhaUsuario = "";
			}
			return false;

		} else {
			return true;
		}

	}

	/**
	 * Método limpa os campos informados
	 */
	public void limparCampos() {
		if (FacesContext.getCurrentInstance().getMessageList() == null || FacesContext.getCurrentInstance().getMessageList().size() == 0) {
			usuario = new Usuario();
			senhaUsuario = "";
			matriculaUsuario = "";
		}

	}

	/**
	 * Método autentica login e senha no LDAP
	 * 
	 * @return
	 */
	public String autenticar() {

		if (validarCampos()) {
			try {

				Usuario filter = new Usuario();
				filter.setSenha(StringUtil.criptografa(senhaUsuario));
				filter.setMatricula(Long.parseLong(matriculaUsuario));
				List<Usuario> usuarios = usuarioService.findByParameters(filter);

				if (usuarios != null && usuarios.size() > 0) {
					usuario = usuarios.get(0);

					if (usuario.getAtivo().equalsIgnoreCase("N")) {

						limparCampos();
						RequestContext.getCurrentInstance().execute("hideStatus();");
						JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("login.label.usuario.inativo"));
						return "login";

					}
					// verifica se usuario possui papel associado
					if (usuario.getPapel() == null) {
						limparCampos();
						RequestContext.getCurrentInstance().execute("hideStatus();");
						JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("login.label.usuario.sem.papel"));
						return "login";
					}

					// verifica se usuario possui papel associado
					if (usuario.getPapel() != null && usuario.getPapel().getAtivo().equals("N")) {
						limparCampos();
						RequestContext.getCurrentInstance().execute("hideStatus();");
						JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("login.label.usuario.papel.inativo"));
						return "login";
					}

					// montarMenuBarraHorizontal(usuario.getListaGruposLdapAsString());

					montarMenuBarraHorizontal(usuario);

					// if (model != null && model.getElements() != null &&
					// model.getElements().size() > 0) {

					Configuracao configuracao = new Configuracao();
					List<Configuracao> configuracoes = configuracaoService.findAll();
					if (configuracoes != null && configuracoes.size() > 0)
						configuracao = configuracoes.get(0);

					// Setando dados do usuario na sessão
					JSFUtil.setSessionMapValue("loggedUser", usuario.getMatricula());
					JSFUtil.setSessionMapValue("usuario", usuario);
					JsfUtil.setSessionMapValue("loggedUserPassword", usuario.getSenha());
					JsfUtil.setSessionMapValue("perfisUsuario", usuario.getPapel());
					JSFUtil.setSessionMapValue("loggedMatricula", usuario.getMatricula());
					//seto a tabela de configuracoes na memória
					JSFUtil.setSessionMapValue("configuracao", configuracao);

					return redirectHome();
					/*
					 * } else { limparCampos();
					 * RequestContext.getCurrentInstance
					 * ().execute("hideStatus();");
					 * JsfUtil.addErrorMessage(MensagemUtil
					 * .obterMensagem("login.label.usuario.papel.inativo"));
					 * return "login"; }
					 */

				} else {
					JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("seguranca.massage.validation.senhaUsuario.invalidos"));
					// throw new
					// AppException(LogUtil.getMensagemPadraoLog("NÃO FOI POSSÍVEL OBTER O USUÁRIO",
					// "ControleAcesso", "Login", matriculaUsuario));
					return "login";
				}

			} catch (AppException e) {
				String mensagemLog = LogUtil.getMensagemPadraoLog("FALHA DE AUTENTICAÇÃO", "ControleAcesso", "Login", matriculaUsuario);
				logger.error(mensagemLog);

				RequestContext.getCurrentInstance().execute("hideStatus();");
				JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("geral.message.erro.desconhecido"));
				limparCampos();
				return "login";

			} catch (Exception e) {

				String mensagemLog = LogUtil.getMensagemPadraoLog("FALHA DE AUTENTICAÇÃO", "ControleAcesso", "Login", matriculaUsuario);
				logger.error(mensagemLog);

				e.printStackTrace();

				limparCampos();
				RequestContext.getCurrentInstance().execute("hideStatus();");
				JsfUtil.addErrorMessage(MensagemUtil.obterMensagem("login.label.usuarioSenha.invalidos"));
				return "login";
			}

			// return redirectHome();
		}

		RequestContext.getCurrentInstance().execute("hideStatus();");
		return "login";
	}

	/**
	 * Método encerra a sessão e redireciona para página de login
	 * 
	 * @return
	 */
	public String logout() {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.resetBuffer();

		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		limparCampos();
		RequestContext.getCurrentInstance().execute("hideStatus();");
		return redirectHome();
	}

	/**
	 * Redireciona para página home.xhtml
	 * 
	 * @return
	 */
	public String redirectHome() {
		RequestContext.getCurrentInstance().execute("hideStatus();");
		return "/home.xhtml?faces-redirect=true";
	}

	/**
	 * Redireciona telas de menu
	 * 
	 * @return
	 */
	public String redirecionarTela() {
		contextoController.setCrudMessage(null);
		contextoController.setObject(null);
		contextoController.setObjectFilter(null);
		contextoController.setTelaOrigem(null);

		if (perfilConsulta != null) {
			if (perfilConsulta == 0)
				contextoController.setPerfilConsulta(false);
			else
				contextoController.setPerfilConsulta(true);
		}

		return getLinkMenu() + "?faces-redirect=true";
	}

	/**
	 * Monta o menu horizontal
	 * 
	 * @param pLista
	 *            ,
	 * @throws Exception
	 */
	private void montarMenuBarraHorizontal(Usuario user) throws AppException {
		DefaultSubMenu submenu = null;

		for (Menu menuPai : getMenusPais(user.getPapel())) {
			submenu = new DefaultSubMenu(menuPai.getNome(), menuPai.getIcone());

			for (Menu menuFilho : getMenusFilhos(user.getPapel())) {
				if (menuPai.getId().equals(menuFilho.getMenuPai().getId())) {
					DefaultMenuItem item = new DefaultMenuItem(menuFilho.getNome());
					item.setUrl(menuFilho.getLink());
					item.setIcon(menuFilho.getIcone());
					submenu.addElement(item);
				}

			}
			model.addElement(submenu);

		}
	}

	public List<Menu> getMenusFilhos(Papel papel) {
		return (List<Menu>) menuService.findMenuByTipoAndPapelAnTipoMenu(false, papel, "M");
	}

	public List<Menu> getMenusPais(Papel papel) {
		return (List<Menu>) menuService.findMenuByTipoAndPapelAnTipoMenu(true, papel, "M");

	}

	public String getMatriculaUsuario() {
		return matriculaUsuario;
	}

	public void setMatriculaUsuario(String matriculaUsuario) {
		this.matriculaUsuario = matriculaUsuario;
	}

	public MenuModel getMenuBarraHorizontal() {
		return menuBarraHorizontal;
	}

	public void setMenuBarraHorizontal(MenuModel menuBarraHorizontal) {
		this.menuBarraHorizontal = menuBarraHorizontal;
	}

	public String getLinkMenu() {
		return linkMenu;
	}

	public void setLinkMenu(String linkMenu) {
		this.linkMenu = linkMenu;
	}

	public String getNomeMenu() {
		return nomeMenu;
	}

	public void setNomeMenu(String nomeMenu) {
		this.nomeMenu = nomeMenu;
	}

	public Integer getPerfilConsulta() {
		return perfilConsulta;
	}

	public void setPerfilConsulta(Integer perfilConsulta) {
		this.perfilConsulta = perfilConsulta;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public MenuModel getModel() {
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

}
