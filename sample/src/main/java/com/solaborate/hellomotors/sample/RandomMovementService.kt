package com.solaborate.hellomotors.sample

import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.solaborate.hellomotors.CameraMotorInteractor
import com.solaborate.hellomotors.MotorMovement
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Kushtrim Pacaj on 13/02/19.
 * kushtrimpacaj@gmail.com
 */

class RandomMovementService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    private var motorInteractor = CameraMotorInteractor()

    private var isRandomMovementStarted = false
    private var directionOfMovement: MotorMovement? = null

    private var randomMovementHandler: Handler? = null
    private var resetMovementTimer: Subscription? = null

    private var randomMovementCounter = 0;

    override fun onCreate() {
        super.onCreate()

        try {
            motorInteractor.init()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }


        registerReceiver(motorStuckEventReceiver, IntentFilter("android.intent.action.hello.button"))

        if (!isRandomMovementStarted) {
            isRandomMovementStarted = true
            directionOfMovement = generateRandomDirection(null)
            startRandomMovement(directionOfMovement)
        }

        setServiceAsForeground()
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(motorStuckEventReceiver)
        randomMovementCounter = 0
        randomMovementHandler!!.removeCallbacksAndMessages(null)
        resetMovementTimer?.unsubscribe()
    }


    private var motorStuckEventReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val name = intent.getIntExtra("name", -1)

            when (name) {
                142 -> Log.d(TAG, "142 -> Stuck in vertical. Direction was $directionOfMovement")
                143 -> Log.d(TAG, "143 -> Stuck in horizontal. Direction was $directionOfMovement")
                else -> {
                }
            }
            //process events if and only if we started random movements
            if (isRandomMovementStarted) {


                when (name) {
                    142, 143 -> {
                        resetMovement()
                    }
                }

            }
        }
    }

    private fun resetMovement() {
        Log.d(TAG, "Stopping all previous movements")
        randomMovementHandler?.removeCallbacksAndMessages(null)
        directionOfMovement = generateRandomDirection(excludeMovement = directionOfMovement)
        Log.d(TAG, "New movement direction is $directionOfMovement")
        startRandomMovement(directionOfMovement)
    }


    private fun startRandomMovement(directionOfMovement: MotorMovement?) {
        //cleanup

        if (randomMovementCounter == 5){
            stopSelf()
            return
        }
        randomMovementCounter++


        resetMovementTimer?.unsubscribe()
        randomMovementHandler?.removeCallbacksAndMessages(null)


        randomMovementHandler = Handler()
        randomMovementHandler?.postDelayed(object : Runnable {
            override fun run() {
                Log.d(TAG, "Moving in direction $directionOfMovement")
                motorInteractor.sendCommand(directionOfMovement)
                randomMovementHandler?.postDelayed(this, 100)
            }

        }, 400)

        resetMovementTimer = Observable.timer(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d(TAG, "resetMovementTimer")
                resetMovement()
            }

    }


    private fun generateRandomDirection(excludeMovement: MotorMovement?): MotorMovement? {
        val list = MotorMovement.values().toMutableList().minus(excludeMovement)
        return list[Random().nextInt(list.size)]
    }

    private fun setServiceAsForeground() {
        val notification = Notification.Builder(this)
            .setContentTitle("Hello")
            .setContentText("Solaborate")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_foreground))
            .setTicker("Solaborate")
            .build()

        startForeground(42, notification)
    }

}