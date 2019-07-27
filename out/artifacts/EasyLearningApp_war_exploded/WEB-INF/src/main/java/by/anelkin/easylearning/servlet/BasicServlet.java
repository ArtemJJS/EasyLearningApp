package by.anelkin.easylearning.servlet;

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
        , "/admin/approve-acc-avatar", "/admin/approve-course-img", "/admin/course-approval"})
public class BasicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("role") == null) {
            session.setAttribute("role", GUEST);
            session.setAttribute("locale", new Locale("en", "US"));
        }

        CommandType commandType;
        try {
            commandType = CommandType.valueOf(request.getParameter("command_name").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ServletException("Wrong command name!!!");
        }

        Account.AccountType accType = (Account.AccountType) session.getAttribute("role");
        if (!commandType.getAccessTypes().contains(accType)) {
            throw new ServletException("Access DENIED!!!");
        }
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
