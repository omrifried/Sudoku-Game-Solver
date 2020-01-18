# Project: Sudoku Solver
##### Author: Omri Fried
##### Last Edit Date: 01/17/2020
##### Project Description: 
The purpose of this assignment was to create a client-server 
 socket connection via TCP. In this assignment, we created a 
 server and a client that are to connect on the local host. 
 The server opens for the connecton process and awaits connection 
 from the client. Once connected via a specified port, the 
 client and server exchange greetings with their NAMES. The 
 client shuts down after the message, but the server remains 
 open and can then recieve additional connections from other 
 clients.
D) server: This program creates the server that will turn on
           and await connection from the client. The program
           acts as the server and, once it connects to the client
           and receives a greetin message, will respond with its
           own greeting message. The client connection will then
           be shut down, but the server will remain open and can
           receive future client connections/messages.
   client: This program creates the client that will connect to
           the server and send an initial greeting message. Once
           the server is up and running, the client will send a 
           connection request and connect to the server. After
           connection, the client will send a greeting message
           to the server and wait for a response. Once it receives
           the response, the client will shut down.
E) The messages are HTTP messages.
G) N/A
H) I followed the code provided by the Beej Guide to Network 
   Programming. The base code itself follows the style provided
   in the Beej guide, but I adjusted it according to the project.
   I used chapters 5, 7, and 9 in the Beej guide to develop the code.
          
