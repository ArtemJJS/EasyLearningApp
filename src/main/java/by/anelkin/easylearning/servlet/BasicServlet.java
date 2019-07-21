package by.anelkin.easylearning.servlet;

import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.RequestReceiver;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.entity.Account.AccountType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;

@Log4j
@WebServlet(name = "BasicServlet", urlPatterns = {"/basic_servlet", "/search"})
public class BasicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getHeader("referer"));

        if (request.getSession().getAttribute("role") == null) {
            request.getSession().setAttribute("role", GUEST);
        }

        CommandType commandType = CommandType.valueOf(request.getParameter("command_name").toUpperCase());
        log.debug("Server received command: " + commandType);
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        RequestReceiver receiver = new RequestReceiver(commandType, requestContent);
        ResponseType responseType;
        try {
            responseType = receiver.executeCommand();
        } catch (RepositoryException | ServiceException e) {
            // TODO: 7/5/2019 сделать редирект на страницу ошибки
            throw new ServletException(e);
        }

        requestContent.insertAttributes(request);
        String path = requestContent.getPath();
        if (responseType == FORWARD) {
            request.getRequestDispatcher(path).forward(request, response);
        } else {
            log.debug("Sending redirect: " + path);
            response.sendRedirect(path);
        }
    }
}
