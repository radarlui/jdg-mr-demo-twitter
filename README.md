JBoss JDG_MR Demo Guide
============================================================
Demo based on JBoss DataGrid and EAP products that shows how to create a distributed big data processing application using JBoss DataGrid's MapReduce implementation.

Setup and Configuration
-----------------------

0) Go to https://dev.twitter.com/ and register your application to acquire consumerKey/consumerSecret/accessToken/accessTokenSecret

1) Fill projects/JDG_MR_WEB/src/main/resources/twitter4j.properties with those values

2) Download and copy jboss-eap-6.4.0.zip and jboss-datagrid-6.4.1-eap-modules-library.zip to the installs dir.

3) Run "init.sh --nodeCount=n" with the amount of nodes you want to create. (you should start with 1)

4) Access the URL: http://localhost:8080/JDG_MR_WEB/ and play with the demo.

5) If you want to add new nodes later, you can run "addNode.sh --nodeNumber=n" where "n" is the node number.

