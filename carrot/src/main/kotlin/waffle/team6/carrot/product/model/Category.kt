package waffle.team6.carrot.product.model

import waffle.team6.carrot.product.exception.InvalidCategoryValueException

enum class Category(
    val value: Int
) {
    DIGITAL_DEVICE(1),
    HOME_APPLIANCE(2),
    FURNITURE_AND_INTERIOR(3),
    KIDS(4),
    LIVING_AND_FOOD(5),
    KIDS_BOOK(6),
    SPORTS_AND_LEISURE(7),
    WOMEN_STUFF(8),
    WOMEN_CLOTHES(9),
    MEN_STUFF_AND_CLOTHES(10),
    GAME_AND_HOBBIES(11),
    BEAUTY_AND_COSMETICS(12),
    PET(13),
    BOOKS_AND_TICKETS_AND_RECORDS(14),
    BOTANICAL(15),
    ETC(16),
    I_AM_BUYING(17);

    companion object {
        fun from (findValue: Int): Category = Category.values().first { it.value == findValue }
    }
}