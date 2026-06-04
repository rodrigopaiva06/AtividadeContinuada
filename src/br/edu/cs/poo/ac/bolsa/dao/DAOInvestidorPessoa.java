package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorPessoa;

public class DAOInvestidorPessoa extends DAOGenerico {

    public DAOInvestidorPessoa() {
        inicializarCadastro(InvestidorPessoa.class); 
    }

    public InvestidorPessoa buscar(String cpf) {
        return (InvestidorPessoa) cadastro.buscar(cpf); 
    }

    public boolean incluir(InvestidorPessoa pessoa) {
        if (buscar(pessoa.getCpf()) == null) { 
            cadastro.incluir(pessoa, pessoa.getCpf()); 
            return true;
        }

        else {
            return false; 
        }
    }

    public boolean alterar(InvestidorPessoa pessoa) {
        if (buscar(pessoa.getCpf()) != null) { 
            cadastro.alterar(pessoa, pessoa.getCpf()); 
            return true;
        }

        else {
            return false; 
        }
    }

    public boolean excluir(String cpf) {
        if (buscar(cpf) != null) { 
            cadastro.excluir(cpf); 
            return true;
        }

        else {
            return false; 
        }
    }
}