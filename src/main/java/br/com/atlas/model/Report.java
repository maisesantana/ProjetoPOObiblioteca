package br.com.atlas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Report {

    private int reportId;
    private String reportType;
    private LocalDate generationDate;
    private int totalLoans; // campo do relatório mensal
    private List<String> mostBorrowed; // campo do relatório mais emprestados

    public Report(String reportType) {
        this.reportType = reportType;
        this.generationDate = LocalDate.now();
        this.mostBorrowed = new ArrayList<>(); // inicializa a lista
    }

    public Report(int reportId, String reportType, LocalDate generationDate) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generationDate = generationDate;
        this.mostBorrowed = new ArrayList<>();
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

    public int getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(int totalLoans) {
        this.totalLoans = totalLoans;
    }

    public List<String> getMostBorrowed() {
        return mostBorrowed;
    }

    public void setMostBorrowed(List<String> mostBorrowed) {
        this.mostBorrowed = mostBorrowed;
    }
}