# M1_Secure_Development Project
## DO PHAM Alexandre IOS1

### Explain how you ensure user is the right one starting the app ? 

<img src="img/write_function.png">

> Screenshot of the write function which saves the data inside the app storage

We know that the right user is starting the app because during the first launch the ID written in the editText field is saved in the app storage.

Each time a user is starting the app we check the id written is the editText field with the saved value stored in the app storage (if the app is already synchronized).
So another user can't access to the app.

During the first run of the app, (the app is not synchronized yet.) the device needs to be connected to internet,
if so, we check if the id inside the filed exists in the API data.
If it exists, we save the id inside the app internal storage, if not we throw an error.

### How do you securely save user's data on your phone ?

We save the user's data in the app storage using `Read and Write Internal Storage`. The file stored in the internal storage is private in default, and only our app accesses it.
They cannot be accessed from outside the application.
When the user uninstall the app from the device, the internal storage file will also be removed.

### How did you hide the API url ?

First We use proguard to get a Bytecode Harder to read (also proguard will remove the debug log during the release). 
To strengthen it we also used a gradle plugin called Enigma.
With the help of Enigma all the string in our code will be encrypted. 
Unfortunately Enigma is only for java classes for now. 
So to solve this problem we create a java class which contains the API URL. 
Kotlin code is fully compatible with Java code so we can get the API URL from our koltin classes without any problems.

### Screenshots of your application
