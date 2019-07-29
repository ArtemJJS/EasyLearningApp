package by.anelkin.easylearning.servlet;


import by.anelkin.easylearning.Main;
import by.anelkin.easylearning.command.CommandFactory;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.filter.JspAccessFilter;
import by.anelkin.easylearning.receiver.RequestReceiver;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import lombok.extern.log4j.Log4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.command.CommandFactory.CommandType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.FORWARD;

@Log4j
@WebServlet(urlPatterns = "/account/change-image")
@MultipartConfig
public class ImgUploadServlet extends HttpServlet {
    private static final String ATTR_USER = "user";
    private static final String ATTR_COMMAND_NAME = "command_name";
    private static final String ATTR_COURSE_ID = "course_id";
    private static final String ATTR_IMG_TO_UPLOAD = "img_to_upload";
    private static final String ATTR_FILE_EXTENSION = "file_extension";
    private static final String PROP_FILE_FOLDER = "file_folder";
    private static final String TEMP_ACC_AVATAR_RELATIVE_LOCATION = "resources/account_avatar_update/";
    private static final String TEMP_COURSE_IMG_RELATIVE_LOCATION = "resources/course_img_update/";

    // FIXME: 7/28/2019 что на счет static?
    private String fileStorage;


    @Override
    public void init() throws ServletException {
        Properties prop = new Properties();
        try {
            prop.load(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("file_storage.properties")));
        } catch (IOException e) {
            log.error("Unable to read file storage path from properties!");
        }
        fileStorage = prop.getProperty(PROP_FILE_FOLDER);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new ServletException("Expected POST method. Instead got GET method.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = (Account) req.getSession().getAttribute(ATTR_USER);
        String commandName = req.getParameter(ATTR_COMMAND_NAME);
        CommandType command = CommandType.valueOf(commandName.toUpperCase());
        String courseId = null;
        String currentFolderPath;
        String tempFileName;
        if (command == CHANGE_COURSE_IMG) {
            courseId = req.getParameter(ATTR_COURSE_ID);
            tempFileName = courseId;
            currentFolderPath = fileStorage + TEMP_COURSE_IMG_RELATIVE_LOCATION;
            req.setAttribute(ATTR_COURSE_ID, courseId);
        } else {
            tempFileName = String.valueOf(acc.getId());
            currentFolderPath = fileStorage + TEMP_ACC_AVATAR_RELATIVE_LOCATION;
        }
        Part filePart = req.getPart(ATTR_IMG_TO_UPLOAD);
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = fileName.substring((fileName.lastIndexOf(".")));

        InputStream in = filePart.getInputStream();
        File file = new File(currentFolderPath + tempFileName + extension);
        Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        req.setAttribute(ATTR_FILE_EXTENSION, extension);
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(req);
        RequestReceiver receiver = new RequestReceiver(command, requestContent);
        SessionRequestContent.ResponseType responseType;
        try {
            responseType = receiver.executeCommand();
        } catch (RepositoryException | ServiceException e) {
            throw new ServletException(e);
        }

        requestContent.insertAttributes(req);
        String path = requestContent.getPath();
        if (responseType == FORWARD) {
            log.debug("Sending forward: " + path);
            req.setAttribute(JspAccessFilter.ATTR_JSP_PERMITTED, true);
            req.getRequestDispatcher(path).forward(req, resp);
        } else {
            String url = req.getContextPath() + path;
            log.debug("Sending redirect: " + url);
            resp.sendRedirect(url);
        }
    }
}
