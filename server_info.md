serverThread

Message
>1.	 login
// >2.	 message
>3.	 message detail
>4.	 messageFrom
>5. messageTo(a list or a map)
>6. logout
>7. isGroup
login
>1. check userId,password from DB
>2. add info to onlineList of server and each client
	
message & message detail
>1. send from messageFrom to messageTo
>2. add to file
>3. add index(path) to DB

messageFrom
>1. userID(name)
// >2. userPort
// > 3. userThread

messageTo(list or map?)
>1. userID
// >2. userPort
// >3. userThread
							
logout
>1. delete user information from server(onlineList,thread)