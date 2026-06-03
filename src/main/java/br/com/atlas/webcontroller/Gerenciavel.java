package br.com.atlas.webcontroller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Interface que define o contrato de polimorfismo para os webcontrollers.
 * Cada controller implementa cadastrar(), atualizar() e remover()
 * operando sobre sua própria entidade:
 * - AdministratorController → Funcionario
 * - ClientController        → Cliente
 * - LibrarianController     → Livro
 */
public interface Gerenciavel {
    void cadastrar(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    void atualizar(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;

    void remover(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException;
}
