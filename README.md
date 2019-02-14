HELLO Motors
============

[ ![Download](https://api.bintray.com/packages/solaborate/maven/com.solaborate.hellomotors/images/download.svg) ](https://bintray.com/solaborate/maven/com.solaborate.hellomotors/_latestVersion)

This is a library to allow developers to control the camera motors on Solaborate HELLO2, in order to achieve tilting funcionality.

Sample usage (see the [sample app][sample_link] for more complex examples) :

```kotlin

private var motorInteractor = CameraMotorInteractor()
motorInteractor.init()       // for example in onCreate
...
motorInteractor.sendCommand(MotorMovement.UP)
motorInteractor.sendCommand(MotorMovement.DOWN)
motorInteractor.sendCommand(MotorMovement.LEFT)
motorInteractor.sendCommand(MotorMovement.RIGHT)
...
motorInteractor.destroy()    // call it onDestroy, in order to close the connection 
```

Note that the movement is discrete, one call moves the motors for one 'unit of movement', a very low angle.
To continually move, you must call it more than once.
```kotlin
   for (i in 1..20){
            motorInteractor.sendCommand(MotorMovement.LEFT)
            Thread.sleep(50)  // don't do this on UI thread.
        }
```

Change the timing between commands in order to adjust the perceived smoothness of movement.



Download
--------

```groovy
dependencies {
  implementation 'com.solaborate:hellomotors:1.0.0'
}
```


License
-------

    Copyright 2019 Solaborate

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [sample_link]: https://github.com/solaborate/HELLOMotors/tree/master/sample
