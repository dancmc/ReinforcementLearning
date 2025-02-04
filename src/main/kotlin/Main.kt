import MAB.Companion.epsilonGreedy
import MAB.Companion.uct

class Main {

    companion object {
        @JvmStatic
        fun main(args:Array<String>){

            // initialise map with 10x6
            val map = Map(10,6)

            // set bottom row to negative reward and terminal
            (0..9).forEach {i->
                Coordinate(i,0).let { c->
                    map.setReward(c, -100)
                    map.setStatus(c, Status.DEATH)
                }

            }

//            (1..8).forEach {i->
//                Coordinate(i,2).let { c->
//                    map.setReward(c, -100)
//                }
//
//            }



            // set rightmost cell on second row to positive reward and terminal
            map.setReward(Coordinate(9,1), 100)
            map.setStatus(Coordinate(9,1), Status.GOAL)

            // set secondary lousier reward nearer to start
            map.setReward(Coordinate(0,5), 50)
            map.setStatus(Coordinate(0,5), Status.GOAL)

            // pass starting coordinate -> get action from policy -> move -> update on/off policy -> pass new coordinate back if not terminal
            val startCoord = Coordinate(0,1)
            val table = QLearningTable(10,6)

            val q = QLearning(map, table, epsilonGreedy(0.7), learningRate = 0.3, discount = 0.9)


            (0..10000).forEach{

                if(it == 700){
                    print("")
                }
                                q.runSimulationOffPolicy(startCoord)
//                q.runSimulationOnPolicy(startCoord)
            }

            // ON POLICY RESULTS IN FEWER DEATHS WHILE LEARNING, AND A MORE CAUTIOUS ROUTE
            // OFF POLICY GETS THE OPTIMAL PATH, PREFERRED IF ABLE TO TRAIN OFFLINE
            // with sarsa, square west of goal will point east. However, square west of that will likely point north
            // because when g-2 steps east, the q function update for (g-2,E) uses the q function for the next step generated by the (e greedy) policy from g-1, which could be south/death/negative
            // therefore, g-2 will end up preferring to go north and be safe instead. Same for practically every square on the bottom row
            // with off policy learning, the q function update for (g-2,E) uses the best q function from g-1, so basically ignores the risk of death
            // thus finds the optimal route but faces more danger during learning


            // uct is drastically better than epsilon greedy in terms of safety - why?

            // What happens when you add a secondary distraction goal (with lower reward) nearer to start?
            // - often the exploration never even reaches the main goal
            // - one way to combat this is off policy learning with high epsilon (0.8), this encourages a lot of random exploration (and a lot of dying) to generate learning and data, but the off policy evaluation still outputs the optimal path

            table.print()


        }



    }

}