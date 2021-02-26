package com.example.bank_app_dopham

class Account {
    private val id : Int
    private val accountName : String
    private val amount : Int
    private val iban : String
    private val currency : String

    public val ID: Int
        get() = this.id

    public val AccountName: String
        get() = this.accountName

    public val Amount: Int
        get() = this.amount

    public val Iban: String
        get() = this.iban

    public val Currency: String
        get() = this.currency

    constructor(id : Int, accountName : String, amount : Int, iban : String, currency : String)
    {
        this.id = id
        this.accountName = accountName
        this.amount = amount
        this.iban = iban
        this.currency = currency
    }

    public override fun toString(): String {
        return "${this.id.toString()} ${this.accountName}"
    }
}