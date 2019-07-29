package by.anelkin.easylearning.servlet;



import by.anelkin.easylearning.Main;
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

@Log4j
@WebServlet(urlPatterns = "/img/*")
public class ImgServlet extends HttpServlet {
    private static final String PROP_FILE_FOLDER = "file_folder";

    // FIXME: 7/28/2019 константа не final? не инициализируется в конструкторе
    //  (но сервлет живет все время так что нормально?)
    private String fileStorage;

    @Override
    public void init() throws ServletException {
        Properties prop = new Properties();
        try {
            prop.load(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("file_storage.properties")));
            fileStorage = prop.getProperty(PROP_FILE_FOLDER);
        } catch (IOException e) {
           log.error("Unable to read file storage path from properties!");
        }
    }

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
