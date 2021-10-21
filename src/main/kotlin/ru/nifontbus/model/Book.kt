package ru.nifontbus.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId

data class Book(
    @BsonId
    var id: ObjectId?,

    var title: String,
    var author: String,
    var price: Float
) {
    constructor() : this(null, "Not init", "Not init", 0f)

    fun getIdAsHex(): String? {
        return this.id!!.toHexString()
    }
}

data class ShoppingCart(var id: String, var userId: String, val items: ArrayList<ShoppingItem>)
data class ShoppingItem(var bookId: String, var qty: Int) // quantity
data class User(var id: String, var name: String, var username:String, var password: String)
