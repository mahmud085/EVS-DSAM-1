package com.example.appengine.java8.servlet;

import com.example.appengine.java8.services.UploadEmail;
import com.opencsv.CSVReader;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

public class UploadFileServelet extends HttpServlet {
    private static Logger logger = Logger.getLogger(UploadFileServelet.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (ServletFileUpload.isMultipartContent(req)) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            UploadEmail uploaddata = new UploadEmail();
            try {
                List<FileItem> multiparts = upload.parseRequest(req);
                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        new File(item.getName()).getName();
                        CSVReader csvReader = new CSVReader(new InputStreamReader(item.getInputStream()));
                        String[] nextRecord;
                        while ((nextRecord = csvReader.readNext()) != null) {
                            uploaddata.uploadFile(nextRecord);
                        }
                    }
                }
            } catch (Exception e) {
                logger.severe("Error importing file" + e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/voterlist");
    }
}
