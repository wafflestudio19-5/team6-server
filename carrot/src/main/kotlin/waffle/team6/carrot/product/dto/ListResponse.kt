package waffle.team6.carrot.product.dto

data class ListResponse<T>(
    val count: Int,
    val results: List<T>
) {
    constructor(list: List<T>?): this(
        list?.size ?: 0,
        list.orEmpty()
    )
}