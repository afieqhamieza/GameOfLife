package com.example.demo.app

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.*

var world = arrayOf<Array<Boolean>>()
const val START = 0
const val END = 10
const val LIVE_CELL_PERCENTAGE = 15

class MyApp: App(MainView::class, Styles::class)

class MainView : View() {

    override val root = flowpane {}

    val grid = gridpane {
        vgap = 5.0
        hgap = 5.0
        padding = insets(15)
    }

    fun<T> isEqual(first: Array<Array<T>>, second: Array<Array<T>>): Boolean {
        if (first == second) return true
        if (first == null || second == null) return false
        if (first.size != second.size) return false
        for (i in first.indices) {
            if (first[i].size != second[i].size) return false
            for (j in first[i].indices) {
                if (!first[i][j]?.equals(second[i][j])!!) return false
            }
        }
        return true
    }

    fun gameOfLife():Boolean{
        val secondWorld = worldCopy()
        for(i in START..END) {
            for (j in START..END) {
                val neighbors = neighbors(secondWorld,i,j)
                if(secondWorld[i][j])
                {
                    world[i][j]= neighbors==2 || neighbors==3
                }
                else{
                    world[i][j]= neighbors==3
                }
            }
        }

        val TwoWorldIsEqual = isEqual(secondWorld, world)
        if (TwoWorldIsEqual) return true else return false
    }

    fun neighbors(worldCopy : Array<Array<Boolean>>, i:Int , j:Int):Int{
        var neighbors =0
        var im1 = i-1
        var jm1 = j-1
        var ip1 = i+1
        var jp1 = j+1

        if(im1 < START)
            im1 = END
        if(ip1 > END)
            ip1 = START
        if(jm1 < START)
            jm1 = END
        if(jp1 > END)
            jp1 = START

        if(worldCopy[im1][j])
            neighbors++
        if(worldCopy[i][jm1])
            neighbors++
        if(worldCopy[ip1][j])
            neighbors++
        if(worldCopy[i][jp1])
            neighbors++
        if(worldCopy[im1][jm1])
            neighbors++
        if(worldCopy[ip1][jp1])
            neighbors++
        if(worldCopy[im1][jp1])
            neighbors++
        if(worldCopy[ip1][jm1])
            neighbors++

        return neighbors

    }

    fun worldCopy(): Array<Array<Boolean>>{
        var worldCopy = arrayOf<Array<Boolean>>()
        for(i in START..END) {
            var internalArr = arrayOf<Boolean>()
            for (j in START..END) {
                internalArr += world[i][j]
            }
            worldCopy += internalArr
        }
        return worldCopy
    }

    fun initWorld(){
        for(i in START..END){
            var internalArr = arrayOf<Boolean>()
            for (j in START..END)
            {
                internalArr += randomLives()
            }
            world += internalArr
        }
    }

    fun randomLives():Boolean = Math.random() * 100 <= LIVE_CELL_PERCENTAGE

    fun drawWorld(){
        var alive: Boolean

        for (i in START..END) {
            for (j in START..END) {
                alive = world[i][j]
                val rect = Rectangle(15.0, 15.0,
                        if (alive) Color.DARKGREEN else Color.LIGHTGRAY)
                grid.add(rect, i, j)
            }
        }
    }

    init {
        print("initializing world...")
        initWorld()

        drawWorld()
        gameOfLife()

        button("Next View") {
            action {
                println("Button pressed!")
                drawWorld()
                gameOfLife()
            }
        }
    }
}
