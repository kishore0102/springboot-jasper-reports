package dev.reports.Jasper.controllers;

import dev.reports.Jasper.models.Details;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class JasperController {

    @GetMapping("/report1")
    public void generateReport1(@RequestParam Integer id, @RequestParam String name,
                                  @RequestParam String designation, @RequestParam String department,
                                  HttpServletResponse response) throws JRException, IOException {
        Details detail = new Details(id, name, designation, department);
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"report.pdf\""));
        OutputStream out = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(generateJasperReport(Arrays.asList(detail)), out);
    }

    private JasperPrint generateJasperReport(List<Details> detailsList) throws JRException {
        final InputStream stream = this.getClass().getResourceAsStream("/report1.jrxml");
        final JasperReport report = JasperCompileManager.compileReport(stream);
        final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(detailsList);
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "happydevs");
        final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
        final String fileName = "report_" + LocalDateTime.now() + ".pdf";
        JasperExportManager.exportReportToPdfFile(print, fileName);
        return print;
    }

}
