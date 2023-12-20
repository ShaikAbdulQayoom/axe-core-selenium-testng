/* The code is a class that has two methods.
 The setup method is called before anything else happens, and the tearDown method is called after everything else happens.
 The setup method creates an instance of ChromeDriver and sets it up with the WebDriverManager.
 Then it assigns this instance to a variable named driver.
 The code attempts to be used in a class called Base, which will have a method called setup() that will create an instance of the ChromeDriver.
 The code above also has a tearDown() method, which should be called after the code has been executed.*/

package Ally;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.github.bonigarcia.wdm.WebDriverManager;


public class base {

    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    
    public void tearDown() {
    	copyFinalReports();
    	if (driver != null) {
            driver.quit();
        }
    
    ZipFolder zip = new ZipFolder();
    zip.createZipWithFolderInternalElements(System.getProperty("user.dir")+"\\backupFolder", System.getProperty("user.dir")+"//Ally_Report.zip");
    
    
    emailSender email = new emailSender();
    email.mailGeneration("test@gmail.com", "test@gmail.com", "test@gmail.com", "Ally_Report", "123456");
        
    }
    
    public void copyFinalReports() {
        File file = new File("");
        File SourceExtentHTMLReport = new File(System.getProperty("user.dir") + "//src//test//java//reports//");
        File dest = new File(System.getProperty("user.dir") + "//backupFolder//");

        try {
            FileUtils.deleteDirectory(dest);
            System.out.println("Files deleted successfully.");
            FileUtils.copyDirectory(SourceExtentHTMLReport, dest);
        } catch (IOException ioException) {
            
            ioException.printStackTrace();
        }
    }
}
