package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exception.RepositoryException;

import javax.servlet.http.HttpServletRequest;

public interface Command {
    String execute(HttpServletRequest request) throws RepositoryException;
}
