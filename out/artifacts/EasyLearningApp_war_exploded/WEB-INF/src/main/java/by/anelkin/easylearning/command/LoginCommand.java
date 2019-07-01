package by.anelkin.easylearning.command;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exeption.RepositoryException;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class LoginCommand implements Command {
    private static final String REDIRECT_PATH = "http://localhost:8080/easyLearning/account";
    private static final String WRONG_LOGIN_PATH = "http://localhost:8080/easyLearning/?wrong-login=true";

    @Override
    public String execute(HttpServletRequest request) throws RepositoryException {
        AccRepository repository = new AccRepository();
        String login = request.getParameter("login");
        List<Account> accounts = repository.query(new SelectAccByLoginSpecification(login));
        if (accounts.size() != 1) {
            // TODO: 7/1/2019 как сделать чтобы неверный логин использовал forward?
            return WRONG_LOGIN_PATH;
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", accounts.get(0));
        return REDIRECT_PATH;
    }
}
