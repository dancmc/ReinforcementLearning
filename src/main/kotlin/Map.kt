/*
    Defines a grid map and allows you to :
        - set rewards for each square
        - set terminal/goal state for each square
        - pass input coordinates and a direction, receive result validity, end coordinates, reward
    Coordinates start from bottom left

 */
class Map(width: Int, height: Int) {

    private val grid = Array(width) {
        Array(height){
            Square()
        }
    }

    fun setReward(c: Coordinate, reward:Int){
        grid[c.x][c.y].reward = reward
    }

    fun setStatus(c: Coordinate, status:Status){
        grid[c.x][c.y].status = status
    }

    fun move(c:Coordinate, dir:Direction):Result{

        val endCoord = when(dir){
            Direction.NORTH -> Coordinate(c.x, c.y+1)
            Direction.SOUTH -> Coordinate(c.x, c.y-1)
            Direction.EAST -> Coordinate(c.x+1, c.y)
            Direction.WEST -> Coordinate(c.x-1, c.y)
        }
        return try{
            val endSquare = grid[endCoord.x][endCoord.y]
            Result(true, endCoord, endSquare.reward, endSquare.status)
        }catch(e:ArrayIndexOutOfBoundsException){
            Result(false, Coordinate(0,0), 0, Status.NORMAL)
        }
    }


    data class Result(val valid:Boolean, val coordinate: Coordinate, val reward:Int, val status:Status)

    private data class Square(var reward:Int = 0, var status: Status =Status.NORMAL)
}