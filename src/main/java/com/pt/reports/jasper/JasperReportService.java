package com.pt.reports.jasper;

import com.pt.reports.utils.JasperReportUtil;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    public void generateReport(List<?> objects, String format, String reportName, HttpServletResponse response) {
        JasperReport jasperReport = getJasperReport(reportName);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
        Map<String, Object> parameters = createParameters(reportName);

        try {
            JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataSource);
            exportReport(jasperPrint, format, reportName, response);
        } catch (JRException | IOException e) {
            throw new RuntimeException("Failed to generate or export report", e);
        }
    }

    private JasperReport getJasperReport(String reportName) {
        try {
            JasperReport jasperReport = JasperReportUtil.loadCompiledReport(reportName);
            if (jasperReport == null) {
                jasperReport = JasperReportUtil.compileReport(reportName);
            }
            return jasperReport;
        } catch (IOException | JRException e) {
            throw new RuntimeException("Failed to load or compile Jasper report", e);
        }
    }

    private Map<String, Object> createParameters(String reportName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", reportName);
        return parameters;
    }

    private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {
        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    private void exportReport(JasperPrint jasperPrint, String format, String reportName, HttpServletResponse response) throws JRException, IOException {
        switch (format.toLowerCase()) {
            case "pdf":
                JasperReportUtil.exportToPdf(jasperPrint, reportName, response);
                break;
            case "xls":
            case "xlsx":
                JasperReportUtil.exportToXlsx(jasperPrint, reportName, response);
                break;
            default:
                throw new RuntimeException("Unknown report format");
        }
    }
}
