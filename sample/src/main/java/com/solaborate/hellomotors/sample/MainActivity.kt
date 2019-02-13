package com.solaborate.hellomotors.sample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.solaborate.hellomotors.CameraMotorInteractor
import com.solaborate.hellomotors.MotorMovement
import kotlinx.android.synthetic.main.activity_main.*

const val TAG = "HelloMotors"

/**
 * Created by Kushtrim Pacaj on 13/02/19.
 * kushtrimpacaj@gmail.com
 */
class MainActivity : AppCompatActivity() {

    private var motorInteractor = CameraMotorInteractor()

    val handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpClickListener()
        try {
            motorInteractor.init()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        registerReceiver(motorStuckEventReceiver, IntentFilter("android.intent.action.hello.button"))
    }


    private fun setUpClickListener() {

        setupClicksForView(view = button_up, motorMovement = MotorMovement.UP)
        setupClicksForView(view = button_down, motorMovement = MotorMovement.DOWN)
        setupClicksForView(view = button_left, motorMovement = MotorMovement.LEFT)
        setupClicksForView(view = button_right, motorMovement = MotorMovement.RIGHT)


        start_random_movement.setOnClickListener {
            startService(
                Intent(this, RandomMovementService::class.java)
            )
        }
        stop_random_movement.setOnClickListener {
            stopService(
                Intent(this, RandomMovementService::class.java)
            )
        }
    }

    private fun setupClicksForView(view: View, motorMovement: MotorMovement) {
        view.setOnClickListener { motorInteractor.sendCommand(motorMovement) }

        view.setOnLongClickListener {
            //when the user long-clicks a button, continuously move motors every 50ms, until the users releases the long-press
            handler.postDelayed(object : Runnable {
                override fun run() {
                    motorInteractor.sendCommand(motorMovement)
                    handler.postDelayed(this, 50)
                }
            }, 50)

        }
        view.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                handler.removeCallbacksAndMessages(null)
            }
            return@setOnTouchListener false
        }
    }

    override fun onDestroy() {
        motorInteractor.destroy()
        unregisterReceiver(motorStuckEventReceiver)
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                motorInteractor.sendCommand(MotorMovement.UP)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                motorInteractor.sendCommand(MotorMovement.DOWN)
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                motorInteractor.sendCommand(MotorMovement.LEFT)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                motorInteractor.sendCommand(MotorMovement.RIGHT)
                return true
            }
        }
        return false
    }

    private var motorStuckEventReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val name = intent.getIntExtra("name", -1)

            when (name) {
                142 -> Log.d(TAG, "Reached end vertically")
                143 -> Log.d(TAG, "Reached end horizontally")
                else -> {
                }
            }
        }
    }
}

