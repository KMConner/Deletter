package deletter

import deletter.sqlJobs.AddStatusJob
import deletter.sqlJobs.AddUserJob
import deletter.sqlJobs.DeleteStatusJob
import twitter4j.*
import java.lang.Exception

class MyUserStreamListener : UserStreamAdapter() {

    private val logger = org.apache.log4j.Logger.getLogger(this.javaClass)

    // StreamListener のメソッド

    override fun onBlock(source: User?, blockedUser: User?) {}

    override fun onDeletionNotice(directMessageId: Long, userId: Long) {}

    override fun onDirectMessage(directMessage: DirectMessage?) {}

    override fun onFavorite(source: User?, target: User?, favoritedStatus: Status?) {
        if (source != null) {
            defaultWriter.addJob(AddUserJob(source))
        }
        if (target != null) {
            defaultWriter.addJob(AddUserJob(target))
        }
        if (favoritedStatus != null) {
            defaultWriter.addJob(AddStatusJob(favoritedStatus))
        }
    }

    override fun onFavoritedRetweet(source: User?, target: User?, favoritedRetweet: Status?) {
        if (source != null) {
            defaultWriter.addJob(AddUserJob(source))
        }
        if (target != null) {
            defaultWriter.addJob(AddUserJob(target))
        }
        if (favoritedRetweet != null) {
            defaultWriter.addJob(AddStatusJob(favoritedRetweet))
        }
    }

    override fun onFollow(source: User?, followedUser: User?) {
        if (source != null) {
            defaultWriter.addJob(AddUserJob(source))
        }
        if (followedUser != null) {
            defaultWriter.addJob(AddUserJob(followedUser))
        }
    }

    override fun onFriendList(friendIds: LongArray?) {}

    override fun onQuotedTweet(source: User?, target: User?, quotingTweet: Status?) {
        if (source != null) {
            defaultWriter.addJob(AddUserJob(source))
        }
        if (target != null) {
            defaultWriter.addJob(AddUserJob(target))
        }
        if (quotingTweet != null) {
            defaultWriter.addJob(AddStatusJob(quotingTweet))
        }
    }

    override fun onRetweetedRetweet(source: User?, target: User?, retweetedStatus: Status?) {
        if (source != null) {
            defaultWriter.addJob(AddUserJob(source))
        }
        if (target != null) {
            defaultWriter.addJob(AddUserJob(target))
        }
        if (retweetedStatus != null) {
            defaultWriter.addJob(AddStatusJob(retweetedStatus))
        }
    }

    override fun onUnblock(source: User?, unblockedUser: User?) {}

    override fun onUnfavorite(source: User?, target: User?, unfavoriteStatus: Status?) {
        if (source != null) {
            defaultWriter.addJob(AddUserJob(source))
        }
        if (target != null) {
            defaultWriter.addJob(AddUserJob(target))
        }
        if (unfavoriteStatus != null) {
            defaultWriter.addJob(AddStatusJob(unfavoriteStatus))
        }
    }

    override fun onUnfollow(source: User?, unfollowedUser: User?) {}

    override fun onUserDeletion(deletedUser: Long) {}

    override fun onUserListCreation(listOwner: User?, list: UserList?) {}

    override fun onUserListDeletion(listOwner: User?, list: UserList?) {}

    override fun onUserListMemberAddition(addedMember: User?, listOwner: User?, list: UserList?) {}

    override fun onUserListMemberDeletion(deletedMember: User?, listOwner: User?, list: UserList?) {}

    override fun onUserListSubscription(subscriber: User?, listOwner: User?, list: UserList?) {}

    override fun onUserListUnsubscription(subscriber: User?, listOwner: User?, list: UserList?) {}

    override fun onUserListUpdate(listOwner: User?, list: UserList?) {}

    override fun onUserProfileUpdate(updatedUser: User?) {}

    override fun onUserSuspension(suspendedUser: Long) {}

    // StatusListener のメソッド

    override fun onDeletionNotice(statusDeletionNotice: StatusDeletionNotice?) {
        if (statusDeletionNotice == null) {
            logger.warn("statusDeletionNotice is null.")
            return
        }
        defaultWriter.addJob(DeleteStatusJob(statusDeletionNotice.statusId))
    }

    override fun onScrubGeo(userId: Long, upToStatusId: Long) {}

    override fun onStallWarning(warning: StallWarning?) {}

    override fun onStatus(status: Status?) {
        if (status == null) {
            logger.warn("Status is null")
            return
        }
        defaultWriter.addJob(AddStatusJob(status))
    }

    override fun onTrackLimitationNotice(numberOfLimitedStatus: Int) {}

    // StreamListener のメソッド

    override fun onException(ex: Exception?) {
        if (ex != null) {
            logger.error("An Exception thrown", ex)
        }
    }

}