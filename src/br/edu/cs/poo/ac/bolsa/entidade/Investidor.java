package br.edu.cs.poo.ac.bolsa.entidade; 

import java.io.Serializable;

import java.math.BigDecimal; 
import java.time.LocalDate; 

public class Investidor implements Serializable { 
    
    private String nome;
    private Endereco endereco; 
    
    private LocalDate dataCriacao;
    private BigDecimal bonus = BigDecimal.ZERO; 
    private Contatos contatos; 
    
    public Investidor() {}
    
    public Investidor(String nome, Endereco endereco,
                      LocalDate dataCriacao, BigDecimal bonus,
                      Contatos contatos) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus;
        this.contatos = contatos;
    }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }

    public Contatos getContatos() { return contatos; }
    public void setContatos(Contatos contatos) { this.contatos = contatos; }
    
    protected LocalDate getDataCriacao() { return dataCriacao; }
    protected void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public BigDecimal getBonus() { return bonus; }
    
    public int getIdade() {
    	return LocalDate.now().getYear() - dataCriacao.getYear();
    }
    
    public void creditarBonus(BigDecimal valor) {
        if (valor == null) return;
        this.bonus = this.bonus.add(valor); 
        
    }

    public void debitarBonus(BigDecimal valor) {
        if (valor == null) return;
        this.bonus = this.bonus.subtract(valor); 
        
    }
}