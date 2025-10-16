package student.projects.studybreakbuddy

data class AdviceResponse(
    val slip: AdviceSlip
)

data class AdviceSlip(
    val id: Int,
    val advice: String
)

data class SearchResponse(
    val slips: List<AdviceSlip>?
)

data class GiphyResponse(
    val data: GiphyData
)

data class GiphyData(
    val images: GiphyImages
)

data class GiphyImages(
    val original: GiphyOriginal
)

data class GiphyOriginal(
    val url: String
)
