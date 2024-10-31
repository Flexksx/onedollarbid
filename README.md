# onedollarbid 💲
Auction Web App

An auction web app built as a lab assignment for the Web Development course for the University.

# Assignment Requirements

1. Select a database to use for the project.
2. Design the data model for your database based on the data you scrape from lab-1. You can use either an ORM or raw SQL queries to interact with the database.
3. Implement CRUD (Create, Read, Update, Delete) operations with an HTTP interface. Use libraries or frameworks as needed. For DELETE, PUT, and GET operations, utilize query parameters (such as ID or name) to identify the resources.
4. Add pagination for your resources (e.g., use ‘offset=1‘ and ‘limit=5‘ to control the result set).
5. Implement a route/handler to accept multipart/form-data file uploads. Test this by sending a JSON file. ( through either Postman or a script)
6. Implement a Chat Room logic using the WebSocket protocol.[1]
7. Use Docker Compose to run your database inside a Docker container. Similar to the previous .yaml file, you have to find the appropriate database image and learn how to connect to the database server over the network.
8. Write a ‘Dockerfile‘ for your application. Run it with the docker command.
9. Implement a separate TCP server to handle client connections and messages. See details here: [2]
10. Implement a coordination mechanism to manage the order of read and write operations on the shared file. Ensure that all write operations complete before any read operations. Use synchronization mechanism or other approaches to coordinate the execution order between threads.

# My Interpretaion 
This lab requirements made it hard to put it together in one single project.
But one option would be an auction app. 
You have listings of different items, and when an auction starts, there can be multiple people connecting to it and placing their bets. 
This is different from the Chat Room idea, but follows the same principles - have a websocket connection to enable a auction-scoped communication, and it also implements the 10th point, which will handle multiple consequent reads and writes. 