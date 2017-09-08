package deletter.sqlJobs

import deletter.defaultWriter
import deletter.twitter
import org.apache.log4j.Logger
import twitter4j.Status
import twitter4j.TwitterException
import java.sql.Connection
import java.sql.Statement

class GetAndAddStatusJob(private val statusId: Long) : SqlJob() {
    override val logger: Logger = Logger.getLogger(this.javaClass)


    override fun execute(connection: Connection): Boolean {
        val statement: Statement = connection.createStatement()
        val result = statement.executeQuery("SELECT *\nFROM status\nWHERE id == $statusId")
        if (result.next()) {
            statement.close()
            logger.trace("Status with id $statusId is already in database.")
            return true
        }
        statement.close()
        try {
            val status: Status = twitter.tweets().showStatus(statusId)
            defaultWriter.addJob(AddStatusJob(status))
        } catch (ex: TwitterException) {
            if (ex.errorCode == 179) {
                logger.warn("Not authorized to see the status id: $statusId", ex)
                return true
            } else {
                logger.error("Error occurred when getting status with id $statusId", ex)
                return false
            }
        } catch (ex: Exception) {
            logger.error("Error occurred when getting status with id $statusId", ex)
            return false
        }
        return true
    }

    override fun toString(): String = "GetAndAddStatusJob id: $statusId"
}