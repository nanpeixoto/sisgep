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

/**
 * The persistent class for the UNIDADE_NEGOCIO database table.
 * 
 */
@Entity
@Table(name = "UNIDADE_NEGOCIO", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_UNIDADE_NEGOCIO", sequenceName = "SEQ_UNIDADE_NEGOCIO", allocationSize = 1)
public class UnidadeNegocio extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UNIDADE_NEGOCIO")
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@Column(name = "DESCRICAO")
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_RESPONSAVEL")
	private Usuario usuarioResponsavel;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@Column(name = "DATA_ALTERACAO")
	private Date dataAlteracao;

	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@Column(name = "STS_ATIVO")
	private String ativo;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Override
	public Object getId() {
		return sequencia;
	}

	@Override
	public void setId(Object id) {
		sequencia = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		return "descricao";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getDescricaoStatus() {
		if (getAtivo().equalsIgnoreCase("S"))
			return "Ativo".toUpperCase();
		else
			return "Inativo".toUpperCase();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Papel)) {
			return false;
		}
		UnidadeNegocio castOther = (UnidadeNegocio) other;
		return this.sequencia.equals(castOther.sequencia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.sequencia.hashCode();

		return hash;
	}

	public Usuario getUsuarioResponsavel() {
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(Usuario usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

}