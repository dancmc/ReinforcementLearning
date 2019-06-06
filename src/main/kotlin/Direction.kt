enum class Direction {

    NORTH {
        override fun toString(): String {
            return "N"
        }
    },
    SOUTH {
        override fun toString(): String {
            return "S"
        }
    },
    EAST {
        override fun toString(): String {
            return "E"
        }
    },
    WEST {
        override fun toString(): String {
            return "W"
        }

    }

}