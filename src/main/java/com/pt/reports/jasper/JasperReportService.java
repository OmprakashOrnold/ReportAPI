package com.pt.reports.jasper;

import com.pt.reports.utils.JasperReportUtil;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportService {

    private static final Logger logger = LoggerFactory.getLogger(JasperReportService.class);

    public void generateReport(List<?> objects, String format, String reportName, HttpServletResponse response) {
        logger.info("Starting report generation for reportName: {}, format: {}", reportName, format);
        JasperReport jasperReport = getJasperReport(reportName);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(objects);
        Map<String, Object> parameters = createParameters(reportName);

        try {
            JasperPrint jasperPrint = fillReport(jasperReport, parameters, dataSource);
            exportReport(jasperPrint, format, reportName, response);
            logger.info("Report generation completed successfully for reportName: {}, format: {}", reportName, format);
        } catch (JRException | IOException e) {
            logger.error("Failed to generate or export report for reportName: {}, format: {}", reportName, format, e);
            throw new RuntimeException("Failed to generate or export report", e);
        }
    }

    private JasperReport getJasperReport(String reportName) {
        try {
            logger.info("Loading compiled report for reportName: {}", reportName);
            JasperReport jasperReport = JasperReportUtil.loadCompiledReport(reportName);
            if (jasperReport == null) {
                logger.info("Compiled report not found, compiling report for reportName: {}", reportName);
                jasperReport = JasperReportUtil.compileReport(reportName);
            }
            return jasperReport;
        } catch (IOException | JRException e) {
            logger.error("Failed to load or compile report for reportName: {}", reportName, e);
            throw new RuntimeException("Failed to load or compile Jasper report", e);
        }
    }

    private Map<String, Object> createParameters(String reportName) {
        logger.debug("Creating parameters for reportName: {}", reportName);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", reportName);
        return parameters;
    }

    private JasperPrint fillReport(JasperReport jasperReport, Map<String, Object> parameters, JRBeanCollectionDataSource dataSource) throws JRException {
        logger.debug("Filling report for reportName: {}", jasperReport.getName());
        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    private void exportReport(JasperPrint jasperPrint, String format, String reportName, HttpServletResponse response) throws JRException, IOException {
        logger.info("Exporting report for reportName: {}, format: {}", reportName, format);
        switch (format.toLowerCase()) {
            case "pdf":
                JasperReportUtil.exportToPdf(jasperPrint, reportName, response);
                break;
            case "xls":
            case "xlsx":
                JasperReportUtil.exportToXlsx(jasperPrint, reportName, response);
                break;
            default:
                logger.error("Unknown report format: {}", format);
                throw new RuntimeException("Unknown report format");
        }
    }
}
