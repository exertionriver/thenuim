package river.exertion.kcop.view.layout.displayViewLayout

import river.exertion.kcop.view.layout.ViewType

enum class DVPaneType {

    // FULL

    FULL_BY_FULL {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.FULL
    },
    FULL_BY_TITLE {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.TITLE
    },
    FULL_BY_LARGE {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.LARGE
    },
    FULL_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.MEDIUM
    },
    FULL_BY_SMALL {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.SMALL
    },
    FULL_BY_TINY {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.TINY
    },
    FULL_BY_UNIT {
        override fun widthDimension() = DVPDimension.FULL
        override fun heightDimension() = DVPDimension.UNIT
    },
    TITLE_BY_FULL {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.FULL
    },
    LARGE_BY_FULL {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.FULL
    },
    MEDIUM_BY_FULL {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.FULL
    },
    SMALL_BY_FULL {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.FULL
    },
    TINY_BY_FULL {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.FULL
    },
    UNIT_BY_FULL {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.FULL
    },

    // TITLE

    TITLE_BY_TITLE {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.TITLE
    },
    TITLE_BY_LARGE {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.LARGE
    },
    TITLE_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.MEDIUM
    },
    TITLE_BY_SMALL {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.SMALL
    },
    TITLE_BY_TINY {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.TINY
    },
    TITLE_BY_UNIT {
        override fun widthDimension() = DVPDimension.TITLE
        override fun heightDimension() = DVPDimension.UNIT
    },
    LARGE_BY_TITLE {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.TITLE
    },
    MEDIUM_BY_TITLE {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.TITLE
    },
    SMALL_BY_TITLE {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.TITLE
    },
    TINY_BY_TITLE {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.TITLE
    },
    UNIT_BY_TITLE {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.TITLE
    },

    // LARGE

    LARGE_BY_LARGE {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.LARGE
    },
    LARGE_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.MEDIUM
    },
    LARGE_BY_SMALL {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.SMALL
    },
    LARGE_BY_TINY {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.TINY
    },
    LARGE_BY_UNIT {
        override fun widthDimension() = DVPDimension.LARGE
        override fun heightDimension() = DVPDimension.UNIT
    },
    MEDIUM_BY_LARGE {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.LARGE
    },
    SMALL_BY_LARGE {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.LARGE
    },
    TINY_BY_LARGE {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.LARGE
    },
    UNIT_BY_LARGE {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.LARGE
    },

    // MEDIUM

    MEDIUM_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.MEDIUM
    },
    MEDIUM_BY_SMALL {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.SMALL
    },
    MEDIUM_BY_TINY {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.TINY
    },
    MEDIUM_BY_UNIT {
        override fun widthDimension() = DVPDimension.MEDIUM
        override fun heightDimension() = DVPDimension.UNIT
    },
    SMALL_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.MEDIUM
    },
    TINY_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.MEDIUM
    },
    UNIT_BY_MEDIUM {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.MEDIUM
    },

    // SMALL

    SMALL_BY_SMALL {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.SMALL
    },
    SMALL_BY_TINY {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.TINY
    },
    SMALL_BY_UNIT {
        override fun widthDimension() = DVPDimension.SMALL
        override fun heightDimension() = DVPDimension.UNIT
    },
    TINY_BY_SMALL {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.SMALL
    },
    UNIT_BY_SMALL {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.SMALL
    },

    // TINY

    TINY_BY_TINY {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.TINY
    },
    TINY_BY_UNIT {
        override fun widthDimension() = DVPDimension.TINY
        override fun heightDimension() = DVPDimension.UNIT
    },
    UNIT_BY_TINY {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.TINY
    },

    // UNIT

    UNIT_BY_UNIT {
        override fun widthDimension() = DVPDimension.UNIT
        override fun heightDimension() = DVPDimension.UNIT
    },

    ;
    abstract fun widthDimension() : DVPDimension
    abstract fun heightDimension() : DVPDimension
    open fun width(screenWidth : Float) = widthDimension().width(screenWidth)
    open fun height(screenHeight : Float) = heightDimension().height(screenHeight)

    companion object {
        private fun byDimensions(widthDim : DVPDimension, heightDim : DVPDimension) = entries.firstOrNull { it.widthDimension() == widthDim && it.heightDimension() == heightDim } ?: UNIT_BY_UNIT
        fun byTags(widthTag : String?, heightTag : String?) = byDimensions(DVPDimension.byTag(widthTag), DVPDimension.byTag(heightTag))
    }

    enum class DVPDimension {
        UNIT {
            override fun tag() = "unit"
            override fun width(screenWidth: Float) = ViewType.seventhWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.seventhHeight(screenHeight)
         },
        TINY {
            override fun tag() = "tiny"
            override fun width(screenWidth: Float) = ViewType.sixthWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.sixthHeight(screenHeight)
        },
        SMALL {
            override fun tag() = "small"
            override fun width(screenWidth: Float) = ViewType.fifthWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.fifthHeight(screenHeight)
        },
        MEDIUM {
            override fun tag() = "medium"
            override fun width(screenWidth: Float) = ViewType.fourthWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.fourthHeight(screenHeight)
        },
        LARGE {
            override fun tag() = "large"
            override fun width(screenWidth: Float) = ViewType.thirdWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.thirdHeight(screenHeight)
        },
        TITLE {
            override fun tag() = "title"
            override fun width(screenWidth: Float) = ViewType.secondWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.secondHeight(screenHeight)
        },
        FULL {
            override fun tag() = "full"
            override fun width(screenWidth: Float) = ViewType.firstWidth(screenWidth)
            override fun height(screenHeight: Float) = ViewType.firstHeight(screenHeight)
        }
        ;
        abstract fun tag() : String
        abstract fun width(screenWidth : Float) : Float
        abstract fun height(screenHeight : Float) : Float

        companion object {
            fun byTag(tag : String?) = values().firstOrNull { it.tag() == tag } ?: UNIT
        }
    }

}