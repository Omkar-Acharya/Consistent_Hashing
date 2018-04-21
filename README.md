# Consistent_Hashing

This program implements Consisten Hashing protocol to resolve names of the servers.

The servers are maintained in a ring based architecture. The servers know who their successors and predeccessors are. The communication between the servers take place in a circular fashion; each server communicating with it's successor. Two types of name servers are implemented.

1. BootStrap Server.
2. Name Server.

Below are the functionalities they provide.

1. # BootStrap Server: It is the first node of the architecture and remains permamently active. It is responsible for the entire range if there are no Name Servers. The initial keyvalue pairs, IP and port of the BootStrap Server are in the config file of BootStrap Server. Following commands can be executed on BootStrap Server:

  a. Lookup key:  Gives the name of the server identified by the key.
  b. Insert key:  A new key-value is inserted.
  c. Delete key:  An existing key-value is deleted.
 
2. # Name Server: These are multiple servers handling some range of key-value pairs. Name servers can anytime enter into and exit from the system. Following commands can be executed from the name server.

  a. Enter: The name server enters into the system. It first contacts bootstrap server, if bootstrap has the range of key-value that  name server should get then 

  b. Exit:  The name server exits the system. It informs it's successor and predessor and gives it's range of key-value pairs to the                   successor. The exit of the server is done in a way that existing ring architecture does not break.
  
  Folder Structure:
  
  --BootStrapServer
    |--BootStrapServer.java
    |--BootStrapServerThread.java
    |--BSProcess.java
    |--myconfig.txt
    
  --NameServer
    |--NameServer.java
    |--NameServerThread.java
    |--NSProcess.java
 
How to compile
Use: javac *.java

How to run:
Run Bootstrap Server first. Use: java BootStrapServer myconfig.txt
Run subsequent name servers using; java NameServer myconfig.txt

“This project was done in its entirety by Akshay Mendki and Omkar Acharya. We hereby state 
that we have not received unauthorized help of any form”
