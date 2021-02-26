package com.example.bank_app_dopham

class User {
    private val id : Int
    private val name : String
    private val lastname : String

    public val ID: Int
        get() = this.id

    public val Name: String
        get() = this.name;

    public val Lastname: String
        get() = this.lastname;

    constructor(id : Int, firstName : String, lastName : String)
    {
        this.id = id
        this.name = firstName
        this.lastname = lastName
    }

    public override fun toString(): String {
        return "${this.id.toString()} ${this.name} ${this.lastname}"
    }
}