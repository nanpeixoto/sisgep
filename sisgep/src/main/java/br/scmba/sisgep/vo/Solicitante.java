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
import br.scmba.util.Util;

/**
 * The persistent class for the SISGEP_MENU database table.
 * 
 */
@Entity
@Table(name = "SOLICITANTE", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_SOLICITANTE", sequenceName = "SEQ_SOLICITANTE", allocationSize = 1)
public class Solicitante extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SOLICITANTE")
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "TELEFONE")
	private String telefone;

	@Column(name = "STS_ATIVO")
	private String ativo;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = Util.removeAcentos(nome).toUpperCase();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
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
		// TODO Auto-generated method stub
		return sequencia;
	}

	@Override
	public void setId(Object id) {
		this.sequencia = (Long) id;

	}

	@Override
	public String getColumnOrderBy() {
		// TODO Auto-generated method stub
		return "nome";
	}

	@Override
	public String getAuditoria() {
		// TODO Auto-generated method stub
		return null;
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
		if (!(other instanceof Solicitante)) {
			return false;
		}
		Solicitante castOther = (Solicitante) other;
		return this.sequencia.equals(castOther.sequencia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.sequencia.hashCode();

		return hash;
	}

}