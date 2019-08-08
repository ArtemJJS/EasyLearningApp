package by.anelkin.easylearning.servlet;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.filter.JspAccessFilter;
import by.anelkin.easylearning.receiver.RequestReceiver;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.entity.Account.AccountType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
@Log4j
@WebServlet(name = "BasicServlet", urlPatterns = {"/basic_servlet", "/search", "/change-lang"
        , "/admin/*", "/user/deposit", "/author/cash-out", "/author/add-course", "/account/change-pass", "/restore-password"
        , "/change-forgotten-password"})
public class BasicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().closePool();
    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("role") == null) {
            session.setAttribute("role", GUEST);
            session.setAttribute("locale", new Locale("en", "US"));
        }

        CommandType commandType;
        try {
            commandType = CommandType.valueOf(request.getParameter("command_name").toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(e);
            throw new ServletException("Wrong command name!!!");
        }

        Account.AccountType accType = (Account.AccountType) session.getAttribute("role");
        if (!commandType.getAccessTypes().contains(accType)) {
            log.warn("Access denied to command: " + commandType);
            throw new ServletException("Access DENIED!!!");
        }
        log.debug("Server received command: " + commandType);
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        RequestReceiver receiver = new RequestReceiver(commandType, requestContent);
        ResponseType responseType;
        try {
            responseType = receiver.executeCommand();
        } catch (ServiceException e) {
            throw new ServletException(e);
        }

        requestContent.insertAttributes(request);
        String path = requestContent.getPath();
        if (responseType == FORWARD) {
            log.debug("Sending forward: " + path);
            request.setAttribute(JspAccessFilter.ATTR_JSP_PERMITTED, true);
            request.getRequestDispatcher(path).forward(request, response);
        } else {
            String url = request.getContextPath() + path;
            log.debug("Sending redirect: " + url);
            response.sendRedirect(url);
        }
    }
}


