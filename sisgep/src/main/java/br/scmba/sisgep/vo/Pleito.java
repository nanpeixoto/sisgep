package br.scmba.sisgep.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import br.scmba.sisgep.enumerator.TipoStatusPleitoEnum;

/**
 * The persistent class for the PLEITO database table.
 * 
 */
@Entity
@Table(name = "PLEITO", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_PLEITO", sequenceName = "SEQ_PLEITO", allocationSize = 1)
public class Pleito extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PLEITO")
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@ManyToOne
	@JoinColumn(name = "SEQ_SOLICITANTE")
	private Solicitante solicitante;

	@Column(name = "OBJETO")
	private String objeto;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@Column(name = "DATA_ALTERACAO")
	private Date dataAlteracao;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_ALTERACAO")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_CADASTRO")
	private Usuario usuarioCadastro;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_EXCLUSAO")
	private Usuario usuarioExclusao;

	@Column(name = "DATA_EXCLUSAO")
	private Date dataExclusao;

	@Column(name = "STS_PLEITO")
	private String stsPleito;

	@Column(name = "DATA_FINALIZACAO")
	private Date dataFinalizacao;

	@Column(name = "STS_ATIVO")
	private String ativo;

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_FINALIZACAO")
	private Usuario usuarioFinalizacao;

	@ManyToOne
	@JoinColumn(name = "SEQ_PLEITO_STATUS_ULTIMO")
	private PleitoMovimentacao ultimaMovimentacao;

	@OneToMany(mappedBy = "pleito")
	private List<PleitoMovimentacao> pleitoMovimentacao;

	@ManyToOne
	@JoinColumn(name = "SEQ_ULTIMA_UNIDADE_NEGOCIO")
	private UnidadeNegocio ultimaUnidadeNegocio;

	public Solicitante getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Solicitante solicitante) {
		this.solicitante = solicitante;
	}

	public String getObjeto() {
		if (objeto != null)
			return objeto.toUpperCase();
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto.toUpperCase();
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Usuario getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(Usuario usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public String getStsPleito() {
		return stsPleito;
	}

	public void setStsPleito(String stsPleito) {
		this.stsPleito = stsPleito;
	}

	public Date getDataFinalizacao() {
		return dataFinalizacao;
	}

	public void setDataFinalizacao(Date dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}

	public Usuario getUsuarioFinalizacao() {
		return usuarioFinalizacao;
	}

	public void setUsuarioFinalizacao(Usuario usuarioFinalizacao) {
		this.usuarioFinalizacao = usuarioFinalizacao;
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
		return "dataCadastro";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	public UnidadeNegocio getUltimaUnidadeNegocio() {
		return ultimaUnidadeNegocio;
	}

	public void setUltimaUnidadeNegocio(UnidadeNegocio ultimaUnidadeNegocio) {
		this.ultimaUnidadeNegocio = ultimaUnidadeNegocio;
	}

	public Long getSequencia() {
		return sequencia;
	}

	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
	}

	public PleitoMovimentacao getUltimaMovimentacao() {
		return ultimaMovimentacao;
	}

	public void setUltimaMovimentacao(PleitoMovimentacao ultimaMovimentacao) {
		this.ultimaMovimentacao = ultimaMovimentacao;
	}

	public List<PleitoMovimentacao> getPleitoMovimentacao() {
		return pleitoMovimentacao;
	}

	public void setPleitoMovimentacao(List<PleitoMovimentacao> pleitoMovimentacao) {
		this.pleitoMovimentacao = pleitoMovimentacao;
	}

	public String getDescricaoStatus() {
		return TipoStatusPleitoEnum.getDescricaoByValor(stsPleito).toUpperCase();
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Usuario getUsuarioExclusao() {
		return usuarioExclusao;
	}

	public void setUsuarioExclusao(Usuario usuarioExclusao) {
		this.usuarioExclusao = usuarioExclusao;
	}

	public Date getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

}