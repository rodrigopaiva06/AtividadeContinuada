package br.edu.cs.poo.ac.bolsa.dao;

import br.edu.cs.poo.ac.bolsa.entidade.InvestidorEmpresa;

public class DAOInvestidorEmpresa extends DAOGenerico {

    public DAOInvestidorEmpresa() {
        inicializarCadastro(InvestidorEmpresa.class); 
    }

    public InvestidorEmpresa buscar(String cnpj) {
        return (InvestidorEmpresa) cadastro.buscar(cnpj); 
    }

    public boolean incluir(InvestidorEmpresa empresa) {
        if (buscar(empresa.getCnpj()) == null) { 
            cadastro.incluir(empresa, empresa.getCnpj()); 
            return true;
        }

        else {
            return false; 
        }
    }

    public boolean alterar(InvestidorEmpresa empresa) {
        if (buscar(empresa.getCnpj()) != null) { 
            cadastro.alterar(empresa, empresa.getCnpj()); 
            return true;
        }

        else {
            return false; 
        }
    }

    public boolean excluir(String cnpj) {
        if (buscar(cnpj) != null) { 
            cadastro.excluir(cnpj); 
            return true;
        }

        else {
            return false;
        }
    }
}