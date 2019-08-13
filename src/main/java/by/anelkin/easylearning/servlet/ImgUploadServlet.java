package by.anelkin.easylearning.servlet;


import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.filter.JspAccessFilter;
import by.anelkin.easylearning.receiver.RequestReceiver;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import lombok.extern.log4j.Log4j;

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
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.command.CommandFactory.CommandType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.FORWARD;
import static by.anelkin.easylearning.util.GlobalConstant.*;
import static by.anelkin.easylearning.util.GlobalConstant.BUNDLE_PAGE_NOT_FOUND;

/**
 * Assistance servlet of the controller layer.
 * Responsible for downloading user's images.
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
@WebServlet(urlPatterns = "/account/change-image")
@MultipartConfig
public class ImgUploadServlet extends HttpServlet {
    private static final String TEMP_ACC_AVATAR_RELATIVE_LOCATION = "resources/account_avatar_update/";
    private static final String TEMP_COURSE_IMG_RELATIVE_LOCATION = "resources/course_img_update/";
    private String fileStorage;


    /**
     * inits absolute path to external file storage from properties file
     *
     * @throws ServletException if problems with property file
     */
    @Override
    public void init() throws ServletException {
        Properties prop = new Properties();
        try {
            prop.load(Objects.requireNonNull(ImgUploadServlet.class.getClassLoader().getResourceAsStream("file_storage.properties")));
            fileStorage = prop.getProperty(PROP_FILE_FOLDER);
        } catch (IOException e) {
            log.error("Unable to read file storage path from properties!");
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new ServletException("Expected POST method. Instead got GET method.");
    }

    /**
     * process downloading of file to the directory with images on review
     * , invoke suitable {@link by.anelkin.easylearning.command.Command}
     *
     * @param req  current {@link HttpServletRequest}
     * @param resp current {@link HttpServletResponse}
     * @throws ServletException replaced with {@link HttpServletResponse} sending error
     * @throws IOException  replaced with {@link HttpServletResponse} sending error
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = (Account) req.getSession().getAttribute(ATTR_USER);
        String commandName = req.getParameter(ATTR_COMMAND_NAME);
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, req.getLocale());
        CommandType command;
        try {
            command = CommandType.valueOf(commandName.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(e);
            req.setAttribute(ATTR_MESSAGE, rb.getString(BUNDLE_ETERNAL_SERVER_ERROR));
            resp.sendError(ERROR_500);
            return;
        } catch (NullPointerException e) {
            log.error(e);
            req.setAttribute(ATTR_MESSAGE, rb.getString(BUNDLE_PAGE_NOT_FOUND));
            resp.sendError(ERROR_404);
            return;
        }

        String courseId;
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
        } catch (ServiceException e) {
            log.error("Command: " + command + ". " + e);
            req.setAttribute(ATTR_MESSAGE, rb.getString(BUNDLE_ETERNAL_SERVER_ERROR));
            resp.sendError(ERROR_500);
            return;
        }

        requestContent.insertAttributes(req);
        String path = requestContent.getPath();
        if (responseType == FORWARD) {
            req.setAttribute(JspAccessFilter.ATTR_JSP_PERMITTED, true);
            req.getRequestDispatcher(path).forward(req, resp);
        } else {
            String url = req.getContextPath() + path;
            resp.sendRedirect(url);
        }
    }
}
