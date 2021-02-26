# M1_Secure_Development Project
<img src="img/main_page.jpg" width="200">

> Main page of our app

## DO PHAM Alexandre IOS1

## Table of Contents
1. [Checking the user](#UserID) 
2. [User's data](#Userdata)
3. [Hiding the API URL](#HidingURL)
4. [Screenshots](#Screenshots)

## Explain how you ensure user is the right one starting the app ? 

<img src="img/write_function.png">

> Screenshot of the write function which saves the data inside the app storage

We know that the right user is starting the app because during the first launch the ID written in the editText field is saved in the app storage.

Each time a user is starting the app we check the id written is the editText field with the saved value stored in the app storage (if the app is already synchronized).
So another user can't access to the app.

<img src="img/main_page_wrong_id.jpg" width="200">

> The user used the wrong id.

During the first run of the app, (the app is not synchronized yet.) the device needs to be connected to internet,
if so, we check if the id inside the filed exists in the API data.
If it exists, we save the id inside the app internal storage, if not we throw an error.

> To reset the saved data, we implemented a reset button but it's only for test purposes not for the `released version`.

## How do you securely save user's data on your phone ?

We save the user's data in the app storage using `Read and Write Internal Storage`. 
The file stored in the internal storage is private in default, and only our app accesses it.

They cannot be accessed from outside the application.

When the user uninstall the app from the device, the internal storage file will also be removed.

<img src="img/file_location.png">

> paths of the data inside the internal storage of the app.

## How did you hide the API url ?

First We use proguard to get a Bytecode Harder to read (also proguard will remove the debug log during the release). 

To strengthen it we also used a gradle plugin called Enigma.

With the help of Enigma all the string in our code will be encrypted. 
Unfortunately Enigma is only for java classes for now. 

So to solve this problem we create a java class which contains the API URL. 

<img src="img/APIURL_function.png">

Kotlin code is fully compatible with Java code so we can get the API URL from our koltin classes without any problems.

<img src="img/Enigma_APIURL.png">

> Now we cannot reverse engineering to find the API URL because it's encrypted.

## Screenshots of your application

<img src="img/main_page.jpg" width="200">

> Main page of the app.

---

<img src="img/main_page_wrong_id.jpg" width="200">

> The user wrote a wrong id

---

<img src="img/user_page_online.jpg" width="200">

> User page when connected to the internet (ONLINE MOD).

---

<img src="img/user_page_offline.jpg" width="200">

> User page when disconnected to the internet (OFFLINE MOD).

---

<img src="img/accounts_page_online.jpg" width="200">

> Accounts page in which we can see all the accounts. (ONLINE MOD)

---

<img src="img/accounts_page_offline_sync.jpg" width="200">

> Accounts page synchronized (OFFLINE MOD).
> The user can't refresh the accounts in OFFLINE MOD.

---

<img src="img/accounts_page_offline_not_sync.jpg" width="200">

> Accounts page not synchronized (OFFLINE MOD). 
> To be sychronized the user must have already consulted this page at least once while being connected to the internet.

---

<img src="img/accounts_page_online_refreshing.jpg" width="200">

> Refreshing the page

---

<img src="img/account_details_page.jpg" width="200">

> When we click on an account in the Accoutns page we can see its details inside a new page.
