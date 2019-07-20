package by.anelkin.easylearning.servlet;


import by.anelkin.easylearning.command.CommandFactory;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.receiver.RequestReceiver;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.service.AccountService;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.command.CommandFactory.CommandType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.FORWARD;

@WebServlet(urlPatterns = "/upload_img_servlet")
@MultipartConfig(
        location = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/account_avatar",
        fileSizeThreshold = 1024,
        maxFileSize = 1024 * 1024,
        maxRequestSize = 1024 * 1024 * 5)
public class ImgUploadServlet extends HttpServlet {
    // TODO: 7/19/2019 может форвардить с сообщением?
    private static final String REDIRECT_TO = "/account";
    private static final String PREVIOUS_OPERATION_MSG = "Image added to review.";
    private static final String ATTR_USER = "user";
    private static final String ATTR_FILE_EXTENSION = "file_extension";
    private static final String ATTR_FILE_NAME = "file_name";
    private static final String ERROR_MSG = "You must be a registered user to process file uploading!";
    // FIXME: 7/19/2019 относительный путь
    private static final String ACC_AVATAR_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/account_avatar/";
    private static final String TEMP_ACC_AVATAR_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/account_avatar_update/";
    private static final String TEMP_FILE_PREFIX = "temp_";
    private static final String TEMP_COURSE_IMG_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/course_img_update/";
    // TODO: 7/17/2019 инициализация пути папки в init()?? но тогда нужно поле сервлету???

    private String commandStr;
    private String courseId;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new ServletException("Expected POST method. Instead got GET method.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        commandStr = req.getHeader("referer");
        CommandType commandType = commandStr.endsWith("change-picture") ? CHANGE_ACC_IMG : CHANGE_COURSE_IMG;
        if (commandType == CHANGE_COURSE_IMG) {
            String tempCourseId = commandStr.substring(commandStr.lastIndexOf("-") + 1);
            courseId = tempCourseId.substring(tempCourseId.lastIndexOf("=") + 1);
        }
        boolean isMultiPart = ServletFileUpload.isMultipartContent(req);
        if (!isMultiPart) {
            throw new ServletException("Request must be multipart!");
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
//        factory.setSizeThreshold(1024);
//        // Location to save data that is larger than maxMemSize.
//        factory.setRepository(new File("c:/temp"));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1024 * 1024);

        // TODO: 7/17/2019 обработки файла(правильное расширение, размер и т.д.)
        try {
            List<FileItem> fileItems = upload.parseRequest(req);
            Iterator iter = fileItems.iterator();
            Account account = (Account) req.getSession().getAttribute(ATTR_USER);
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {
                    if (account == null) {
                        throw new ServletException(ERROR_MSG);
                    }
                    String fileName = item.getName();
                    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                    // put it to request, expected in service
                    req.setAttribute(ATTR_FILE_EXTENSION, fileExtension);
                    req.setAttribute("course_id", courseId);


                    // FIXME: 7/17/2019 фото обновляется не сразу на странице аккаунта??? (через какое-то время)
                    // FIXME: 7/17/2019 кеш?? как исправить?

                    String currId;
                    String currFilePath;

                    switch (commandType) {
                        case CHANGE_ACC_IMG:
                            currId = String.valueOf(account.getId());
                            currFilePath = TEMP_ACC_AVATAR_LOCATION + currId + fileExtension;
                            break;
                        case CHANGE_COURSE_IMG:
                            currId = String.valueOf(courseId);
                            currFilePath = TEMP_COURSE_IMG_LOCATION + currId + fileExtension;
                            break;
                        default:
                            throw new ServletException("Unexpected command: " + commandType);
                    }

                    Files.deleteIfExists(Paths.get(currFilePath));
                    File file = new File(currFilePath);
                    item.write(file);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(req);
        RequestReceiver receiver = new RequestReceiver(commandType, requestContent);
        SessionRequestContent.ResponseType responseType;
        try {
            responseType = receiver.executeCommand();
        } catch (RepositoryException | ServiceException e) {
            throw new ServletException(e);
        }

        requestContent.insertAttributes(req);
        String path = requestContent.getPath();
        if (responseType == FORWARD) {
            req.getRequestDispatcher(path).forward(req, resp);
        } else {
            resp.sendRedirect(path);
        }
    }
}
