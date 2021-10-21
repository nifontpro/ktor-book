package ru.nifontbus.model

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.bson.codecs.configuration.CodecRegistries.fromProviders
import org.bson.codecs.configuration.CodecRegistries.fromRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.bson.types.ObjectId

class DataManagerMongoDB {
    private val database: MongoDatabase
    private val bookCollection: MongoCollection<Book>

    init {
        val pojoCodecRegistry: CodecRegistry = fromProviders(
            PojoCodecProvider.builder().automatic(true).build()
        )

        val codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry)
        val settings: MongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(ConnectionString("mongodb://localhost")) // add the connection string if not using localhost
            .codecRegistry(codecRegistry)
            .build()

        val mongoClient = MongoClients.create(settings)
        database = mongoClient.getDatabase("bookstore")
        bookCollection = database.getCollection(Book::class.java.name, Book::class.java)

        initBooks()
    }

    fun allBooks(): List<Book> {
        val mongoResult = bookCollection.find()
        mongoResult.forEach {
            println("book: $it")
        }
        return mongoResult.toList()
    }

    fun newBook(book: Book): Book {
        bookCollection.insertOne(book)
        book.getIdAsHex()
        return book
    }

    fun updateBook(hexId: String, book: Book): Book? {
        val bookFound = bookCollection.find(Document("_id", ObjectId(hexId))).first()
        println("------------> id =${bookFound?.getIdAsHex()}")
        bookFound?.let {
            book.id = bookFound.id
            bookCollection.replaceOne(
                eq("_id", bookFound.id),
                book
            )
        }
        return bookFound
    }

/*    private fun deleteAllBooks() {
        bookCollection.deleteMany(Document())
    }*/

    fun deleteBook(hexId: String): Book? {
        val bookFound = bookCollection.find(Document("_id", ObjectId(hexId))).first()
        bookCollection.deleteOne(eq("_id", ObjectId(hexId)))
        return bookFound
    }

    fun sortedBooks(sortBy: String, pageNumber: Int, asc: Boolean = true): List<Book> {
        val pageSize = 3
        val ascInt: Int = if (asc) 1 else -1
        return bookCollection
            .find()
            .sort(Document(mapOf(Pair(sortBy, ascInt), Pair("_id", -1))))
            .skip(pageNumber - 1)
            .limit(pageSize)
            .toList()
    }


    private fun initBooks() {

/*        deleteAllBooks()
        for (i in 1..100) {
            val price = (0..1000).random().toFloat()
            bookCollection.insertOne(Book(null, "Title $i. A", "Author $i A", price))
            bookCollection.insertOne(Book(null, "Title $i. B", "Author $i B", price))
            bookCollection.insertOne(Book(null, "Title $i. C", "Author $i C", price))
        }*/
    }
}