package deletter.sqlJobs

import org.apache.log4j.Logger
import twitter4j.HashtagEntity
import java.sql.Connection

/**
 * This class is sql job to add hashtag information to the database.
 */
class AddHashtagJob(private val statusId: Long, private val hashtag: HashtagEntity) : SqlJob() {
    /**
     * Logger object.
     */
    override val logger: Logger = Logger.getLogger(this.javaClass)

    /**
     * Execute this job.
     */
    override fun execute(connection: Connection): Boolean {
        try {
            connection.prepareStatement("INSERT INTO hashtag\n(status_id, text, start, end) VALUES (?, ?, ?, ?)").apply {
                setLong(1, statusId)
                setString(2, hashtag.text)
                setInt(3, hashtag.start)
                setInt(4, hashtag.end)
            }.executeUpdate()
        } catch (ex: Exception) {
            logger.error("Error occurred when adding new hashtag information.", ex)
            return false
        }
        return true
    }
}