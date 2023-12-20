package Ally;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

public class ZipFolder {
	
	ZipFolder() {
		this.fileList = new ArrayList();
		   }

		   private static String sourceFolderPath = "";

		   public static boolean copyFile(File original, String copyPathName) {
		File copied = new File(copyPathName);

		try {
		    FileUtils.copyFile(original, copied);
		    return true;
		} catch (IOException e) {
		    //Reporting.logReporter(Status.DEBUG, "ZipFile- Cannot do a copy");
		    e.printStackTrace();
		    return false;
			}

		}
		public static void createZipWithFolderInternalElements(String sourceFolder, String outputFolderNameZip) {
		sourceFolderPath = sourceFolder;
		ZipFolder appZip = new ZipFolder();
		appZip.generateFileList(new File(sourceFolder));
		appZip.zipIt(outputFolderNameZip);
		}

		private List<String> fileList;

		   /**
		    * Traverse a directory and get all files, and add the file into fileList
		    * @param node
		    * file or directory
		    */

		private void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
		    this.fileList.add(this.generateZipEntry(node.getAbsoluteFile().toString()));
		}
		if (node.isDirectory()) {
		    String[] subNote = node.list();
		    for (String filename : subNote) {
		   this.generateFileList(new File(node, filename));
		    			}
			}
		}

		   /**
		    * Format the file path for zip @param file file path @return Formatted file path */
		   private String generateZipEntry(String file) {
		   System.out.println("$$$$"+file.substring(sourceFolderPath.length() -1 , file.length()));
		return file.substring(sourceFolderPath.length() , file.length());
		   }

		   /**
		    * Zip it @param zipFile output ZIP file location */
		   private void zipIt(String zipFile) {
		byte[] buffer = new byte[1024];

		try {
		    FileOutputStream fos = new FileOutputStream(zipFile);
		    ZipOutputStream zos = new ZipOutputStream(fos);
		   // Reporting.logReporter(Status.DEBUG, " ZipFolder - Output to Zip : " + zipFile);

		    for (String file : this.fileList) {
		   System.out.println(" ZipFolder - File Added : " + file);
		       
		   ZipEntry ze = new ZipEntry(file);
		   zos.putNextEntry(ze);
		   System.out.println("***"+sourceFolderPath + File.separator + file);
		   FileInputStream in = new FileInputStream(sourceFolderPath + File.separator + file);

		   int len;
		   while ((len = in.read(buffer)) > 0) {
		       zos.write(buffer, 0, len);
		   }
		   in.close();
		    }
		    zos.closeEntry();
		    // remember close it
		    zos.close();
		   // Reporting.logReporter(Status.DEBUG, " ZipFolder - Zip created!");

		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		   }
}
