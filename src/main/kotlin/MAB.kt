import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.random.Random

class MAB {

    companion object {

        fun epsilonGreedy(epsilon:Double):(ArrayList<QLearningTable.Action>)->Direction{

            return { actions->
                if (Random.nextDouble() < epsilon) {
                    // take a random action
                    actions.random().dir
                } else {
                    val dirs = ArrayList<Direction>(4)
                    val maxQ = actions.maxBy { it.qvalue }!!.qvalue
                    actions.forEach {a->
                        if(a.qvalue== maxQ){
                            dirs.add(a.dir)
                        }
                    }
                    if(dirs.size==1) dirs[0] else dirs.random()
                }
            }
        }

        fun uct():(ArrayList<QLearningTable.Action>)->Direction{

            val explorationConstant = 2.0/ sqrt(2.0)


            return {actions->
                var maxVal = Double.MIN_VALUE
                val dirs = ArrayList<Direction>(4)
                val ns = actions.sumBy { it.numberTaken }.toDouble()
                actions.forEach {a->
                    val value = if(a.numberTaken==0) Double.MAX_VALUE else a.qvalue + explorationConstant * sqrt(2* ln(ns)/a.numberTaken)
                    when{
                        value > maxVal -> {
                            dirs.clear()
                            dirs.add(a.dir)
                            maxVal = value
                        }
                        value == maxVal-> dirs.add(a.dir)
                    }
                }
                if(dirs.size==1) dirs[0] else dirs.random()
            }

        }

    }

}