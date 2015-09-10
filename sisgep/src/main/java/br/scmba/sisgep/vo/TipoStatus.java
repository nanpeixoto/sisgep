package br.scmba.sisgep.vo;

import java.io.Serializable;
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
import javax.persistence.Transient;

import br.scmba.arquitetura.util.Constantes;
import br.scmba.sisgep.enumerator.TipoStatusPleitoEnum;

/**
 * The persistent class for the TIPO_STATUS database table.
 * 
 */
@Entity
@Table(name = "TIPO_STATUS", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_TIPO_STATUS", sequenceName = "SEQ_TIPO_STATUS", allocationSize = 1)
public class TipoStatus extends br.scmba.arquitetura.BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TIPO_STATUS")
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "STS_PLEITO")
	private String stsPleito;

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

	@Column(name = "STS_EXIGE_UNIDADE_NEGOCIO")
	private String stsExigeUnidadeNegocio;

	@Transient
	private boolean exigeUnidadeNegocio;

	public boolean getExigeUnidadeNegocio() {
		if (stsExigeUnidadeNegocio != null) {
			if (stsExigeUnidadeNegocio.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return exigeUnidadeNegocio;
	}

	public void setExigeUnidadeNegocio(boolean exigeUnidadeNegocio) {
		this.exigeUnidadeNegocio = exigeUnidadeNegocio;
	}

	public long getSequencia() {
		return this.sequencia;
	}

	public void setSequencia(long sequencia) {
		this.sequencia = sequencia;
	}

	public String getDescricao() {
		if (descricao != null)
			return this.descricao.toUpperCase();
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.toUpperCase();
	}

	public String getStsPleito() {
		return this.stsPleito;
	}

	public void setStsPleito(String stsPleito) {
		this.stsPleito = stsPleito;
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
		return "descricao";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	@Override
	public Usuario getUsuarioAlteracao() {
		return null;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
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

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getDescricaoStatusPleito() {
		return TipoStatusPleitoEnum.getDescricaoByValor(stsPleito).toUpperCase();
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
		TipoStatus castOther = (TipoStatus) other;
		return this.sequencia.equals(castOther.sequencia);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.sequencia.hashCode();

		return hash;
	}

	public String getStsExigeUnidadeNegocio() {
		return stsExigeUnidadeNegocio;
	}

	public void setStsExigeUnidadeNegocio(String stsExigeUnidadeNegocio) {
		this.stsExigeUnidadeNegocio = stsExigeUnidadeNegocio;
	}

}