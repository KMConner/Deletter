package deletter.sqlJobs

import deletter.util.downloadFile
import deletter.util.getExtensionFromUrl
import org.apache.log4j.Logger
import twitter4j.User
import java.io.File
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

class AddUserJob(private val user: User) : SqlJob() {
    override val logger: Logger = Logger.getLogger(this.javaClass)

    override fun execute(connection: Connection): Boolean {
        val statement: Statement = connection.createStatement()
        val result: ResultSet

        // Check if specified user is already added.
        try {
            result = statement.executeQuery("SELECT *\nFROM user\nWHERE id == ${user.id}")
            // TODO: Add image comparison.
            if (result.next()) {
                if (result.getString("name") == user.name
                        && result.getString("description") == user.description
                        && result.getInt("protected") == (if (user.isProtected) 1 else 0)
                        && result.getString("screen_name") == user.screenName
                        && result.getString("url") == user.url
                        && result.getString("location") == user.location
                        && result.getString("profile_image_url") == user.originalProfileImageURLHttps) {
                    logger.trace("User with id ${user.id} is already in database.")
                } else {
                    val newImagePath: String =
                            if (result.getString("profile_image_url") != user.originalProfileImageURLHttps)
                                downloadImage(user.id, user.originalProfileImageURLHttps)
                            else result.getString("profile_image_path")

                    // Save current data
                    connection.prepareStatement("INSERT INTO old_user\n(user_id, name, description, created_at, protected, screen_name, url, location, updated_at, profile_image_path)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").apply {
                        setLong(1, result.getLong("id"))
                        setString(2, result.getString("name"))
                        setString(3, result.getString("description"))
                        setString(4, result.getString("created_at"))
                        setInt(5, result.getInt("protected"))
                        setString(6, result.getString("screen_name"))
                        setString(7, result.getString("url"))
                        setString(8, result.getString("location"))
                        setString(9, result.getString("updated_at"))
                        setString(10, result.getString("profile_image_path"))
                    }.executeUpdate()

                    // update previous data
                    connection.prepareStatement("UPDATE user\nSET\n  name               = ?,\n  description        = ?,\n  protected          = ?,\n  screen_name        = ?,\n  url                = ?,\n  location           = ?,\n  profile_image_url  = ?,\n  profile_image_path = ?,\n  updated_at         = ?\nWHERE id = ${user.id}").apply {
                        setString(1, user.name)
                        setString(2, user.description)
                        setInt(3, if (user.isProtected) 1 else 0)
                        setString(4, user.screenName)
                        setString(5, user.url)
                        setString(6, user.location)
                        setString(7, user.originalProfileImageURLHttps)
                        setString(8, newImagePath)
                        setString(9, Date().toString())
                    }.executeUpdate()
                    logger.debug("User information with id ${user.id} (${user.name}) has been updated.")
                }
                return true
            }
        } catch (ex: Exception) {
            logger.error("Error occurred when executing sql query.", ex)
            return false
        } finally {
            statement.close()
        }

        // Add specified user to the database.
        try {
            val imagePath: String = downloadImage(user.id, user.originalProfileImageURLHttps)

            connection.prepareStatement("INSERT INTO user\n(id, name, description, protected, screen_name, url, location, created_at, updated_at, profile_image_url, profile_image_path)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").apply {
                setLong(1, user.id)
                setString(2, user.name)
                setString(3, user.description)
                setInt(4, if (user.isProtected) 1 else 0)
                setString(5, user.screenName)
                setString(6, user.url)
                setString(7, user.location)
                setString(8, user.createdAt.toString())
                setString(9, Date().toString())
                setString(10, user.originalProfileImageURLHttps)
                setString(11, imagePath)
            }.executeUpdate()

            logger.debug("New user added id: ${user.id} (${user.name})")
        } catch (ex: Exception) {
            logger.error("Error occurred when adding new user.", ex)
            return false
        }
        return true
    }

    override fun toString(): String = "AddUserJob id: ${user.id} (${user.screenName})"

    private fun downloadImage(userId: Long, imageUrl: String): String {
        val extension: String = getExtensionFromUrl(imageUrl)
        val basePath = "./ProfileImage/" + user.id.toString(16).padStart(16, '0') + "-"
        var suffix = 0
        while (File(basePath + suffix.toString(16).padStart(2, '0') + extension).exists()) {
            suffix++
        }
        val imagePath = basePath + suffix.toString(16).padStart(2, '0') + extension
        downloadFile(imageUrl, imagePath)
        return imagePath
    }
}