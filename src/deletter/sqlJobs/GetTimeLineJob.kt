package deletter.sqlJobs

import deletter.defaultWriter
import deletter.twitter
import org.apache.log4j.Logger
import twitter4j.Paging
import twitter4j.ResponseList
import twitter4j.Status
import java.sql.Connection

/**
 * This job gets home timeline.
 */
class GetTimeLineJob : SqlJob() {
    /**
     * Logger object.
     */
    override val logger: Logger = Logger.getLogger(this.javaClass)

    /**
     * Execute this job.
     */
    override fun execute(connection: Connection): Boolean {
        try {
            // Get statuses on the timeline and add information of each status.
            val timeline: ResponseList<Status> = twitter.timelines().getHomeTimeline(Paging(1, 200))
            timeline.forEach { status ->
                defaultWriter.addJob(AddStatusJob(status))
            }
            logger.debug("${timeline.size} statuses added to job.")
        } catch (ex: Exception) {
            logger.error("Error occurred when getting the time line.", ex)
            return false
        }

        return true
    }
}