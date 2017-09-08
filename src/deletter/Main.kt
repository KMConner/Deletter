package deletter

import deletter.sqlJobs.GetTimeLineJob
import twitter4j.TwitterStream
import org.apache.log4j.Logger
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.TwitterStreamFactory


val twitter: Twitter = TwitterFactory.getSingleton()

fun main(args: Array<String>) {
    val logger = Logger.getLogger("main")
    logger.info("Start")
    val mm = Main()
    mm.connect()
}

class Main {
    private val logger = Logger.getLogger(this.javaClass)

    fun connect() {
        val twitter: TwitterStream = TwitterStreamFactory.getSingleton()
        JavaMethods.addListener(twitter)
        val th = Thread { twitter.user() }
        th.start()
        defaultWriter.start()
        defaultWriter.addJob(GetTimeLineJob())
        readLine()
        logger.info("Stopping...")
        twitter.shutdown()
        defaultWriter.stopJobs()
        logger.info("Stopped")
    }
}
