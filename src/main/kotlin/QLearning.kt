import kotlin.random.Random

class QLearning(val map:Map,val qTable:QLearningTable, val mabFunction:(ArrayList<QLearningTable.Action>)->Direction, val learningRate:Double=0.5, val discount:Double=0.9) {




    fun runSimulationOffPolicy(startCoordinate: Coordinate){
        var coord :Coordinate?= startCoordinate

        while(coord!=null) {
            coord = moveAndUpdateOffPolicy(coord)
        }
    }


    // returns new coordinate, or null if terminal or no moves
    fun moveAndUpdateOffPolicy(c:Coordinate):Coordinate?{
        val direction = getDirection(c) ?: return null

        val result = map.move(c, direction)


        if(result.valid){

//            println("${result.coordinate.x}, ${result.coordinate.y}")

            // if terminal, return null coord, but still update qvalue
            updateQOffPolicy(c, direction, result)

            return when(result.status){
                Status.NORMAL -> result.coordinate
                Status.DEATH->{
                    qTable.updateDeath(result.coordinate)
                    qTable.deaths++
                    null
                }
                Status.GOAL->{
                    qTable.updateGoal(result.coordinate)
                    qTable.goals++
                    null
                }
            }


        } else {
            // if move is not even possible
            qTable.removeAction(c,direction)
            return c
        }


    }

    fun updateQOffPolicy(c:Coordinate, direction:Direction, result:Map.Result){
        val oldValue = qTable.qFunction(c, direction)?: return
        val bestNextQ = qTable.bestQFunction(result.coordinate)
        val newValue = oldValue+ learningRate *(result.reward + discount*bestNextQ-oldValue)
        qTable.updateQFunction(c,direction, newValue)
    }

    fun runSimulationOnPolicy(startCoordinate: Coordinate){
        var coord :Coordinate?= startCoordinate

        var direction = getDirection(coord!!)!!

        while(coord!=null) {
            val result = moveAndUpdateOnPolicy(coord, direction)
            // if returned coordinate is same as input coord, that means prev direction was illegal, try again
            direction = if(coord == result.first) getDirection(coord)!! else result.second
            coord = result.first

        }
    }

    fun moveAndUpdateOnPolicy(c:Coordinate, direction: Direction):Pair<Coordinate?, Direction>{

        val result = map.move(c, direction)


        if(result.valid){

//            println("${result.coordinate.x}, ${result.coordinate.y}")

            val nextDirection = getDirection(result.coordinate)?: return Pair(null, Direction.NORTH)

            updateQOnPolicy(c, direction, nextDirection, result)

            // if terminal, return null coord, but still update qvalue
            return when(result.status){
                Status.NORMAL -> Pair(result.coordinate, nextDirection)
                Status.DEATH->{
                    qTable.updateDeath(result.coordinate)
                    qTable.deaths++
                    Pair(null, Direction.NORTH)
                }
                Status.GOAL->{
                    qTable.updateGoal(result.coordinate)
                    qTable.goals++
                    Pair(null, Direction.NORTH)
                }
            }
        } else {
            // if move is not even possible
            qTable.removeAction(c,direction)
            return Pair(c, direction)
        }


    }

    fun updateQOnPolicy(c:Coordinate, direction:Direction, nextDirection:Direction, result:Map.Result){
        val oldValue = qTable.qFunction(c, direction)?: return
        val nextQ = qTable.qFunction(result.coordinate, nextDirection)!!
        val newValue = oldValue+ learningRate *(result.reward + discount*nextQ-oldValue)
        qTable.updateQFunction(c,direction, newValue)
    }

    fun getDirection(c:Coordinate):Direction?{
        val actions = qTable.lookupCoordinate(c)
        if(actions == null || actions.size==0){
            return null
        }
        return mabFunction(actions)
    }



}