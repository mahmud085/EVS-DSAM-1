Electronic Voting System for App Engine Standard (Java 8)
============================

  It is scalable, cloud-based solution, using Google’s PaaS App Engine, to manage election process. It has a backend interface, voting interface and a election result interface.
  
  The application is deployed in Google App Engine and is available at : https://majestic-bounty-226712.appspot.com/

 **Backend interface** 
 
 This interface is secured, so that only administrators can access it. It has the following features:
 
 * Admins can define the date and time of the start and end of the election.

 * Add the candidates that are nominated for the election

 * Import the email addresses of the students who are eligible to vote from a CSV file(.csv).

 * Trigger an email dispatch, sending a mail(uses the built-in Java Mail functionality of App Engine) to all students informing them about the election. The mail has the link to the voting interface and a unique token generated for every single student that enables her to cast her vote on the voting interface. 
    And the link is : https://majestic-bounty-226712.appspot.com/user/vote
    
 * An automated generic reminder email is sent the day before the voting starts in order to motivate the students to take part in the elections with a cron job which continuously checks the time up to the election.
 To deploy the cron seperately a gradle task can be executed: appengineDeployCron
  
  **Voting interface** 
  IMPORTANT: make sure the voter is added in the voting table before accessing the voting page.otherwise it redirects to the results page
  It mimics a basic ballot paper with all candidates and a possibility to mark one’s favorite. By entering a valid token, the vote will be counted and the token expires. Moreover, the system makes sure that only votes casted during the defined voting interval will be recognized.
  If it is past the voting period the voting interface url will redirect to the voting results page which is :https://majestic-bounty-226712.appspot.com/votingresults
  The voting interface is available at:https://majestic-bounty-226712.appspot.com/user/vote
  
  **Election results interface** 
  This will show the results right after the voting interval has ended. It should include a summary counting all eligible voters, casted votes, and the percentage of voter participation.
  The election result interface is available at :https://majestic-bounty-226712.appspot.com/votingresults




This sample demonstrates how to deploy an application on Google App Engine.

See the [Google App Engine standard environment documentation][ae-docs] for more
detailed instructions.

[ae-docs]: https://cloud.google.com/appengine/docs/java/


* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi) (at least 3.5)
* [Gradle](https://gradle.org/gradle-download/) (optional)
* [Google Cloud SDK](https://cloud.google.com/sdk/) (aka gcloud)

## Setup

• Download and initialize the [Cloud SDK](https://cloud.google.com/sdk/)

    gcloud init

* Create an App Engine app within the current Google Cloud Project

    gcloud app create

## Maven
### Running locally

    mvn appengine:run

To use vist: http://localhost:8080/

### Deploying

    mvn appengine:deploy

To use vist:  https://YOUR-PROJECT-ID.appspot.com

## Gradle
### Running locally

    gradle appengineRun

If you do not have gradle installed, you can run using `./gradlew appengineRun`.

To use vist: http://localhost:8080/

### Deploying

    gradle appengineDeploy

If you do not have gradle installed, you can deploy using `./gradlew appengineDeploy`.

To use vist:  https://YOUR-PROJECT-ID.appspot.com

## Testing

    mvn verify

 or

    gradle test

As you add / modify the source code (`src/main/java/...`) it's very useful to add [unit testing](https://cloud.google.com/appengine/docs/java/tools/localunittesting)
to (`src/main/test/...`).  The following resources are quite useful:

* [Junit4](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)
* [Truth](http://google.github.io/truth/)

