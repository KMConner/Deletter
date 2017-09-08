package deletter.sqlJobs

import org.apache.log4j.Logger
import java.sql.Connection

/**
 * This class is the super class of each sql jobs.
 */
abstract class SqlJob {
    /**
     * Gets or sets how many times this job has been tried to execute.
     */
    var executeCount = 0

    /**
     * Logger object.
     */
    protected abstract val logger: Logger

    /**
     * Execute this job.
     */
    abstract fun execute(connection: Connection): Boolean

}