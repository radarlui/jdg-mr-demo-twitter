JBoss JDG_MR Demo Guide
============================================================
Demo based on JBoss DataGrid and EAP products that show how to create a distributed big data processing application using JBoss DataGrid's MapReduce implementation.

Setup and Configuration
-----------------------
See docs directory for details on this project.

For those that can't wait, see README in 'installs' directory, add products, 
run 'init.sh' and follow the instructions given.

0) Download and cCopy jboss-eap-6.4.0.zip and jboss-datagrid-6.4.1-eap-modules-library.zip to the installs dir.

2) Run "init.sh --nodeCount=n" with the amount of nodes you want to create. (you should start with 1)

3) Access the URL: http://localhost:8080/JDG_MR_WEB/ and play with the demo.

4) If you want to add new nodes later, you can run "addNode.sh --nodeNumber=n" where "n" is the node number.

