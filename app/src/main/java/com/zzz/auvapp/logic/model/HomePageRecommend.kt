package com.zzz.auvapp.logic.model

data class HomePageRecommend(
    val adExist: Boolean,
    val count: Int,
    val itemList: List<Item>,
    val nextPageUrl: String,
    val total: Int
) {

    data class Item(
        val adIndex: Int,
        val `data`: Data,
        val id: Int,
        val tag: Any,
        val trackingData: Any,
        val type: String
    )

    data class Data(
        val actionUrl: String,
        val ad: Boolean,
        val adTrack: Any,
        val author: com.zzz.auvapp.logic.model.Author,
        val backgroundImage: String,
        val autoPlay: Boolean,
        val brandWebsiteInfo: Any,
        val campaign: Any,
        val category: String,
        val collected: Boolean,
        val consumption: com.zzz.auvapp.logic.model.Consumption,
        val content: Content,
        val count: Int,
        val cover: com.zzz.auvapp.logic.model.Cover,
        val dataType: String,
        val date: Long,
        val description: String,
        val descriptionEditor: String,
        val descriptionPgc: Any,
        val duration: Int,
        val favoriteAdTrack: Any,
        val follow: Any,
        val footer: Any,
        val header: Header,
        val icon: String,
        val id: Long,
        val idx: Int,
        val ifLimitVideo: Boolean,
        val image: String,
        val itemList: List<ItemX>,
        val label: Label?,
        val labelList: List<Label>,
        val lastViewTime: Any,
        val library: String,
        val playInfo: List<PlayInfoXX>,
        val playUrl: String,
        val played: Boolean,
        val playlists: Any,
        val promotion: Any,
        val provider: ProviderXX,
        val reallyCollected: Boolean,
        val recallSource: String,
        val recall_source: String,
        val releaseTime: Long,
        val remark: String,
        val resourceType: String,
        val searchWeight: Int,
        val shade: Boolean,
        val shareAdTrack: Any,
        val slogan: Any,
        val src: Int,
        val subTitle: Any,
        val subtitles: List<Any>,
        val tags: List<TagXX>,
        val text: String,
        val thumbPlayUrl: Any,
        val title: String,
        val titleList: List<String>,
        val titlePgc: Any,
        val type: String,
        val videoPosterBean: VideoPosterBeanX,
        val waterMarks: Any,
        val webAdTrack: Any,
        val webUrl: com.zzz.auvapp.logic.model.WebUrl,
        val rightText: String,
        val detail: AutoPlayVideoAdDetail?
    )

    data class AutoPlayVideoAdDetail(
        val actionUrl: String,
        val adTrack: List<Any>,
        val adaptiveImageUrls: String,
        val adaptiveUrls: String,
        val canSkip: Boolean,
        val categoryId: Int,
        val countdown: Boolean,
        val cycleCount: Int,
        val description: String,
        val displayCount: Int,
        val displayTimeDuration: Int,
        val icon: String,
        val id: Long,
        val ifLinkage: Boolean,
        val imageUrl: String,
        val iosActionUrl: String,
        val linkageAdId: Int,
        val loadingMode: Int,
        val openSound: Boolean,
        val position: Int,
        val showActionButton: Boolean,
        val showImage: Boolean,
        val showImageTime: Int,
        val timeBeforeSkip: Int,
        val title: String,
        val url: String,
        val videoAdType: String,
        val videoType: String
    )

    data class Author(
        val adTrack: Any,
        val approvedNotReadyVideoCount: Int,
        val description: String,
        val expert: Boolean,
        val follow: Follow,
        val icon: String,
        val id: Int,
        val ifPgc: Boolean,
        val latestReleaseTime: Long,
        val link: String,
        val name: String,
        val recSort: Int,
        val shield: Shield,
        val videoNum: Int
    )

    data class Consumption(
        val collectionCount: Int,
        val realCollectionCount: Int,
        val replyCount: Int,
        val shareCount: Int
    )

    data class Content(
        val adIndex: Int,
        val `data`: DataX,
        val id: Int,
        val tag: Any,
        val trackingData: Any,
        val type: String
    )

    data class CoverX(
        val blurred: String,
        val detail: String,
        val feed: String,
        val homepage: Any,
        val sharing: Any
    )

    data class Header(
        val actionUrl: String,
        val cover: Any,
        val description: String,
        val font: String,
        val icon: String,
        val iconType: String,
        val id: Int,
        val label: Any,
        val labelList: Any,
        val rightText: String,
        val showHateVideo: Boolean,
        val subTitle: String,
        val subTitleFont: String,
        val textAlign: String,
        val time: Long,
        val title: String
    )

    data class ItemX(
        val adIndex: Int,
        val `data`: DataXX,
        val id: Int,
        val tag: Any,
        val trackingData: Any,
        val type: String
    )

    data class PlayInfoXX(
        val height: Int,
        val name: String,
        val type: String,
        val url: String,
        val urlList: List<UrlXX>,
        val width: Int
    )

    data class ProviderXX(
        val alias: String,
        val icon: String,
        val name: String
    )

    data class TagXX(
        val actionUrl: String,
        val adTrack: Any,
        val bgPicture: String,
        val childTagIdList: Any,
        val childTagList: Any,
        val communityIndex: Int,
        val desc: String,
        val haveReward: Boolean,
        val headerImage: String,
        val id: Int,
        val ifNewest: Boolean,
        val name: String,
        val newestEndTime: Any,
        val tagRecType: String
    )

    data class VideoPosterBeanX(
        val fileSizeStr: String,
        val scale: Double,
        val url: String
    )

    data class WebUrlXX(
        val forWeibo: String,
        val raw: String
    )

    data class Follow(
        val followed: Boolean,
        val itemId: Int,
        val itemType: String
    )

    data class Shield(
        val itemId: Int,
        val itemType: String,
        val shielded: Boolean
    )

    data class DataX(
        val ad: Boolean,
        val adTrack: List<Any>,
        val author: com.zzz.auvapp.logic.model.Author,
        val brandWebsiteInfo: Any,
        val campaign: Any,
        val category: String,
        val collected: Boolean,
        val consumption: com.zzz.auvapp.logic.model.Consumption,
        val cover: com.zzz.auvapp.logic.model.Cover,
        val dataType: String,
        val date: Long,
        val description: String,
        val descriptionEditor: String,
        val descriptionPgc: Any,
        val duration: Int,
        val favoriteAdTrack: Any,
        val id: Long,
        val idx: Int,
        val ifLimitVideo: Boolean,
        val label: Any,
        val labelList: List<Any>,
        val lastViewTime: Any,
        val library: String,
        val playInfo: List<PlayInfo>,
        val playUrl: String,
        val played: Boolean,
        val playlists: Any,
        val promotion: Any,
        val provider: Provider,
        val reallyCollected: Boolean,
        val recallSource: String,
        val recall_source: String,
        val releaseTime: Long,
        val remark: Any,
        val resourceType: String,
        val searchWeight: Int,
        val shareAdTrack: Any,
        val slogan: Any,
        val src: Int,
        val subtitles: List<Any>,
        val tags: List<Tag>,
        val thumbPlayUrl: Any,
        val title: String,
        val titlePgc: Any,
        val type: String,
        val videoPosterBean: VideoPosterBean,
        val waterMarks: Any,
        val webAdTrack: Any,
        val webUrl: com.zzz.auvapp.logic.model.WebUrl
    )

    data class AuthorX(
        val adTrack: Any,
        val approvedNotReadyVideoCount: Int,
        val description: String,
        val expert: Boolean,
        val follow: FollowX,
        val icon: String,
        val id: Int,
        val ifPgc: Boolean,
        val latestReleaseTime: Long,
        val link: String,
        val name: String,
        val recSort: Int,
        val shield: ShieldX,
        val videoNum: Int
    )

    data class ConsumptionX(
        val collectionCount: Int,
        val realCollectionCount: Int,
        val replyCount: Int,
        val shareCount: Int
    )

    data class Cover(
        val blurred: String,
        val detail: String,
        val feed: String,
        val homepage: Any,
        val sharing: Any
    )

    data class PlayInfo(
        val height: Int,
        val name: String,
        val type: String,
        val url: String,
        val urlList: List<Url>,
        val width: Int
    )

    data class Provider(
        val alias: String,
        val icon: String,
        val name: String
    )

    data class Tag(
        val actionUrl: String,
        val adTrack: Any,
        val bgPicture: String,
        val childTagIdList: Any,
        val childTagList: Any,
        val communityIndex: Int,
        val desc: Any,
        val haveReward: Boolean,
        val headerImage: String,
        val id: Int,
        val ifNewest: Boolean,
        val name: String,
        val newestEndTime: Any,
        val tagRecType: String
    )

    data class VideoPosterBean(
        val fileSizeStr: String,
        val scale: Double,
        val url: String
    )

    data class WebUrl(
        val forWeibo: String,
        val raw: String
    )

    data class FollowX(
        val followed: Boolean,
        val itemId: Int,
        val itemType: String
    )

    data class ShieldX(
        val itemId: Int,
        val itemType: String,
        val shielded: Boolean
    )

    data class Url(
        val name: String,
        val size: Int,
        val url: String
    )

    data class DataXX(
        val adTrack: List<Any>,
        val content: ContentX,
        val header: HeaderX,

        val cover: Cover,
        val dailyResource: Boolean,
        val dataType: String,
        val id: Int,
        val nickname: String,
        val resourceType: String,
        val url: String,
        val urls: List<String>,
        val userCover: String
    )

    data class ContentX(
        val adIndex: Int,
        val `data`: DataXXX,
        val id: Int,
        val tag: Any,
        val trackingData: Any,
        val type: String
    )

    data class HeaderX(
        val actionUrl: String,
        val cover: Any,
        val description: Any,
        val font: Any,
        val icon: String,
        val iconType: String,
        val id: Int,
        val label: Any,
        val labelList: Any,
        val rightText: Any,
        val showHateVideo: Boolean,
        val subTitle: Any,
        val subTitleFont: Any,
        val textAlign: String,
        val time: Long,
        val title: String
    )

    data class DataXXX(
        val ad: Boolean,
        val adTrack: List<Any>,
        val author: AuthorXX,
        val brandWebsiteInfo: Any,
        val campaign: Any,
        val category: String,
        val collected: Boolean,
        val consumption: ConsumptionXX,
        val cover: CoverXX,
        val dataType: String,
        val date: Long,
        val description: String,
        val descriptionEditor: String,
        val descriptionPgc: Any,
        val duration: Int,
        val favoriteAdTrack: Any,
        val id: Int,
        val idx: Int,
        val ifLimitVideo: Boolean,
        val label: Any,
        val labelList: List<Any>,
        val lastViewTime: Any,
        val library: String,
        val playInfo: List<PlayInfoX>,
        val playUrl: String,
        val played: Boolean,
        val playlists: Any,
        val promotion: Any,
        val provider: ProviderX,
        val reallyCollected: Boolean,
        val recallSource: Any,
        val recall_source: Any,
        val releaseTime: Long,
        val remark: Any,
        val resourceType: String,
        val searchWeight: Int,
        val shareAdTrack: Any,
        val slogan: Any,
        val src: Any,
        val subtitles: List<Any>,
        val tags: List<TagX>,
        val thumbPlayUrl: Any,
        val title: String,
        val titlePgc: Any,
        val type: String,
        val videoPosterBean: Any,
        val waterMarks: Any,
        val webAdTrack: Any,
        val webUrl: WebUrlX
    )

    data class AuthorXX(
        val adTrack: Any,
        val approvedNotReadyVideoCount: Int,
        val description: String,
        val expert: Boolean,
        val follow: FollowXX,
        val icon: String,
        val id: Int,
        val ifPgc: Boolean,
        val latestReleaseTime: Long,
        val link: String,
        val name: String,
        val recSort: Int,
        val shield: ShieldXX,
        val videoNum: Int
    )

    data class ConsumptionXX(
        val collectionCount: Int,
        val realCollectionCount: Int,
        val replyCount: Int,
        val shareCount: Int
    )

    data class CoverXX(
        val blurred: String,
        val detail: String,
        val feed: String,
        val homepage: String,
        val sharing: Any
    )

    data class PlayInfoX(
        val height: Int,
        val name: String,
        val type: String,
        val url: String,
        val urlList: List<UrlX>,
        val width: Int
    )

    data class ProviderX(
        val alias: String,
        val icon: String,
        val name: String
    )

    data class TagX(
        val actionUrl: String,
        val adTrack: Any,
        val bgPicture: String,
        val childTagIdList: Any,
        val childTagList: Any,
        val communityIndex: Int,
        val desc: String,
        val haveReward: Boolean,
        val headerImage: String,
        val id: Int,
        val ifNewest: Boolean,
        val name: String,
        val newestEndTime: Any,
        val tagRecType: String
    )

    data class WebUrlX(
        val forWeibo: String,
        val raw: String
    )

    data class FollowXX(
        val followed: Boolean,
        val itemId: Int,
        val itemType: String
    )

    data class ShieldXX(
        val itemId: Int,
        val itemType: String,
        val shielded: Boolean
    )

    data class UrlX(
        val name: String,
        val size: Int,
        val url: String
    )

    data class UrlXX(
        val name: String,
        val size: Int,
        val url: String
    )

    data class Label(val actionUrl: String?, val text: String?, val card: String, val detail: Any?)
}