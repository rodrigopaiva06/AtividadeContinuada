package br.edu.cs.poo.ac.bolsa.dao;

import java.io.Serializable;
import java.lang.reflect.Array;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoObjetoJaExistente;
import br.edu.cs.poo.ac.bolsa.util.ExcecaoOobjetoNaoExistente;
import br.edu.cs.poo.ac.bolsa.util.Registro;

public class DAO<T extends Registro> {

    private CadastroObjetos cadastro;
    private Class<T> tipoEntidade;

    public DAO(Class<T> tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
        this.cadastro = new CadastroObjetos(tipoEntidade);
    }

    public T buscar(String identificador) {
        return tipoEntidade.cast(cadastro.buscar(identificador));
    }

    public void incluir(T objeto) throws ExcecaoObjetoJaExistente {
        if (buscar(objeto.getIdentificador()) != null) {
            throw new ExcecaoObjetoJaExistente();
        }
        cadastro.incluir(objeto, objeto.getIdentificador());
    }

    public void alterar(T objeto) throws ExcecaoOobjetoNaoExistente {
        if (buscar(objeto.getIdentificador()) == null) {
            throw new ExcecaoOobjetoNaoExistente();
        }
        cadastro.alterar(objeto, objeto.getIdentificador());
    }

    public void excluir(String identificador) throws ExcecaoOobjetoNaoExistente {
        if (buscar(identificador) == null) {
            throw new ExcecaoOobjetoNaoExistente();
        }
        cadastro.excluir(identificador);
    }

    @SuppressWarnings("unchecked")
    public T[] buscarTodos() {
        Serializable[] todos = cadastro.buscarTodos();
        if (todos == null) return null;
        T[] resultado = (T[]) Array.newInstance(tipoEntidade, todos.length);
        for (int i = 0; i < todos.length; i++) {
            resultado[i] = tipoEntidade.cast(todos[i]);
        }
        return resultado;
    }
}
