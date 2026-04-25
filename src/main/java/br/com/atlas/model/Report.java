package br.com.atlas.model;

import java.time.LocalDate;

public class Report {

    private int reportId;
    private String reportType;
    private LocalDate generationDate;

    public Report(String reportType) {
        this.reportType = reportType;
        this.generationDate = LocalDate.now();
    }

    public Report(int reportId, String reportType, LocalDate generationDate) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generationDate = generationDate;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }
}