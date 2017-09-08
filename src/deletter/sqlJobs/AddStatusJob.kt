package deletter.sqlJobs

import deletter.defaultWriter
import org.apache.log4j.Logger
import twitter4j.Status
import java.sql.Connection
import java.sql.Statement

/**
 * This job adds information of specific status tot the database.
 *
 * @param status The status to add.
 */
class AddStatusJob(private val status: Status) : SqlJob() {
    /**
     * Logger object.
     */
    override val logger: Logger = Logger.getLogger(this.javaClass)

    /**
     * Execute this job.
     */
    override fun execute(connection: Connection): Boolean {
        // Check if the same status is already in the database.
        try {
            val statement: Statement = connection.createStatement()
            val result = statement.executeQuery("SELECT *\nFROM status\nWHERE id == ${status.id}")
            if (result.next()) {
                statement.close()
                logger.trace("Status with id ${status.id} is already in database.")
                return true
            }
            statement.close()

            // If the same status is not in the database, add status to the database.
            connection.prepareStatement(
                    "INSERT INTO status\n(id, in_reply_to_status_id, created_at, retweet_status_id, text, quote_status_id, hashtag_count, url_count, mention_count, media_count, user_id)\nVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").apply {
                setLong(1, status.id)
                setString(2, if (status.inReplyToStatusId == -1L) null else status.inReplyToStatusId.toString())
                setString(3, status.createdAt.toString())
                setString(4, status.retweetedStatus?.id?.toString())
                setString(5, if (status.isRetweet) null else status.text)
                setString(6, if (status.quotedStatusId == -1L) null else status.quotedStatusId.toString())
                setInt(7, if (status.isRetweet) -1 else status.hashtagEntities.size)
                setInt(8, if (status.isRetweet) -1 else status.urlEntities.size)
                setInt(9, if (status.isRetweet) -1 else status.userMentionEntities.size)
                setInt(10, if (status.isRetweet) -1 else status.extendedMediaEntities.size)
                setLong(11, status.user.id)
            }.execute()

            // If this status is retweet, add the retweeted status.
            if (status.retweetedStatus != null) {
                defaultWriter.addJob(AddStatusJob(status.retweetedStatus))
            }

            // If this status quotes other status, add the quoted status.
            if (status.quotedStatus != null) {
                defaultWriter.addJob(AddStatusJob(status.quotedStatus))
            }

            // If this status is a reply to other status, add the status.
            if (status.inReplyToStatusId != -1L) {
                defaultWriter.addJob(GetAndAddStatusJob(status.inReplyToStatusId))
            }

            // Add the user who states this status.
            defaultWriter.addJob(AddUserJob(status.user))

            // Unless this is a retweet, add entities information to the database.
            if (!status.isRetweet) {
                status.hashtagEntities.forEach { hashtag ->
                    defaultWriter.addJob(AddHashtagJob(status.id, hashtag))
                }

                status.urlEntities.forEach { url ->
                    defaultWriter.addJob(AddUrlJob(status.id, url))
                }

                status.extendedMediaEntities.forEach { media ->
                    defaultWriter.addJob(AddMediaJob(status, media))
                }
            }

            logger.debug("New status added id: ${status.id} (by ${status.user?.name})")
        } catch (ex: Exception) {
            logger.error("Error Occurred!", ex)
            return false
        }
        return true
    }

    override fun toString() = "AddStatusJob id: ${status.id} (by ${status.user?.name})"
}