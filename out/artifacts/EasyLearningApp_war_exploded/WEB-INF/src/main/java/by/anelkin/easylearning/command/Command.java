package by.anelkin.easylearning.command;

import by.anelkin.easylearning.exeption.RepositoryException;

import javax.servlet.http.HttpServletRequest;

public interface Command {
    String execute(HttpServletRequest request) throws RepositoryException;
}
