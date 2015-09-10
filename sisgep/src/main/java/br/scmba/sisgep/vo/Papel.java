package br.scmba.sisgep.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.util.Constantes;

@Entity
@Table(name = "PAPEL", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_PAPEL", sequenceName = "SEQ_PAPEL", allocationSize = 1)
public class Papel extends BaseEntity {

	private static final long serialVersionUID = 2064857479718901212L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAPEL")
	@Column(name = "sequencia")
	private Integer id;

	@Column(name = "descricao", length = 20)
	private String descricao;

	@Column(name = "sigla", length = 6)
	private String sigla;

	@OneToMany(mappedBy = "papel", cascade = CascadeType.ALL, orphanRemoval = true)
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	private List<PapelMenu> papelMenu;

	@Column(name = "sts_ativo")
	private String ativo;

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Papel)) {
			return false;
		}
		Papel castOther = (Papel) other;
		return this.id.equals(castOther.id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id.hashCode();

		return hash;
	}

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
		return "descricao";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public List<PapelMenu> getPapelMenu() {
		return papelMenu;
	}

	public void setPapelMenu(List<PapelMenu> papelMenu) {
		this.papelMenu = papelMenu;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
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
