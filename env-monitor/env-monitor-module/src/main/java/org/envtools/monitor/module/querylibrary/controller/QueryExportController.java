package org.envtools.monitor.module.querylibrary.controller;

import org.envtools.monitor.module.ModuleConstants;
import org.envtools.monitor.module.querylibrary.services.export.ZipArchiveExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IAvdeev on 18.01.2017.
 */
@Controller
public class QueryExportController {

    @Autowired
    ZipArchiveExportService exportService;

    @RequestMapping(value = "/M_QUERY_LIBRARY/exportQueries", produces = "application/zip")
    public void getSettings(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"Queries.zip\"");
        exportService.writeArchive(response.getOutputStream());
    }
}
