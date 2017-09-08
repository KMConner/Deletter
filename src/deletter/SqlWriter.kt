package deletter

import deletter.sqlJobs.SqlJob
import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.LinkedBlockingDeque

val defaultWriter: SqlWriter = SqlWriter()

class SqlWriter {
    /**
     * Logger object.
     */
    private val logger = org.apache.log4j.Logger.getLogger(this.javaClass)

    /**
     * Blocking queue contains jobs.
     *
     * This queue is thread-safe.
     */
    private val jobs: LinkedBlockingDeque<SqlJob> = LinkedBlockingDeque()

    /**
     * Connection to sqlite file.
     */
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:./data.sqlite")

    companion object {
        /**
         * Max times of job trial.
         */
        const val RETRY_COUNT = 5
    }

    /**
     * Thread to execute jobs.
     */
    private val executeThread: Thread

    /**
     * If executeThread needs to stop.
     */
    @Volatile
    private var stop: Boolean = false

    init {
        executeThread = Thread(this::executeThreadImpl)
        executeThread.name = "Sql Writer Thread"
    }

    /**
     * Add specified job to the queue.
     *
     * @param sqlJob The SqlJob to add.
     */
    fun addJob(sqlJob: SqlJob) {
        jobs.add(sqlJob)
        logger.trace("New job added -- $sqlJob")
    }

    /**
     * Start executing thread.
     */
    fun start() {
        logger.info("Execution thread starting...")
        executeThread.start()
    }

    /**
     * Stop executing thread.
     *
     * Once this method is called, the executing thread in the same SqlWriter object cannot be restart.
     */
    fun stopJobs() {
        logger.info("Execution thread stopping.")
        stop = true
    }

    /**
     * Execute each job.
     *
     * When jobs has jobs, they will executed.
     * Otherwise, wait for 500 milli seconds.
     *
     * Each job is executed up to RETRY_COUNT times.
     */
    private fun executeThreadImpl() {
        logger.info("Execution thread started.")
        while (!stop) {
            if (jobs.isEmpty()) {
                Thread.sleep(500)
            } else {
                val job: SqlJob = jobs.pollFirst()
                logger.trace("Execution start -- $job")
                if (!job.execute(connection)) {
                    job.executeCount++
                    logger.warn("Failed to execute job $job")
                    if (job.executeCount < RETRY_COUNT) {
                        addJob(job)
                    }
                }
            }
        }
        connection.close()
        logger.info("Execution thread stopped.")
    }
}