package deletter.sqlJobs

import org.apache.log4j.Logger
import twitter4j.StatusDeletionNotice
import java.sql.Connection

/**
 * This job set the deleted frag of the specific status.
 *
 * @param statusId The id of deleted status.
 */
class DeleteStatusJob(private val statusDeletionNotice: StatusDeletionNotice,private val timeStamp: Long) : SqlJob() {
    /**
     * Logger object.
     */
    override val logger: Logger = org.apache.log4j.Logger.getLogger(this.javaClass)

    /**
     * Execute this job.
     */
    override fun execute(connection: Connection): Boolean {
        val statement = connection.createStatement()
        try {
            statement.executeUpdate("INSERT INTO delete_status\n(delete_timestamp, status_id, user_id) VALUES ($timeStamp, ${statusDeletionNotice.statusId}, ${statusDeletionNotice.userId})")
            val result = statement.executeUpdate("UPDATE status\nSET deleted = 1\nWHERE id = ${statusDeletionNotice.statusId}")
            logger.debug("$result items changed.")
        } catch (ex: Exception) {
            logger.error("Error occurred!", ex)
            return false
        } finally {
            statement.close()
        }
        return true
    }

    override fun toString(): String = "DeleteStatusJob id: ${statusDeletionNotice.statusId} user: ${statusDeletionNotice.userId}"
}