# CampsiteReservation

Welcome to my Campsite reservation Demo!
To access the API, please run the project under the configuration "DemoApplication" and go to http://localhost:8080/swagger-ui.html on a browser. Once there, you are able to access each call, and press "try it out" to enter the values.
Alternatively, any other way to send queries (ex: Postman) would work!

Additional notes on current implementation:

API:
- Date formats for the API are currently set to yyyy/MM/dd
- check-in/check-out times are set to 12:01pm/12:00pm
- There is no limit to how many reservations a single user can have, but each reservation can not be longer than 3 days
- As there is no simulation of time moving forward, "expired" reservations are not currently deleted (unless done manually). I have also not added a way to delete/modify users as it did not seem required for the scope of the challenge.

Technical:
- I have used a simple h2 file based DB for storage. Database manipulations were done via Hibernate
- for the unique booking id, I have used a uuid, which while in most cases should have no issues, leaves an unlikely yet possible case of duplicate booking ids. No protection was added for this. While using a simple incremental id would have been simpler, it did not feel "real" enough for the theme/design of the challenge. 
- To handle concurrency issues, I have used a simple ReentrantLock, due to the short time constraint not allowing for a more advanced queuing algorithm. It is able to handle a large incoming amount of concurrent api calls, but not very efficiently and is potentially prone to a deadlock/endless loop if anything goes wrong (for example, a failed connection to the DB).
- Documentation was done with an OpenApi swagger spec