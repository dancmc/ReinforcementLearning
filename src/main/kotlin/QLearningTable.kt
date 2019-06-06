class QLearningTable(val width:Int, val height:Int) {

    private val table = HashMap<Coordinate, ArrayList<Action>>()
    private val deathTable = HashSet<Coordinate>()
    private val goalTable = HashSet<Coordinate>()

    var deaths = 0
    var goals = 0

    init {
        (0 until width).forEach { x->
            (0 until height).forEach { y->
                val coordinate = Coordinate(x,y)
                val cellArray = ArrayList<Action>(4).apply {
                    this.add(Action(Direction.NORTH,0.0))
                    this.add(Action(Direction.SOUTH,0.0))
                    this.add(Action(Direction.EAST,0.0))
                    this.add(Action(Direction.WEST,0.0))
                }
                table[coordinate] = cellArray
            }
        }
    }

    fun lookupCoordinate(c:Coordinate):ArrayList<Action>?{
        return table[c]
    }

    fun bestQFunction(c:Coordinate):Double{
        return table[c]?.maxBy { it.qvalue }?.qvalue ?: 0.0
    }

    fun qFunction(c:Coordinate, dir:Direction):Double?{
        return table[c]?.find { it.dir == dir }?.qvalue
    }

    fun updateQFunction(c:Coordinate, dir:Direction, v:Double){
        table[c]?.find { it.dir == dir }?.apply {
            qvalue = v
            numberTaken++
        }
    }

    fun removeAction(c:Coordinate, dir: Direction){
        table[c]?.removeIf { it.dir==dir }
    }

    fun updateDeath(c:Coordinate){
        deathTable.add(c)
    }

    fun updateGoal(c:Coordinate){
        goalTable.add(c)
    }

    fun print(){
        (0 until height).reversed().forEach { y->
            (0 until width).forEach { x->
                val coord = Coordinate(x,y)
                when{
                    deathTable.contains(coord) -> print("D ")
                    goalTable.contains(coord) -> print("G ")
                    else ->{
                        val cellArray = table[coord]!!

                        if(cellArray.sumBy { it.numberTaken }==0){
                            print("O ")
                        } else {

                            val dir = cellArray.maxBy { it.qvalue }!!.dir
                            print("$dir ")
                        }
                    }
                }

            }
            print("\n")
        }
        println()
        println("Deaths : $deaths, Goals : $goals")
    }

    data class Action(val dir:Direction, var qvalue:Double, var numberTaken:Int = 0)
}