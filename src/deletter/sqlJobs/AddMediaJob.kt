package deletter.sqlJobs

import deletter.util.downloadFile
import deletter.util.getHashFromSnowFlake
import org.apache.log4j.Logger
import twitter4j.ExtendedMediaEntity
import twitter4j.Status
import java.io.File

import java.sql.Connection

/**
 * This job download and add information to the database.
 *
 * @param status The status which contains the media.
 *
 * @param mediaEntity The media entity to download and add information to the database.
 */
class AddMediaJob(private val status: Status, private val mediaEntity: ExtendedMediaEntity) : SqlJob() {

    // TODO: Use getExtensionFromUrl function in utilities.
    companion object {
        private val regex = Regex("^https?://.*(\\.[a-zA-Z0-9]+)$", RegexOption.IGNORE_CASE)
    }

    /**
     * Logger object.
     */
    override val logger: Logger = Logger.getLogger(this.javaClass)

    /**
     * Execute this job.
     */
    override fun execute(connection: Connection): Boolean {
        // TODO: support other media types.
        if (mediaEntity.type != "photo") {
            return true
        }

        val match = regex.matchEntire(mediaEntity.mediaURLHttps)

        if (match == null) {
            logger.warn("Image url is invalid -- ${mediaEntity.mediaURLHttps}")
            return false
        }

        val extension: String? = match.groups[1]?.value
        if (extension == null) {
            logger.warn("Image url is invalid -- ${mediaEntity.mediaURLHttps}")
            return false
        }

        // Generate file name
        var suffix = 0
        val baseStr: String = "./Images/" + getHashFromSnowFlake(status.user.id) + "-" + getHashFromSnowFlake(status.id) + "-"
        while (File(baseStr + suffix.toString(16) + extension).exists()) {
            suffix++
        }
        val fileName: String = baseStr + suffix.toString(16) + extension

        // Download media file
        try {
            downloadFile(mediaEntity.mediaURLHttps, fileName)
        } catch (ex: Exception) {
            logger.error("Error occurred when downloading media", ex)
            return false
        }

        // Add media information to the database.
        try {
            connection.prepareStatement("INSERT INTO media\n(status_id, media_url, url, display_url, expanded_url, type, local_path, start, end) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)").apply {
                setLong(1, status.id)
                setString(2, mediaEntity.mediaURLHttps)
                setString(3, mediaEntity.url)
                setString(4, mediaEntity.displayURL)
                setString(5, mediaEntity.expandedURL)
                setString(6, mediaEntity.type)
                setString(7, fileName)
                setInt(8, mediaEntity.start)
                setInt(9, mediaEntity.end)
            }.executeUpdate()
        } catch (ex: Exception) {
            logger.error("Error occurred when adding media information.", ex)
            return false
        }

        logger.debug("New Image Saved path:$fileName, url: ${mediaEntity.mediaURLHttps}")

        return true
    }
}