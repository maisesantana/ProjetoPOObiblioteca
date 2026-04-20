package br.com.atlas.model;

import java.time.LocalDate;

public class Report {

    private int reportId;
    private String reportType;
    private LocalDate generationDate;
    private Librarian librarian;

    public Report() {
    }

    public Report(int reportId, String reportType, LocalDate generationDate, Librarian librarian) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generationDate = generationDate;
        this.librarian = librarian;
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

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }
}