package servlet;

import java.io.File;
import java.io.IOException;

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
	        FileUtil.createFileFromByteArray(zipData,new File("Download.zip"));
	        zipData = FileUtil.processZipToKeepSrcFolderOnly(zipData);
	        FileUtil.createFileFromByteArray(zipData,new File("Clean.zip"));
	        deploymentUtil.deployFromZipByteArrayData(zipData, deployOptions, targetConnection);
	        out.write(("Deployment is done. Please check the target org for status.").getBytes());
	        out.flush();
	        out.close();	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
	}
	
}
