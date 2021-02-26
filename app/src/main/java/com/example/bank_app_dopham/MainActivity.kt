package com.example.bank_app_dopham

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private val fileName : String = "private_data"
    private val idFile: String = fileName + "_id"
    private val accountsFile : String = fileName + "_accounts"
    private val userFile : String = fileName + "_user"
    private var isOffline : Boolean = false
    private var isSync : Boolean = false
    private var id : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckState()
        ShowLoginPage()
    }

    private fun CheckState(){
        this.isOffline = !CheckConnection() // Check if the app is connected to internet
        Log.d("debugConsole", "isOffline " + this.isOffline.toString())

        this.isSync = CheckId() // Check if the app has the data in storage
        Log.d("debugConsole", "isSync " +  isSync.toString())
    }
    private fun CheckConnection() : Boolean{
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }
    private fun ShowLoginPage()
    {
        setContentView(R.layout.activity_main)
        val loginButton : Button = findViewById(R.id.main_login_button)
        val resetButton : Button = findViewById(R.id.main_reset_button)
        val idEditText : EditText = findViewById(R.id.main_id_EditText)

        resetButton.setOnClickListener{
            ResetData() // Delete all stored data for test purposes
        }
        loginButton.setOnClickListener {
            if (!this.isOffline)
            {
                OnlineRoutine(this.isSync, idEditText) //When the app is connected to internet
            }
            else
            {
                OfflineRoutine(this.isSync, idEditText) //When the app is not connected to internet
            }
        }
    }
    private fun ResetData()
    {
        deleteFile(idFile)
        deleteFile(accountsFile)
        deleteFile(userFile)
        this.isSync = false
    }
    private fun OfflineRoutine(isRegistered: Boolean, idEditText: EditText)
    {
        if (isRegistered) { // if sync
            if (idEditText.text.toString() == GetId()) // same Id
            {
                this.id = idEditText.text.toString()
                ShowUserPage(this.id)
            } else // Not the right one => Error
            {
                Toast.makeText(this, "ERROR : Not the right ID", Toast.LENGTH_LONG).show()
                // We could not show
                // the error to make the app safer against hackers
                // but for now it's not a problematic
            }
        } else //first time so we don't have data yet
        {
            Toast.makeText(this, "ERROR : Not sync", Toast.LENGTH_LONG).show()
        }
    }
    private fun OnlineRoutine(isRegistered : Boolean, idEditText : EditText) {
        if (CheckIdExist(idEditText.text.toString())) { //check if the id exist in the api data

            if (isRegistered) { // if sync
                if (idEditText.text.toString() == GetId()) // same Id
                {
                    this.id = idEditText.text.toString()
                    ShowUserPage(this.id)
                } else // Not the right one => Error
                {
                    Toast.makeText(this, "ERROR : Not the right ID", Toast.LENGTH_LONG).show()
                    // We could not show
                    // the error to make the app safer against hackers
                    // but for now it's not a problematic
                }
            } else //first time login so we save the id inside the device
            {
                this.id = idEditText.text.toString()
                WriteFile(idFile, this.id) //saving the id
                ShowUserPage(this.id)
            }
        } else {
            Toast.makeText(this, "ERROR : Not the right ID", Toast.LENGTH_LONG).show()
            // We could not show
            // the error to make the app safer against hackers
            // but for now it's not a problematic
        }
    }
    //Check if the id is already saved in the device
    // if not then it's the first time inside the app
    // if it's saved then the user has to enter the right id to log inside the app.
    private fun CheckId(): Boolean{
        val idInfo = ReadFile(idFile)
        return idInfo.count() != 0
    }
    //Check is the id exist inside the api data
    private fun CheckIdExist(id : String) : Boolean{
        try {
            val aConnection : APIConnection = APIConnection()
            val json :String = aConnection.getTLS(APIURL.URL_CONFIG+id)
            val user = aConnection.fromJSONToUser(json)

            if (user == null) // user did not exist
            {
                return false
            }
        }
        catch (e : Exception)
        {
            Log.d("debugConsole", e.toString())
        }
        return true
    }
    //Return the id saved inside the device
    private fun GetId() : String{
        return ReadFile(idFile)
    }
    private fun ShowUserPage(id : String)
    {
        try {
            var user : User? = null
            val aConnection: APIConnection = APIConnection()
            if (this.isOffline) //if the user is offline we load the saved data
            {
                val json = ReadFile(userFile)
                user = aConnection.fromJSONToUser(json)
            }
            else {
                val json: String = aConnection.getTLS(APIURL.URL_CONFIG + id)
                user = aConnection.fromJSONToUser(json)
                WriteFile(userFile, json)
            }
            setContentView(R.layout.user_page)

            val informationListView : ListView = findViewById(R.id.information_ListView)
            val connectionTextView : TextView = findViewById(R.id.user_page_connection_textView)

            var value : String = ""
            if(this.isOffline)
            {
                value = "Offline"
            }
            else
            {
                value = "Online"
            }
            connectionTextView.text = value //this.isOffline ? "Offline" : "Online"

            if (user != null) {
                val informationUser: Array<String> = arrayOf(
                        "ID : " + user.ID.toString(),
                        "FirstName : " + user.Name,
                        "LastName : " + user.Lastname)

                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, informationUser).also {adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    informationListView.adapter = adapter}
            }

            val buttonAccount : Button = findViewById(R.id.account_button)

            buttonAccount.setOnClickListener{
                ShowAccountsPage()
            }
        }
        catch (e : Exception)
        {
            Log.d("debugConsole", e.toString())
        }
    }
    private fun ShowAccountsPage()
    {
        try {
            setContentView(R.layout.account_page)
            val refreshButton : Button = findViewById(R.id.refresh_button)
            val backButton : Button = findViewById(R.id.back_button)

            if (this.isOffline) //hide the refresh button if we are not connected to internet
            {
                refreshButton.setVisibility(View.INVISIBLE)
            }
            else
            {
                refreshButton.setVisibility(View.VISIBLE)
            }
            refreshButton.setOnClickListener{
                LoadAccountsPage()
                Toast.makeText(this,"Refreshing",Toast.LENGTH_LONG).show()
            }
            backButton.setOnClickListener{
                ShowUserPage(this.id)
            }
            LoadAccountsPage()
        }
        catch (e: Exception)
        {
            Log.d("debugConsole", e.toString())
        }
    }
    private fun ShowAccountPage(account: Account)
    {
        try {
            setContentView(R.layout.selected_account_page)
            val accountListView : ListView = findViewById(R.id.account_ListView)
            val accountInfo : Array<String> = arrayOf(
                    "ID : " + account.ID.toString(),
                    "Name : " + account.AccountName,
                    "Amount : " + account.Amount.toString(),
                    "Iban : " + account.Iban,
                    "Currency : " + account.Currency)
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accountInfo).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                accountListView.adapter = adapter}

            val backButton : Button = findViewById(R.id.back_button)

            backButton.setOnClickListener{
                ShowAccountsPage()
            }
        }
        catch (e: Exception)
        {
            Log.d("debugConsole", e.toString())
        }
    }
    private fun LoadAccountsPage()
    {
        try {
            val aConnection : APIConnection = APIConnection()
            var accounts : List<Account> = arrayListOf()
            if (this.isOffline)
            {
                val json :String = ReadFile(accountsFile)
                accounts = aConnection.fromJSONToAccounts(json)
            }
            else
            {
                val json :String = aConnection.getTLS(APIURL.URL_ACCOUNTS)
                accounts = aConnection.fromJSONToAccounts(json)
                WriteFile(accountsFile, json)
            }

            val accountsListView : ListView = findViewById(R.id.accounts_recyclerView)
            var accountsAdapter : ListAdapter
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accounts).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                accountsListView.adapter = adapter
                accountsAdapter = adapter}

            accountsListView.setOnItemClickListener { parent, view, position, id ->
                val element = accountsAdapter.getItem(position)// The item that was clicked
                ShowAccountPage(element as Account)
                Log.d("debugConsole", element.toString())
            }
        }
        catch(e:Exception)
        {
            Log.d("debugConsole", e.toString())
        }
    }
    private fun WriteFile(file : String, content : String)
    {
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(content.toByteArray())
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("debugConsole", e.toString())
        }
    }
    private fun ReadFile(file: String) : String
    {
        var fileInputStream: FileInputStream? = null
        var stringBuilder: StringBuilder? = null
        try {
            fileInputStream = openFileInput(file)
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            stringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("debugConsole", e.toString())
            if (e is java.io.FileNotFoundException) {
                return "" //return empty => file did not exist yet
            }
        }
        return stringBuilder.toString()
    }
}