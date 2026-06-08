package br.edu.cs.poo.ac.bolsa.dao;

import java.io.Serializable;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;

public class DAOInvestidorPessoa extends DAOGenerico {

    public DAOInvestidorPessoa() {
        inicializarCadastro(InvestidorPessoa.class);
    }

    public InvestidorPessoa buscar(String cpf) {
        return (InvestidorPessoa) cadastro.buscar(cpf);
    }
    
    public InvestidorPessoa buscarInvestidorPessoa(String cpf) {
        return buscar(cpf);
    }

    public boolean incluir(InvestidorPessoa pessoa) {
        if (buscar(pessoa.getCpf()) == null) {
            cadastro.incluir(pessoa, pessoa.getCpf());
            return true;
        } else {
            return false;
        }
    }

    public boolean incluirInvestidorPessoa(InvestidorPessoa pessoa) {
        return incluir(pessoa);
    }

    public boolean alterar(InvestidorPessoa pessoa) {
        if (buscar(pessoa.getCpf()) != null) {
            cadastro.alterar(pessoa, pessoa.getCpf());
            return true;
        } else {
            return false;
        }
    }

    public boolean excluir(String cpf) {
        if (buscar(cpf) != null) {
            cadastro.excluir(cpf);
            return true;
        } else {
            return false;
        }
    }

    public InvestidorPessoa[] consultarTodos() {
        Serializable[] todos = cadastro.buscarTodos();
        if (todos == null) return new InvestidorPessoa[0];
        InvestidorPessoa[] resultado = new InvestidorPessoa[todos.length];
        for (int i = 0; i < todos.length; i++) {
            resultado[i] = (InvestidorPessoa) todos[i];
        }
        return resultado;
    }
}
