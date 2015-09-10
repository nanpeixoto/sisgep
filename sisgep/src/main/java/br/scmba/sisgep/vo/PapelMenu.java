package br.scmba.sisgep.vo;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.util.Constantes;


@Entity
@Table(name = "PAPEL_MENU", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name="SEQ_PAPEL_MENU", sequenceName="SEQ_PAPEL_MENU", allocationSize=1)
public class PapelMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;

	public final static Integer PERFIL_APENAS_CONSULTA = 1;
	public final static Integer PERFIL_CADASTRO = 0;


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PAPEL_MENU")
	@Column(name = "SEQUENCIA")
	private Integer id;


	@ManyToOne
	@JoinColumn(name = "SEQ_papel")
	private Papel papel;

	@ManyToOne
	@JoinColumn(name = "SEQ_MENU")
	private Menu menu;

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void setId(Object id) {
		this.id = (Integer) id;
	}

	@Override
	public String getColumnOrderBy() {
		return null;
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
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