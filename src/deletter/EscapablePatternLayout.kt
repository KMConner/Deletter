package deletter

import org.apache.log4j.helpers.PatternConverter
import org.apache.log4j.helpers.PatternParser
import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.*

class EscapablePatternLayout : Layout {
    private val BUF_SIZE: Int = 256
    private val MAX_CAPACITY: Int = 1024
    private var sbuf: StringBuffer? = null
    private var pattern: String? = null
    private var head: PatternConverter? = null

    constructor() : this("%m%n") {}

    constructor(pattern: String) {
        this.sbuf = StringBuffer(256)
        this.pattern = pattern
        this.head = this.createPatternParser(pattern).parse()
    }

    fun setConversionPattern(conversionPattern: String) {
        this.pattern = conversionPattern
        this.head = this.createPatternParser(conversionPattern).parse()
    }

    fun getConversionPattern(): String? = this.pattern

    override fun activateOptions() {}

    override fun ignoresThrowable(): Boolean = true

    private fun createPatternParser(pattern: String): PatternParser = PatternParser(pattern)

    override fun format(event: LoggingEvent): String {
        if (this.sbuf!!.capacity() > this.MAX_CAPACITY) {
            this.sbuf = StringBuffer(this.BUF_SIZE)
        } else {
            this.sbuf!!.setLength(0)
        }


        val e = LoggingEvent(event.fqnOfLoggerClass,
                event.logger,
                event.timeStamp,
                event.getLevel(),
                escape(event.renderedMessage),
                escape(event.threadName),
                event.throwableInformation,
                event.ndc,
                event.locationInformation,
                event.properties)

        var c = this.head
        while (c != null) {
            c!!.format(this.sbuf, e)
            c = c!!.next
        }

        return this.sbuf!!.toString()
    }

    private fun escape(str: String): String = str.replace("'", "''").replace("\r\n", "\n").replace("\n", "\r\n")
}