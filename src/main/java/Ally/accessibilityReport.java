package Ally;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class accessibilityReport {
	
	private String reportFolderPath;
    private String backupFolderPath;

    public accessibilityReport(String reportFolderPath, String backupFolderPath) {
        this.reportFolderPath = reportFolderPath;
        this.backupFolderPath = backupFolderPath;
    }

    public void generateHtmlReport(String url, int totalViolations, String content) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File reportDirectory = new File(reportFolderPath);
        if (!reportDirectory.exists()) {
            reportDirectory.mkdirs();
        }

        File reportFile = new File(reportDirectory, "accessibility_report_" + url.hashCode() + "_" + timeStamp + ".html");
        FileWriter writer = new FileWriter(reportFile);
        writer.write("<html><body>");
        writer.write("<h2>Accessibility Report for URL: " + url + "</h2>");
        writer.write("<p>Total Violations: " + totalViolations + "</p>");
        writer.write(content);
        writer.write("</body></html>");
        writer.close();
        System.out.println("HTML report generated: " + reportFile.getAbsolutePath());
    }
    
    public void moveReportsToBackupFolder(File currentReportDirectory) {
        File backupFolder = new File(backupFolderPath);
        if (!backupFolder.exists()) {
            backupFolder.mkdirs();
        }

        File[] reportFiles = currentReportDirectory.listFiles();

        if (reportFiles != null) {
            for (File reportFile : reportFiles) {
                // ... (rest of the backup logic)
            }
        }
    }
}

