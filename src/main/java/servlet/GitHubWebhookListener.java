package servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sforce.soap.metadata.*;
import com.sforce.ws.ConnectionException;

import util.DeploymentUtil;
import util.MetadataLoginUtil;
import util.FileUtil;
import util.MailUtil;




@WebServlet(
        name = "GitHubWebhookListener", 
        urlPatterns = {"/GitHubWebhookListener"}
    )
public class GitHubWebhookListener extends HttpServlet {

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		handleRequest(req,resp);
    }
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
		handleRequest(req,resp);
    }
    
	private void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		try {	
			
			// Create a stream to hold the output
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			// IMPORTANT: Save the old System.out!
			PrintStream old = System.out;
			// Tell Java to use your special stream
			System.setOut(ps);
			
			ServletOutputStream out = resp.getOutputStream();
	        out.write(("GitHubWebhookListener POST is ready\n ").getBytes());
	        DeployOptions deployOptions = new DeployOptions();
	        deployOptions.setPerformRetrieve(false);
	        deployOptions.setRollbackOnError(true);
	        deployOptions.setCheckOnly(false);          
	        deployOptions.setTestLevel(TestLevel.NoTestRun);
	        MetadataConnection targetConnection;
				targetConnection = MetadataLoginUtil.getMetadataConnection(System.getenv("SF_USERNAME"), System.getenv("SF_PASSWORD"), System.getenv("ORG_TYPE"));
	        DeploymentUtil deploymentUtil = new DeploymentUtil();
	        byte []zipData = FileUtil.downloadFile(System.getenv("SOURCE_CODE_DOWNLOAD_PATH"));
	        System.out.println("Code is downloaded.");
	        zipData = FileUtil.processZipToKeepSrcFolderOnly(zipData);
	        System.out.println("Non required files are removed.");
	        deploymentUtil.deployFromZipByteArrayData(zipData, deployOptions, targetConnection);
	        String output = baos.toString();
	        out.write(output.getBytes());
	        System.setOut(old);
	        MailUtil mailUtil = new MailUtil(System.getenv("GMAIL_USERNAME"), System.getenv("GMAIL_PASSWORD"));
			String[] to = { System.getenv("SF_ADMIN_EMAIL_ADDRESS")}; // list of recipient email addresses
			mailUtil.sendFromGMail(to,"SfGitHubCI Deployment Status",output);
	        out.flush();
	        out.close();	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
	}
	
}
