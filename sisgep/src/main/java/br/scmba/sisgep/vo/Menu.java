package br.scmba.sisgep.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.util.Constantes;


/**
 * The persistent class for the SISGEP_MENU database table.
 * 
 */
@Entity
@Table(name="MENU", schema=Constantes.SCHEMADB_NAME)
@SequenceGenerator(name="SEQ_MENU", sequenceName="SEQ_MENU", allocationSize=1)
public class Menu extends BaseEntity  implements Comparable<Menu>{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_MENU")
	@Column(name="SEQUENCIA")
	private Long sequencia;
	
	@Column(name="ACTION")
	private String action;
	
	@ManyToOne
	@JoinColumn(name = "SEQ_MENU_PAI")
	private Menu menuPai;
	
	@Column(name="ICONE")
	private String icone;
	
	@Column(name="LINK")
	private String link;
	
	@Column(name="NOME")
	private String nome;
	
	@Column(name="ORDEM")
	private Long ordem;
	
	//Refere-se ao tipo ou é (M)enu ou é (T)ela interna 
	@Column(name="tipo")
	private String tipo;
	
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	private List<PapelMenu> listaPapelMenu;


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Menu getMenuPai() {
		return menuPai;
	}

	public void setMenuPai(Menu menuPai) {
		this.menuPai = menuPai;
	}

	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public Object getId() {
		return sequencia;
	}

	@Override
	public void setId(Object id) {
		this.sequencia = (Long) id;
		
	}

	@Override
	public String getColumnOrderBy() {
		return "ordem";
	}

	@Override
	public String getAuditoria() {
		return null;
	}
	
	@Override
	public int compareTo(Menu o) {
		return this.getOrdem().compareTo(o.getOrdem());
	}

	public List<PapelMenu> getListaPapelMenu() {
		return listaPapelMenu;
	}

	public void setListaPapelMenu(List<PapelMenu> listaPapelMenu) {
		this.listaPapelMenu = listaPapelMenu;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDataAlteracao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario getUsuarioCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDataCadastro() {
		// TODO Auto-generated method stub
		return null;
	}

	
	





}