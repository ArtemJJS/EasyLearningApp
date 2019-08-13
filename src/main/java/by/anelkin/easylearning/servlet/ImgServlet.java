package by.anelkin.easylearning.servlet;



import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

/**
 * Assistance servlet of the controller layer.
 * Responsible for representing of images in the interface.
 *
 * @author Artsiom Anelkin on 2019-08-12.
 * @version 0.1
 */
@Log4j
@WebServlet(urlPatterns = "/img/*")
public class ImgServlet extends HttpServlet {
    private static final String PROP_FILE_FOLDER = "file_folder";
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
            prop.load(Objects.requireNonNull(ImgServlet.class.getClassLoader().getResourceAsStream("file_storage.properties")));
            fileStorage = prop.getProperty(PROP_FILE_FOLDER);
        } catch (IOException e) {
           log.error("Unable to read file storage path from properties!");
        }
    }

    /**
     * Responsible representing of images in the interface.
     *
     * @param request  current {@link HttpServletRequest}
     * @param response current {@link HttpServletResponse}
     * @throws ServletException if faced problems
     * @throws IOException  if faced problems
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String filename = URLDecoder.decode(request.getPathInfo().substring(1));
        File file = new File(fileStorage, filename);
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName() + "");
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
