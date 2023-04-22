package river.exertion.kcop.simulation.view

enum class DisplayViewPaneType {

    // MEDIUM through TINY are best for font

    // FULL == 0

    FULL_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },
    FULL_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },
    FULL_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },
    FULL_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },
    FULL_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },
    FULL_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },
    FULL_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },
    TITLE_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },
    LARGE_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },
    MEDIUM_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },
    SMALL_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },
    TINY_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },
    UNIT_BY_FULL {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
    },

    // TITLE == 1

    TITLE_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },
    TITLE_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },
    TITLE_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },
    TITLE_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },
    TITLE_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },
    TITLE_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },
    LARGE_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },
    MEDIUM_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },
    SMALL_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },
    TINY_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },
    UNIT_BY_TITLE {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
    },

    // LARGE == 2

    LARGE_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },
    LARGE_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },
    LARGE_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },
    LARGE_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },
    LARGE_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },
    MEDIUM_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },
    SMALL_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },
    TINY_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },
    UNIT_BY_LARGE {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
    },

    // MEDIUM == 3

    MEDIUM_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },
    MEDIUM_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },
    MEDIUM_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },
    MEDIUM_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },
    SMALL_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },
    TINY_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },
    UNIT_BY_MEDIUM {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
    },

    // SMALL == 4

    SMALL_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },
    SMALL_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },
    SMALL_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },
    TINY_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },
    UNIT_BY_SMALL {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
    },

    // TINY == 5

    TINY_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },
    TINY_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },
    UNIT_BY_TINY {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
    },

    // UNIT == 6

    UNIT_BY_UNIT {
        override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
        override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
    },

    ;
    abstract fun width(screenWidth : Float) : Float
    abstract fun height(screenHeight : Float) : Float

}