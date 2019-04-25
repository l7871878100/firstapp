package com.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View

class TetrisActivityAW : AppCompatActivity() {

    val height = 50
    val width = 16
    var currentRow = 0
    var pause = false
    var plaing = false

    var dowBlock = mutableListOf<MutableList<Int>>()
    var nextBlock = mutableListOf<MutableList<Int>>()

    val handle = Handler()
    val runnable: Runnable

    var allRows: MutableList<MutableList<Int>> = mutableListOf()

    init {
        for (rowIndex in 0 until height - 1) {
            val columns = mutableListOf<Int>()
            for (columnIndex in 0 until width - 1) {
                columns[columnIndex] = 0
            }
            allRows[rowIndex] = columns
        }
        runnable = object : Runnable {
            override fun run() {
                update()
                handle.postDelayed(this, 1000)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tetris_aw)
    }

    /**
     * 开始游戏
     */
    fun startGame() {
        plaing = true
        pause = false
        handle.postDelayed(runnable, 1000)
    }

    fun pauseGame() {
        this.pause = true
    }

    fun stopGame() {
        handle.removeCallbacks(runnable)
        plaing = false
        pause = false
    }

    fun clickLeft(view: View) {
        moveLeft()
    }

    fun clickRight(view: View) {
        moveRight()
    }

    fun clickTop(view: View) {
        if (pause) {
            stopGame()
            plaing = false
        } else {
            pauseGame()
        }
    }

    fun clickBottom(view: View) {
        if (!plaing) {
            startGame()
        } else if (pause) {
            pause = false
        } else {
//            直接落下
            while (update()) {
            }
        }
    }
// 旋转
    fun clickRotate(view: View) {

    }


    /**
     * 定时下落更新
     */
    fun update(): Boolean {
        return if (pause) {
            false
        }else{
            if (canDown()) {
                currentRow++
                true
            } else {
                next()
                false
            }
        }

    }

    /**
     * 判断是否还能继续下落
     */
    fun canDown(): Boolean {

        var index = 0
        while (currentRow - index <= 0) {
            for (dIndex in dowBlock.size - 1 downTo 0) {
                val column = dowBlock[dIndex]
                val rIndex = currentRow + 1 + dIndex - dowBlock.size
                if (rIndex < 0) continue
                val tRow = allRows[rIndex]
                for (cIndex in 0 until column.size - 1) {
                    if (column[cIndex] + tRow[cIndex] > 0) {
                        return false
                    }
                }
            }
            index++
        }

        return true
    }

    fun canLeft(): Boolean = !dowBlock.any { it[0] == 1 }
    fun moveLeft() {
        if (canLeft()) {
            dowBlock.forEach {
                it.removeAt(0)
                it.add(0)
            }

        }
    }

    fun canRight(): Boolean = !dowBlock.any { it.last() == 1 }

    fun moveRight() {
        if (canRight()) {
            dowBlock.forEach {
                it.remove(it.lastIndex)
                it.add(0, 0)
            }

        }
    }

    /**
     * 产生下一个方块
     */
    fun next() {

        nextBlock = mutableListOf()
        for (rowIndex in 0 until 3) {
            val columns = mutableListOf<Int>()
            for (columnIndex in 0 until width - 1) {
                columns[columnIndex] = 0
            }
            nextBlock[rowIndex] = columns
        }
        currentRow = 0

    }
}
