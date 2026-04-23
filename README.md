<h1>MyInventoryMobileApp</h1>
<h2>SNHU CS360</h4>
<h2>4.22.2026</h5>
<br>
<h3>Purpose of this app</h5>
This app was created for my final project during my second term of 2026 in order to create a basic mobile app allowing a user to login or register for a new account, 
give permission to send SMS low quantity messages to users, and to allow users to store an item and its quantity inside a table and edit, or delete any items they wish. 
<br>

<h3>App Design</h3>
The app was built to allow for quick and ease of use allowing users to store a phone number initially when registering which will only be utilized if the user also gives permission
to send SMS through the app. The app securely stores passwords so users can sign up knowing their passwords are securely stores utilizing hashing and salting to help prevent against 
attacks. Items are stored and accessed using the room library to query a SQLite database. 
<br>

<h3>App Testing</h3>
The app was properly tested to ensure users can sign in with unique usernames, phone numbers are correctly stored, and password are stored securely using hashing and salting. Permissions are saved per
device and will not display the request permissions screen if already given, and will display only if permissions have not been set. User can skip giving permission and go to inventory
in the permissions screen by simply pressing go to inventory button. Iventory screen was amply tested to ensure incorrect quantity types are handled accordingly, and items are correctly updated, and deleted. 
<br>

<h3>Design Challenges</h3>
When creating the app I found it to be fairly simple and something I was used to, I enjoyed using XML a lot more than I originally expected to since I have only really used HTML in the past. 
One challenge I ran into was when creating I weanted to keep passwords secure, and initially was going to use just hashing until when researching how to use hashing in Java I stumbled
upon salting which seemed to increase security a ton. Getting salting to work as intended was a little harder than I expected but once I understood the steps it became easier and now I don't
think I will ever not use it when creating other apps.
<br>

<h3>Key Takeaways</h3>
I am very happy to have discovered salting since it will keep users that much more secure and allow for an increase in security for all the apps I want to design. When creating apps it is important
to me that they flow very well no matter the user and with this app I feel it is simple enough to put this to use well. I find it extremely easy and fast to login, add, remove, or update items which
was the main goal I set to achieve when creating this app. The UI is very simple and doesn't clog the viewing port giving users room to breathe and operate in the app.
