package by.anelkin.easylearning.servlet;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.specification.account_spec.SelectAccByLogin;
import lombok.extern.log4j.Log4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Log4j
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        log.debug("Attempt to enter in account. Login: " + login + ", password: " + password);
        if (!checkRegistration(login, password)) {
            request.setAttribute("wrong_login_message", "Username or Password is incorrect! Try again, please!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/start_page.jsp");
            dispatcher.forward(request, response);
        } else {

        }
    }

    private boolean checkRegistration(String login, String password) {
        boolean isRegistered = false;
        AccRepository repository = new AccRepository();
        List<Account> list = repository.query(new SelectAccByLogin(login));
        if (list.size() > 0) {
            Account account = list.get(0);
            isRegistered = password.equals(account.getPassword());
            System.out.println(account.getPassword());
        }
        System.out.println(password);
        return isRegistered;
    }
}
