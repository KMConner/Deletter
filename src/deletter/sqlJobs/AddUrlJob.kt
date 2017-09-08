package deletter.sqlJobs

import org.apache.log4j.Logger
import twitter4j.URLEntity
import java.sql.Connection

/**
 * This job adds information of urls in a status.
 *
 * @param statusId The id of the status which contains the url entity.
 *
 * @param url The url entity to add.
 */
class AddUrlJob(private val statusId: Long, private val url: URLEntity) : SqlJob() {
    /**
     * Logger object.
     */
    override val logger: Logger = Logger.getLogger(this.javaClass)

    /**
     * Execute this job.
     */
    override fun execute(connection: Connection): Boolean {
        try {
            connection.prepareStatement("INSERT INTO url (status_id, display_url, expanded_url, url, start, end) VALUES (?, ?, ?, ?, ?, ?);").apply {
                setLong(1, statusId)
                setString(2, url.displayURL)
                setString(3, url.expandedURL)
                setString(4, url.url)
                setInt(5, url.start)
                setInt(6, url.end)
            }.executeUpdate()
        } catch (ex: Exception) {
            logger.error("Error occurred when adding a url", ex)
            return false
        }
        return true
    }
}