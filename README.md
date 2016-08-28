# sf-github-ci
Salesforce continuous integration using github version control system and heroku as deployment system.

##Getting Started
In order to use this you would need following things:  
  1. Github public repo which has Salesforce code in src folder along with package.xml. Make sure that code is deployable.  
  2. You would need one Heroku Account where this application would be hosted for Continuos integration.  
  3. GMAIL Account from which mails would be deployment notification would be triggered.  

###Please follow below steps to configure this application for continuos integration: 
-------------------------------------------------------------------------------------
  - **Get the ZIP URL of the github repo.**  
    1. Go to the your repository.  
      You should be able to see "Clone or Download" button. Click on it and then right click on the "Download ZIP" link and copy link address.  
    2. It should be as below for my SFChangeSet-Heroku application.  
      https://github.com/kiran-machhewar/SFChangeSet-Heroku/archive/master.zip  
      Keep this ZIP DOWNLOAD URL which we need to configure as envrionment variable for heroku.  
  - **Click on below link to deploy this application in your heroku account.**  
    <a href="https://www.heroku.com/deploy/?template=https://github.com/kiran-machhewar/sf-github-ci">
      <img src="https://www.herokucdn.com/deploy/button.svg" alt="Deploy">
    </a>
  - **Now once the application is installed configure below environment/config variables in your created heroku application.**  
    A. Logon to your heroku account where application is installed.  
    B. Navigate to your application and click on settings. There you should be able to "Reveal Config Vars" button click on it and configure below variables.  
      a. ORG_TYPE : Allowed values are Production or Sandbox  
      a. SF_USERNAME : Value would be the org where you want to deploy code.  
      b. SF_PASSWORD : Password of the org.  
      c. GMAIL_USERNAME : GMAIL username from where the notifications mails would be initiated. Please make sure not to give any .gmail suffix.  
      d. GMAIL_PASSWORD : Password for gmail account.  
      e. SF_ADMIN_EMAIL_ADDRESS : Email address of the admin of salesforce org whom deployment notification would be sent.  
      f. SOURCE_CODE_DOWNLOAD_PATH : URL which has been catpured in step 1.
  - **Configure github webhooks:**  
    a. Go to the settings of your github repository.  
    b. Click on Webhooks & Services.    
    c. Update the push address as <your-heroku-app-main-address>/GitHubWebhookListener  
      eg. https://sf-github-ci.herokuapp.com/GitHubWebhookListener  
    d. Update the setting to only listen for "Just the push event". 
    
```
Note : Because of GMAIL strict security you might not get email notification so in order to get emails you need to lessen the security below performing below two steps  
  1.Turning on the access for less secure apps from below url:  
    URL : https://www.google.com/settings/security/lesssecureapps  
  2.Gmail also prevents login from servers (as in heroku). So we need to disable captha also.  
  To do that open below url and click continue then update any file on your github so that new deployment gets initiated which will initiate a mail.  
  After which server would be added in trusted list of gmail account.  
    URL : https://accounts.google.com/b/0/DisplayUnlockCaptcha 

Since because of above process GMAIL account security would be compromised, so it would be good to create new GMAIL account for this notification purpose.
```
 
Once above configurations are done then you can test your CI by commiting to any file in your github repo. Heroku will get a push notification form GitHub and it will pull the zip file and deploy it to your configured salesforce account. Once deployment is done you will get notification mail on your configured SF_ADMIN_EMAIL_ADDRESS.
  
Enjoy continuous integration using github and heroku. If you face any issues you can reach out to me on smachhewar@gmail.com or can raise issue on my github repo: https://github.com/kiran-machhewar/sf-github-ci.



