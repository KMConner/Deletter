package deletter.sqlJobs

import org.apache.log4j.Logger
import java.sql.Connection

/**
 * This job set the deleted frag of the specific status.
 *
 * @param statusId The id of deleted status.
 */
class DeleteStatusJob(private val statusId: Long) : SqlJob() {
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
            val result = statement.executeUpdate("UPDATE status\nSET deleted = 1\nWHERE id = $statusId")
            logger.debug("$result items changed.")
        } catch (ex: Exception) {
            logger.error("Error occurred!", ex)
            return false
        } finally {
            statement.close()
        }
        return true
    }

    override fun toString(): String = "DeleteStatusJob id: $statusId"
}