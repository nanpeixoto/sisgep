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

import br.scmba.arquitetura.BaseEntity;
import br.scmba.arquitetura.util.Constantes;

@Entity
@Table(name = "USUARIO", schema = Constantes.SCHEMADB_NAME)
@SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_USUARIO", allocationSize = 1)
public class Usuario extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
	@Column(name = "SEQUENCIA")
	private Long sequencia;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "MATRICULA")
	private Long matricula;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "STS_ATIVO")
	private String ativo;

	@Column(name = "NOME_ABREVIADO")
	private String nomeAbreviado;

	@Column(name = "SENHA")
	private String senha;

	@Column(name = "TELEFONE")
	private String telefone;

	// S - SIM OU N - NÃO
	@Column(name = "STS_GESTOR_UNIDADE")
	private String stsGestorUnidade;

	@ManyToOne
	@JoinColumn(name = "seq_papel")
	private Papel papel;

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

	@Transient
	private boolean gestorUnidade;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getMatricula() {
		return this.matricula;
	}

	public void setMatricula(Long matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		if (nome != null)
			return this.nome.toUpperCase();
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getSenha() {
		return this.senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTelefone() {
		return this.telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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
		return "nome";
	}

	@Override
	public String getAuditoria() {
		return null;
	}

	public String getNomeAbreviado() {
		if (nomeAbreviado != null)
			return nomeAbreviado.toUpperCase();
		return nomeAbreviado;
	}

	public void setNomeAbreviado(String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado.toUpperCase();
	}

	public Papel getPapel() {
		return papel;
	}

	public void setPapel(Papel papel) {
		this.papel = papel;
	}

	public Long getSequencia() {
		return sequencia;
	}

	public void setSequencia(Long sequencia) {
		this.sequencia = sequencia;
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

	public String getStsGestorUnidade() {
		return stsGestorUnidade;
	}

	public void setStsGestorUnidade(String stsGestorUnidade) {
		this.stsGestorUnidade = stsGestorUnidade;
	}

	public boolean getGestorUnidade() {
		if (stsGestorUnidade != null) {
			if (stsGestorUnidade.equalsIgnoreCase("S"))
				return true;
			else
				return false;
		}
		return gestorUnidade;
	}

	public void setGestorUnidade(boolean gestorUnidade) {
		this.gestorUnidade = gestorUnidade;
	}

}