package Ally;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.deque.axe.AXE;
import io.github.bonigarcia.wdm.WebDriverManager;

public class adaTest extends base {
	
	private URL scriptUrl;
    private String reportFolderPath;
    private String backupFolderPath;
    private accessibilityReport accessibilityReport;
    
    private void handleException(String methodName, Exception e) {
        System.out.println("Exception in method " + methodName + ": " + e.getMessage());
        e.printStackTrace();
        // Log or handle the exception as needed
    }

    @BeforeSuite
    public void setUp() {
        try {
            loadConfigProperties();
            createReportDirectory();
            accessibilityReport = new accessibilityReport(reportFolderPath, backupFolderPath);
        } catch (Exception e) {
            handleException("setUp", e);
        }
    }

	@Test
    public void testAccessibilityForUrls() throws IOException {
		try {
		    
            JSONArray urlsArray = getTestUrlsFromJson();
            for (int i = 0; i < urlsArray.length(); i++) {
                String url = urlsArray.getString(i);
                testUrlAccessibility(url);
            }
        } catch (Exception e) {
            handleException("testAccessibilityForUrls", e);
        }
    }
    private JSONArray getTestUrlsFromJson() throws IOException {
    	try {
            try (InputStream inputStream = adaTest.class.getResourceAsStream("/url.json")) {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                String jsonString = new String(buffer, "UTF-8");
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.getJSONArray("urls");
            }
        } catch (IOException e) {
            handleException("getTestUrlsFromJson", e);
            return new JSONArray(); // Return an empty array in case of an exception
        }
    }
    
    private void loadConfigProperties() {
    	try {
            try (InputStream inputStream = adaTest.class.getResourceAsStream("/config.properties")) {
                Properties properties = new Properties();
                properties.load(inputStream);
                String scriptUrlProperty = properties.getProperty("scriptUrl");
                scriptUrl = new URL(scriptUrlProperty);
                reportFolderPath = properties.getProperty("reportFolderPath");
            }
        } catch (IOException e) {
            handleException("loadConfigProperties", e);
        }
    }
    
    private void testUrlAccessibility(String url) throws IOException {
    	try {
            driver.get(url);

            JSONObject responseJson = new AXE.Builder(driver, scriptUrl).analyze();
            JSONArray violations = responseJson.getJSONArray("violations");
            if (violations.length() > 0) {
                String violationsContent = generateViolationsContent(violations);
                int totalViolations = violations.length();
                accessibilityReport.generateHtmlReport(url, totalViolations, violationsContent);
            }
        } catch (IOException e) {
            handleException("testUrlAccessibility", e);
        }
    }
    
    private String generateViolationsContent(JSONArray violations) {
        StringBuilder content = new StringBuilder();
        content.append("<table border=\"1\" style=\"border-collapse: collapse; width: 100%;\">");
        content.append("<tr><th>Violation</th><th>Description</th><th>Impact</th></tr>");
        for (int i = 0; i < violations.length(); i++) {
            JSONObject violation = violations.getJSONObject(i);
            content.append("<tr>");
            content.append("<td>").append(i + 1).append("</td>");
            content.append("<td>").append(violation.getString("description")).append("</td>");
            content.append("<td>").append(violation.getString("impact")).append("</td>");
            content.append("</tr>");
        }
        content.append("</table>");
        return content.toString();
    }
    
    private void createReportDirectory() {
    	try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String currentReportFolderPath = "src/test/java/reports/" + timeStamp;
            File currentReportDirectory = new File(currentReportFolderPath);
            File dest = new File(System.getProperty("user.dir") + "//src//test//java//reports//");

            FileUtils.deleteDirectory(dest);

            System.out.println("Files deleted successfully.");
            if (!currentReportDirectory.exists()) {
                currentReportDirectory.mkdirs();
            }

            String backupFolderPath = "src/test/java/backup/";
            File backupFolder = new File(backupFolderPath);
            if (!backupFolder.exists()) {
                backupFolder.mkdirs();
            }

            File[] reportFiles = new File("src/test/java/reports").listFiles();
            if (reportFiles != null) {
                for (File reportFile : reportFiles) {
                    if (reportFile.isDirectory() || reportFile.equals(currentReportDirectory)) {
                        continue;
                    }

                    File backupFile = new File(backupFolderPath, reportFile.getName());

                    try {
                        Files.copy(reportFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        handleException("createReportDirectory", e);
                    }
                }
            }

            // Set the report folder path for the current execution
            reportFolderPath = currentReportFolderPath;
        } catch (IOException e) {
            handleException("createReportDirectory", e);
        }
    }
}