package by.anelkin.easylearning.servlet;


import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import by.anelkin.easylearning.service.AccountService;
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

@WebServlet(urlPatterns = "/upload_img_servlet")
@MultipartConfig(
        location = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/account_avatar",
        fileSizeThreshold = 1024,
        maxFileSize = 1024 * 1024,
        maxRequestSize = 1024 * 1024 * 5)
public class ImgUploadServlet extends HttpServlet {
    private static final String PREVIOUS_OPERATION_MSG = "previous_operation_message";
    private static final String ATTR_USER = "user";
    private static final String ATTR_FILE_NAME = "file_name";
    private static final String ERROR_MSG = "You must be a registered user to process file uploading!";
    private static final String FILE_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/account_avatar/";
    private static final String TEMP_FILE_LOCATION = "C:/Users/User/Desktop/GIT Projects/EasyLearningApp/web/resources/account_avatar/" + "temp_";
    // TODO: 7/17/2019 инициализация пути папки в init()?? но тогда нужно поле сервлету???

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new ServletException("Expected POST method. Instead got GET method.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                    String accId = String.valueOf(account.getId());

                    //write file as temp, if successful - delete previous version if exists
                    // and rename current with correct path
                    // FIXME: 7/17/2019 фото обновляется не сразу на странице аккаунта??? (через какое-то время)
                    // FIXME: 7/17/2019 кеш?? как исправить??? может это из-за дефолтного сервлета?
                    File file = new File(TEMP_FILE_LOCATION + accId + fileExtension);
                    item.write(file);
                    Files.deleteIfExists(Paths.get(FILE_LOCATION + accId + fileExtension));
                    file.renameTo(new File(FILE_LOCATION + accId + fileExtension));
                    req.setAttribute(ATTR_FILE_NAME, accId + fileExtension);
                }
            }

            SessionRequestContent requestContent = new SessionRequestContent();
            requestContent.extractValues(req);
            (new AccountService()).updateAccImage(requestContent);
            requestContent.insertAttributes(req);
            resp.sendRedirect(req.getContextPath() + "/account");
        } catch (Exception e) {
            // FIXME: 7/17/2019
            throw new ServletException(e);
        }


    }
}
