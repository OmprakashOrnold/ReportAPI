package com.pt.reports.utils;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Utility class for JasperReports operations.
 *
 * @author Om Prakash Peddamadthala
 * @version 1.0
 * @since 7/19/2024
 */
public class JasperReportUtil {

    private static final Logger logger = LoggerFactory.getLogger(JasperReportUtil.class);

    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String JASPER_EXTENSION = ".jasper";
    private static final String JRXML_EXTENSION = ".jrxml";
    private static final String CONTENT_TYPE_PDF = "application/pdf";
    private static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT_FILENAME_PDF = "attachment; filename=\"%s.pdf\"";
    private static final String ATTACHMENT_FILENAME_XLSX = "attachment; filename=\"%s.xlsx\"";

    public static JasperReport loadCompiledReport(String reportName) throws IOException, JRException {
        logger.info("Loading compiled report: {}", reportName);
        try {
            File reportFile = ResourceUtils.getFile(CLASSPATH_PREFIX + reportName + JASPER_EXTENSION);
            return (JasperReport) JRLoader.loadObject(reportFile);
        } catch (FileNotFoundException e) {
            logger.warn("Compiled report not found: {}", reportName, e);
            return null;
        }
    }

    public static JasperReport compileReport(String reportName) throws IOException, JRException {
        logger.info("Compiling report: {}", reportName);
        try {
            File jrxmlFile = ResourceUtils.getFile(CLASSPATH_PREFIX + reportName + JRXML_EXTENSION);
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
            JRSaver.saveObject(jasperReport, CLASSPATH_PREFIX + reportName + JASPER_EXTENSION);
            logger.info("Report compiled and saved: {}", reportName);
            return jasperReport;
        } catch (FileNotFoundException e) {
            logger.error("JRXML file not found: {}", reportName, e);
            throw e;
        }
    }

    public static void exportToPdf(JasperPrint jasperPrint, String reportName, HttpServletResponse response) throws JRException, IOException {
        logger.info("Exporting report to PDF: {}", reportName);
        response.setContentType(CONTENT_TYPE_PDF);
        response.setHeader(CONTENT_DISPOSITION, String.format(ATTACHMENT_FILENAME_PDF, reportName));
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        logger.info("Report exported to PDF: {}", reportName);
    }

    public static void exportToXlsx(JasperPrint jasperPrint, String reportName, HttpServletResponse response) throws JRException, IOException {
        logger.info("Exporting report to XLSX: {}", reportName);
        response.setContentType(CONTENT_TYPE_XLSX);
        response.setHeader(CONTENT_DISPOSITION, String.format(ATTACHMENT_FILENAME_XLSX, reportName));

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
        exporter.exportReport();
        logger.info("Report exported to XLSX: {}", reportName);
    }
}
